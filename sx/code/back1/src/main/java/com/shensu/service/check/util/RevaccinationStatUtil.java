package com.shensu.service.check.util;

import com.shensu.mybatis.entity.check.Student;
import com.shensu.service.check.util.model.CheckPassJzjl;
import com.shensu.service.check.util.model.VaccineState;
import com.shensu.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName RevaccinationStatUtil.java
 * @Description 接种记录、补种状态计算
 * @createTime 2022年12月14日 13:52
 */
public class RevaccinationStatUtil {

    private static final Logger logger = LoggerFactory.getLogger(RevaccinationStatUtil.class);

    /**
     * 全程接种
     */
    public static String VACCINE_ALL = "0";

    /**
     * 未全程接种
     */
    public static String NOT_VACCINE_ALL = "1";

    /**
     * 已种
     */
    public static String hasVaccineState = "11";

    /**
     * 已到月龄,未种
     */
    public static String reachTimeButNotVaccineState = "1";

    /**
     * 未达到月龄,未种
     */
    public static String notReachTimeNotVaccineState = "21";

    /**
     * 未种，但不需要提示补种
     */
    public static String notVaccineButNotNeedRestockState = "31";

    /**
     * 超过最大月龄或不需要接种，不判断是否未种
     */
    public static String overTimeOrNoNeedState = "99";

    //到期未种  需补种
    //未到接种时间
    //已种替代疫苗 已种但时间未知 都是已接种
    //超期不种
    //禁忌  已患
    //需要补种的："到期未种", "不详", "父亲/母亲/监护人要求不种", "未接种"
    //特殊状态 : "不详", "禁忌", "已患", "超期不种", "已种替代疫苗", "父亲/母亲/监护人要求不种", "已种但时间未知" };
    public static Map<String, String> SHOT_STATE_MAP = new HashMap<>();
    static {
        SHOT_STATE_MAP.put("到期未种", "1");
        SHOT_STATE_MAP.put("未到接种时间", "21");
        //SHOT_STATE_MAP.put("超期不种", "99");
    }

    /**
     * 特殊日期状态代表不需要再接种了
     *
     * 代码类型	中文解释	对应特殊日期年份
     * 1    正常接种
     * 2    禁忌	        1983
     * 3    不详	        1981
     * 4    已种	        1985
     * 5    家长拒种	    1982
     * 6    已种同效苗    1980
     * 7    超期不种	    1984
     * 8    已患	        1986
     * 针对已种未种的判断：
     * 1，4，6算已种，3，5算未种需提示补种，2，7，8 算未种但是不需提示补种。
     */
    public static Map<String, VaccineState> SPECIAL_SHOT_DATE_MAP = new HashMap<>();
    static {
        //1，4，6算已种
        SPECIAL_SHOT_DATE_MAP.put("已种替代疫苗", new VaccineState("1980-01-01", "11"));
        SPECIAL_SHOT_DATE_MAP.put("已种但时间未知", new VaccineState("1985-01-01", "11"));

        //3，5算未种需提示补种
        SPECIAL_SHOT_DATE_MAP.put("不详", new VaccineState("1981-01-01", "1"));
        SPECIAL_SHOT_DATE_MAP.put("父亲/母亲/监护人要求不种", new VaccineState("1982-01-01", "1"));

        //2，7，8算未种但是不需提示补种
        SPECIAL_SHOT_DATE_MAP.put("禁忌", new VaccineState("1983-01-01", "31"));
        SPECIAL_SHOT_DATE_MAP.put("已患", new VaccineState("1986-01-01", "31"));
        SPECIAL_SHOT_DATE_MAP.put("超期不种", new VaccineState("1984-01-01", "31"));
    }

    //B093对应B059 麻疹1
    //B089对应B060 麻疹2
    public static Map<String, String> MAZHEN_NICKNAME_MAP = new HashMap<>();
    static {
        MAZHEN_NICKNAME_MAP.put("B093", "B059");
        MAZHEN_NICKNAME_MAP.put("B089", "B060");
    }

    public static Set<String> VARICELLA_NICKNAME_SET = new HashSet<>();
    static {
        VARICELLA_NICKNAME_SET.add("B050");
        VARICELLA_NICKNAME_SET.add("B132");
    }

    /**
     * 给用于列表页面展示数据的剂次D0XX赋值
     * @param stuinfo
     * @param list
     * @return
     */
    public static Student initFbactInfo(Student stuinfo, List<CheckPassJzjl> list) {
        String regEx = "^[A-Za-z]{1}[0-9]{3}[A-Za-z]*$";
        Pattern pattern = Pattern.compile(regEx);

        //代码类型	中文解释	对应特殊日期年份
        //1    正常接种
        //2    禁忌	        1983
        //3    不详	        1981
        //4    已种	        1985
        //5    家长拒种	    1982
        //6    已种同效苗     1980
        //7    超期不种	    1984
        //8    已患	        1986
        //针对已种未种的判断：
        //1，4，6算已种，3，5算未种需提示补种，2，7，8算未种但是不需提示补种。

        //计算补种是否完成
        int needNum = 0;
        int hasNum = 0;

        for (CheckPassJzjl checkPassJzjl : list) {
            PropertyDescriptor pd;
            String shotDate = checkPassJzjl.getShotdate();
            String fbactid = checkPassJzjl.getFbactid();

            /*if (fbactid.equals("B050") || fbactid.equals("B132") ) {
                System.out.println(fbactid);
            }*/

            //处理查验接口麻疹类剂次名称不一致, B093对应B059 麻疹1, B089对应B060 麻疹2
            if (MAZHEN_NICKNAME_MAP.containsKey(fbactid)) {
                fbactid = MAZHEN_NICKNAME_MAP.get(fbactid);
            }

            try {
                if (StringUtils.isNotBlank(fbactid) && StringUtils.isNotBlank(shotDate) && pattern.matcher(fbactid.toLowerCase()).find()) {
                    String prop = "d" + fbactid.substring(fbactid.length() - 3);
                    //logger.info("字段名称: " + prop);
                    //疫苗针次接种时间、状态
                    pd = new PropertyDescriptor(prop, Student.class);
                    Method writeMethod = pd.getWriteMethod();
                    //处理D字段
                    if (SPECIAL_SHOT_DATE_MAP.containsKey(shotDate)) {
                        //特殊日期状态转换成合法的日期赋值到D0XX字段
                        String specialShotDate = SPECIAL_SHOT_DATE_MAP.get(shotDate).getShotDate();
                        writeMethod.invoke(stuinfo, specialShotDate);
                    } else {
                        //将查验后的接种日期或各种状态赋值到D0XX字段
                        writeMethod.invoke(stuinfo, shotDate);
                    }
                    //上一次查验的状态
                    pd = new PropertyDescriptor(fbactid + "State", Student.class);
                    Method readMethod = pd.getReadMethod();
                    String lastState = (String) readMethod.invoke(stuinfo);

                     //处理B字段
                    if (DateUtil.isDate(shotDate)) { //合法的日期，不包含特殊日期
                        if (StringUtils.isBlank(lastState)) {//初次查验
                            pd = new PropertyDescriptor(fbactid + "State", Student.class);
                        } else { //非初次查验，未到月龄
                            pd = new PropertyDescriptor(fbactid + "StateAfter", Student.class);
                        }
                        writeMethod = pd.getWriteMethod();
                        writeMethod.invoke(stuinfo, hasVaccineState);
                    } else { //状态
                        //初次查验更新State
                        if (StringUtils.isBlank(lastState)) {
                            pd = new PropertyDescriptor(fbactid + "State", Student.class);
                        } else { //非初次查验
                            //TODO 替代为包含未接种意义的状态集合
                            //if (UtilForinforInspection.reachTimeButNotVaccine.equals(lastState)) {
                            pd = new PropertyDescriptor(fbactid + "StateAfter", Student.class);
                            //}
                        }
                        writeMethod = pd.getWriteMethod();
                        if (SHOT_STATE_MAP.containsKey(shotDate)) { //非特殊日期的状态
                            writeMethod.invoke(stuinfo, SHOT_STATE_MAP.get(shotDate));
                        }
                        if (SPECIAL_SHOT_DATE_MAP.containsKey(shotDate)) { //特殊日期状态
                            writeMethod.invoke(stuinfo, SPECIAL_SHOT_DATE_MAP.get(shotDate).getState()); //1，4，6 算已种，3，5算未种需提示补种，2，7，8算未种但是不需提示补种。
                        }
                    }

                    pd = new PropertyDescriptor(fbactid + "State", Student.class);
                    readMethod = pd.getReadMethod();
                    String currentState = (String) readMethod.invoke(stuinfo);

                    pd = new PropertyDescriptor(fbactid + "StateAfter", Student.class);
                    readMethod = pd.getReadMethod();
                    String currentStateAfter = (String) readMethod.invoke(stuinfo);

                    //stateAfter未种 或 初次查验未种，需要补种，不累计水痘
                    if (!VARICELLA_NICKNAME_SET.contains(fbactid)
                            && (reachTimeButNotVaccineState.equals(currentStateAfter)||notVaccineButNotNeedRestockState.equals(currentStateAfter) || (reachTimeButNotVaccineState.equals(currentState) && StringUtils.isBlank(currentStateAfter)))) {
                        System.out.print("需补种: " + fbactid + " - " + currentState + ", " + currentStateAfter + " | ");
                        needNum++;
                    }

                    //后续查验状态StateAfter为已种，算已补种，不累计水痘
                    if (!VARICELLA_NICKNAME_SET.contains(fbactid)
                            && (reachTimeButNotVaccineState.equals(currentState) && hasVaccineState.equals(currentStateAfter))) {
                        System.out.print("已补种: " + fbactid + " - " + currentState + ", " + currentStateAfter);
                        hasNum++;
                    }

                    System.out.println();
                }
            } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
                logger.info("字段[" + fbactid + "]不存在或get, set方法未找到!");
            }
        }

        if (needNum > 0) { //存在到期未种的, 需要补种
            stuinfo.setStuIsfull("1");
        } else {
            if (hasNum > 0) {//存在已补种的
                stuinfo.setStuIsfull("0");
            } else {
                stuinfo.setStuIsfull("1");
            }
        }
        return stuinfo;
    }


}
