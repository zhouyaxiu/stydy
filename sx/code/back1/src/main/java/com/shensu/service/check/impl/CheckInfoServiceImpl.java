package com.shensu.service.check.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shensu.constant.Constant;
import com.shensu.mybatis.entity.check.*;
import com.shensu.mybatis.entity.highStudent.HighStudent;
import com.shensu.mybatis.entity.inforInspection.InforReplantMess;
import com.shensu.mybatis.entity.school.SchoolManageBean;
import com.shensu.mybatis.entity.systemmanage.SysStation;
import com.shensu.mybatis.mapper.check.CheckInfoMapper;
import com.shensu.mybatis.mapper.check.SaasInfoMapper;
import com.shensu.mybatis.mapper.systemmanage.SysAreaMapper;
import com.shensu.service.check.CheckInfoService;
import com.shensu.service.check.SaasInfoService;
import com.shensu.service.check.util.RevaccinationStatUtil;
import com.shensu.service.check.util.RxrtService;
import com.shensu.service.check.util.model.CheckPassChildInfo;
import com.shensu.service.check.util.model.CheckPassVO;
import com.shensu.service.check.util.model.ResponseBean;
import com.shensu.util.*;
import com.shensu.utils.FindStationMess;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName CheckInfoServiceImpl.java
 * @Description TODO
 * @createTime 2023年04月07日 09:05
 */
@Service
public class CheckInfoServiceImpl implements CheckInfoService {

    private static final Logger logger = LoggerFactory.getLogger(CheckInfoServiceImpl.class);

    @Autowired
    private CheckInfoMapper checkInfoMapper;

    @Autowired
    private SaasInfoService saasInfoService;
    @Resource
    private SaasInfoMapper saasInfoMapper;

    @Resource
    private SysAreaMapper sysAreaMapper;

    @Resource
    private FindStationMess fIndStationMess;

    @Override
    public PageInfo<StudentBase> getList(String schId, String year, String rounds, String grade, String clazz,
                                         String isjz, String status, String name, String idcard, String code, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        String sqlState  ="";
        if(StringUtils.isNotEmpty(status)){
            if("2".equals(status)){
                sqlState="and (s.stu_isjzz ='1' or s.stu_isjz_origin='2' or s.stu_isjz_origin is null )";
            }else{
                sqlState="and s.stu_isjz_origin = '"+status+"'";
            }
        }
        List<StudentBase> studentList = checkInfoMapper.queryStudentList(null, null, null, schId, year, rounds, grade, clazz, isjz, sqlState, name, idcard, code);
        studentList.forEach(ss->ss.setGrade( Constant.GRADE_MAP.get(ss.getGrade())));
        //处理d0XX文本为页面要求的✔、✖
        studentList.forEach((this::changeStuToShow));
        return new PageInfo<>(studentList);
    }
    /**
     * 处理d0XX文本为页面要求的✔、✖
     * -1 :O
     * 0: ✖
     * 1: ✔
     * 2: ✔ (蓝色, 表示补种完成)
     * 3: 未到接种月龄 (空着)
     *
     * @return
     */
    public   void   changeStuToShow(StudentBase checkstu) {
        //将查验过后的学生进行处理
        String[] str ={"D063","D064","D065","D001","D009","D010","D011","D012","D015","D016","D017","D018","D037","D059","D060","D040","D041","D045","D046","D032","D033","D073","D074","D050","D132"};
        try {
            for(int  i=0;i<str.length;i++){
                String methodNameGet  ="get"+str[i];
                String methodNameSet  ="set"+str[i];
                Method methodGet  = checkstu.getClass().getMethod(methodNameGet);
                Method methodSet = checkstu.getClass().getMethod(methodNameSet,String.class);
                String getValue  = (String)methodGet.invoke(checkstu);
                //判断getValue是不是时间
                if(DateUtils.isDate(getValue)){
                    //如果是日期的话判断下是不是特殊日期
                    String year  = getValue.substring(0,4);
                    //禁忌已患超级不中显示null
                    if("1983".equals(year)||"1986".equals(year)||"1984".equals(year)){
                        methodSet.invoke(checkstu,"-1");
                    }else if("1982".equals(year)||"1981".equals(year)){
                        methodSet.invoke(checkstu,"0");
                    }else{
                        //判断是否是补种
                        String methodNameGetAfter ="getB"+str[i].substring(1)+"StateAfter";
                        String stateAfterValue = (String)checkstu.getClass().getMethod(methodNameGetAfter).invoke(checkstu);
                        String beforeValue=(String)checkstu.getClass().getMethod("getB"+str[i].substring(1)+"State").invoke(checkstu);
                        //如果已经接种，并且是stateAfter是已经接种
                        if(RevaccinationStatUtil.hasVaccineState.equals(stateAfterValue) &&RevaccinationStatUtil.NOT_VACCINE_ALL.equals( beforeValue)){
                            methodSet.invoke(checkstu,"2");
                        }else{
                            methodSet.invoke(checkstu,"1");
                        }
                    }
                }else{
                    if("未到接种时间".equals(getValue)){
                        methodSet.invoke(checkstu," ");
                    }else if("到期未种".equals(getValue)){
                        methodSet.invoke(checkstu,"0");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //转换户籍类型
        //checkstu.setStuType(Student.changeStuType(checkstu.getStuType()));
    }


    /**
     * 处理d0XX文本为页面要求的✔、✖
     * 0: ✖
     * 1: ✔
     * 2: ✔ (蓝色, 表示补种完成)
     * 3: 未到接种月龄 (空着)
     * @return
     */
    public void reset4d(StudentBase student) {

        //已种 hasVaccineState = "11"
        //已到月龄,未种 reachTimeButNotVaccineState = "1"
        //未达到月龄,未种 notReachTimeNotVaccineState = "21"

        String regEx = "^[d]{1}[0-9]{3}?$";
        Pattern pattern = Pattern.compile(regEx);
        PropertyDescriptor pd;
        Method readMethod;
        Method writeMethod;
        for (Field field : StudentBase.class.getDeclaredFields()) {
            String fieldName = field.getName();
            if (pattern.matcher(fieldName).find()) {
                try {
                    String prop = "b" + fieldName.substring(fieldName.length() - 3);
                    pd = new PropertyDescriptor(prop + "State", StudentBase.class);
                    readMethod = pd.getReadMethod();
                    String state = (String) readMethod.invoke(student); //初次查验状态
                    pd = new PropertyDescriptor(prop + "StateAfter", StudentBase.class);
                    readMethod = pd.getReadMethod();
                    String stateAfter = (String) readMethod.invoke(student); //后续查验状态

                    String status = "3"; //默认空置
                    if (RevaccinationStatUtil.hasVaccineState.equals(state)) {
                        status = "1";
                    }
                    if (RevaccinationStatUtil.reachTimeButNotVaccineState.equals(state)) {
                        if (RevaccinationStatUtil.hasVaccineState.equals(stateAfter)) {
                            status = "2";
                        } else {
                            status = "0";
                        }
                    }
                    if (RevaccinationStatUtil.notReachTimeNotVaccineState.equals(state)) {
                        status = "3";
                    }

                    pd = new PropertyDescriptor(fieldName, StudentBase.class);
                    writeMethod = pd.getWriteMethod();
                    writeMethod.invoke(student, status);

                } catch (Exception e) {
                    logger.info("字段[" + fieldName + "]不存在或get方法未找到!");
                }
            }
        }
    }

    @Override
    public StudentBase getOneById(String rowkey) {
        StudentBase student = checkInfoMapper.queryOneById(rowkey);
        return student;
    }

    @Override
    public int batchDeleteById(List<String> rowkeyList) {
        int rows = checkInfoMapper.batchDeleteByIds(rowkeyList);
        return rows;
    }

    @Transactional
    @Override
    public int batchUpdate(List<String> rowkeyList, String year, String grade, String clazz, String rounds) {
        int rows = checkInfoMapper.batchUpdate(rowkeyList, year, grade, clazz, rounds);
        return rows;
    }

    @Override
    public PageInfo<Student> getReplantRosterPage(String schId, String year, String rounds, String grade,String className, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Student> studentList = checkInfoMapper.queryReplantRosterList(null, null, null, schId, year, rounds, grade,className);
        for (Student student: studentList) {
            List<String> needReplantList = getNeedReplantVaccinumList(student);
            String needReplantRoster = StringUtils.join(needReplantList, ",");
            student.setNeedReplantRoster(needReplantRoster);
        }
        return new PageInfo<>(studentList);
    }

    /**
     * 初始化补种疫苗统计结果
     * @param name 疫苗名称
     * @param needVacNum 需补种
     * @param vacNum 已补种
     * @param vacRate 已补种率
     */
    private JSONObject initReplantVacStatValue(String name, Integer needVacNum, Integer vacNum, String vacRate) {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("needVacNum", needVacNum);
        json.put("vacNum", vacNum);
        json.put("vacRate", vacRate);
        return json;
    }

    @Override
    public PageInfo<JSONObject> getReplantVaccinumStatistics(Integer level, String areaCode, String schType, String year, String rounds, String birthStart, String birthEnd, String replantStart, String replantEnd, String isFull) {
        ReplantVacStatBean statBean = getReplantVaccinumStat(level, areaCode, schType, year, rounds, birthStart, birthEnd, replantStart, replantEnd, isFull);
        List<JSONObject> list = new ArrayList<>();
        //卡介苗
        JSONObject json = initReplantVacStatValue("卡介苗", statBean.getKaJieNeedReplantSubtotal(), statBean.getKaJieHasReplantSubtotal(), statBean.getKaJieHasReplantRate());
        list.add(json);
        // 乙肝
        json = initReplantVacStatValue("乙肝疫苗", statBean.getYiGanNeedReplantSubtotal(), statBean.getYiGanHasReplantSubtotal(), statBean.getYiGanHasReplantRate());
        list.add(json);
        // 脊灰
        json = initReplantVacStatValue("脊灰疫苗", statBean.getJiHuiNeedReplantSubtotal(), statBean.getJiHuiHasReplantSubtotal(), statBean.getJiHuiHasReplantRate());
        list.add(json);
        // 百白破
        json = initReplantVacStatValue("百白破", statBean.getBaiBaiPoNeedReplantSubtotal(), statBean.getBaiBaiPoHasReplantSubtotal(), statBean.getBaiBaiPoHasReplantRate());
        list.add(json);
        // 白破
        json = initReplantVacStatValue("白破疫苗", statBean.getBaiPoNeedReplantSubtotal(), statBean.getBaiPoHasReplantSubtotal(), statBean.getBaiPoHasReplantRate());
        list.add(json);
        // 麻疹类疫苗
        json = initReplantVacStatValue("麻腮风疫苗", statBean.getMaFengNeedReplantSubtotal(), statBean.getMaFengHasReplantSubtotal(), statBean.getMaFengHasReplantRate());
        list.add(json);
        // A群流脑疫苗
        json = initReplantVacStatValue("A群流脑", statBean.getAQunLiuNaoNeedReplantSubtotal(), statBean.getAQunLiuNaoHasReplantSubtotal(), statBean.getAQunLiuNaoHasReplantRate());
        list.add(json);
        // A+C群流脑疫苗
        json = initReplantVacStatValue("A+C群流脑", statBean.getACQunLiuNaoNeedReplantSubtotal(), statBean.getACQunLiuNaoHasReplantSubtotal(), statBean.getACQunLiuNaoHasReplantRate());
        list.add(json);
        // 乙脑
        json = initReplantVacStatValue("乙脑疫苗", statBean.getYiNaoJianDuHuoNeedReplantSubtotal(), statBean.getYiNaoJianDuHuoHasReplantSubtotal(), statBean.getYiNaoJianDuHuoHasReplantRate());
        list.add(json);
        // 甲肝
        json = initReplantVacStatValue("甲肝疫苗", statBean.getJiaGanJianDuHuoNeedReplantSubtotal(), statBean.getJiaGanJianDuHuoHasReplantSubtotal(), statBean.getJiaGanJianDuHuoHasReplantRate());
        list.add(json);
        // 合计
        json = initReplantVacStatValue("合计", statBean.getNeedReplantTotal(), statBean.getHasReplantTotal(), statBean.getHasReplantTotalRate());
        list.add(json);
        return new PageInfo<>(list);
    }

    @Override
    public ReplantVacStatBean getReplantVaccinumStat(Integer level, String areaCode, String schType, String year, String rounds, String birthStart, String birthEnd, String replantStart, String replantEnd, String isFull) {
        ReplantVacStatBean statBean = checkInfoMapper.queryReplantVaccinumStat(level, areaCode, schType, year, rounds, birthStart, birthEnd, replantStart, replantEnd, isFull);
        //合并
        //卡介苗
        int b001NeedReplant = statBean.getB001NeedReplant();
        statBean.setKaJieNeedReplantSubtotal(b001NeedReplant);
        int b001HasReplant = statBean.getB001HasReplant();
        statBean.setKaJieHasReplantSubtotal(b001HasReplant);
        statBean.setKaJieHasReplantRate(Util.accuracyInt(statBean.getKaJieHasReplantSubtotal(), statBean.getKaJieNeedReplantSubtotal(), 2));

        // 乙肝
        int b063NeedReplant = statBean.getB063NeedReplant();
        int b064NeedReplant = statBean.getB064NeedReplant();
        int b065NeedReplant = statBean.getB065NeedReplant();
        statBean.setYiGanNeedReplantSubtotal(b063NeedReplant+b064NeedReplant+b065NeedReplant);
        int b063HasReplant = statBean.getB063HasReplant();
        int b064HasReplant = statBean.getB064HasReplant();
        int b065HasReplant = statBean.getB065HasReplant();
        statBean.setYiGanHasReplantSubtotal(b063HasReplant+b064HasReplant+b065HasReplant);
        statBean.setYiGanHasReplantRate(Util.accuracyInt(statBean.getYiGanHasReplantSubtotal(), statBean.getYiGanNeedReplantSubtotal(), 2));

        // 脊灰
        int b009NeedReplant = statBean.getB009NeedReplant();
        int b010NeedReplant = statBean.getB010NeedReplant();
        int b011NeedReplant = statBean.getB011NeedReplant();
        int b012NeedReplant = statBean.getB012NeedReplant();
        statBean.setJiHuiNeedReplantSubtotal(b009NeedReplant+b010NeedReplant+b011NeedReplant+b012NeedReplant);
        int b009HasReplant = statBean.getB009HasReplant();
        int b010HasReplant = statBean.getB010HasReplant();
        int b011HasReplant = statBean.getB011HasReplant();
        int b012HasReplant = statBean.getB012HasReplant();
        statBean.setJiHuiHasReplantSubtotal(b009HasReplant+b010HasReplant+b011HasReplant+b012HasReplant);
        statBean.setJiHuiHasReplantRate(Util.accuracyInt(statBean.getJiHuiHasReplantSubtotal(), statBean.getJiHuiNeedReplantSubtotal(), 2));

        // 百白破
        int b015HasReplant = statBean.getB015HasReplant();
        int b015NeedReplant = statBean.getB015NeedReplant();
        int b016HasReplant = statBean.getB016HasReplant();
        int b016NeedReplant = statBean.getB016NeedReplant();
        int b017HasReplant = statBean.getB017HasReplant();
        int b017NeedReplant = statBean.getB017NeedReplant();
        int b018HasReplant = statBean.getB018HasReplant();
        int b018NeedReplant = statBean.getB018NeedReplant();
        statBean.setBaiBaiPoHasReplantSubtotal(b015HasReplant+b016HasReplant+b017HasReplant+b018HasReplant);
        statBean.setBaiBaiPoNeedReplantSubtotal(b015NeedReplant+b016NeedReplant+b017NeedReplant+b018NeedReplant);
        statBean.setBaiBaiPoHasReplantRate(Util.accuracyInt(statBean.getBaiBaiPoHasReplantSubtotal(), statBean.getBaiBaiPoNeedReplantSubtotal(), 2));

        // 白破
        int b037HasReplant = statBean.getB037HasReplant();
        int b037NeedReplant = statBean.getB037NeedReplant();
        int b038HasReplant = statBean.getB038HasReplant();
        int b038NeedReplant = statBean.getB038NeedReplant();
        int b039HasReplant = statBean.getB039HasReplant();
        int b039NeedReplant = statBean.getB039NeedReplant();
        statBean.setBaiPoHasReplantSubtotal(b037HasReplant+b038HasReplant+b039HasReplant);
        statBean.setBaiPoNeedReplantSubtotal(b037NeedReplant+b038NeedReplant+b039NeedReplant);
        statBean.setBaiPoHasReplantRate(Util.accuracyInt(statBean.getBaiPoHasReplantSubtotal(), statBean.getBaiPoNeedReplantSubtotal(), 2));

        // 麻疹类疫苗
        int b059HasReplant = statBean.getB059HasReplant();
        int b059NeedReplant = statBean.getB059NeedReplant();
        int b060HasReplant = statBean.getB060HasReplant();
        int b060NeedReplant = statBean.getB060NeedReplant();
        statBean.setMaFengHasReplantSubtotal(b059HasReplant+b060HasReplant);
        statBean.setMaFengNeedReplantSubtotal(b059NeedReplant+b060NeedReplant);
        statBean.setMaFengHasReplantRate(Util.accuracyInt(statBean.getMaFengHasReplantSubtotal(), statBean.getMaFengNeedReplantSubtotal(), 2));

        // A群流脑疫苗
        int b040HasReplant = statBean.getB040HasReplant();
        int b040NeedReplant = statBean.getB040NeedReplant();
        int b041HasReplant = statBean.getB041HasReplant();
        int b041NeedReplant = statBean.getB041NeedReplant();
        statBean.setAQunLiuNaoHasReplantSubtotal(b040HasReplant+b041HasReplant);
        statBean.setAQunLiuNaoNeedReplantSubtotal(b040NeedReplant+b041NeedReplant);
        statBean.setAQunLiuNaoHasReplantRate(Util.accuracyInt(statBean.getAQunLiuNaoHasReplantSubtotal(), statBean.getAQunLiuNaoNeedReplantSubtotal(), 2));

        // A+C群流脑疫苗
        int b045HasReplant = statBean.getB045HasReplant();
        int b045NeedReplant = statBean.getB045NeedReplant();
        int b046HasReplant = statBean.getB046HasReplant();
        int b046NeedReplant = statBean.getB046NeedReplant();
        statBean.setACQunLiuNaoHasReplantSubtotal(b045HasReplant+b046HasReplant);
        statBean.setACQunLiuNaoNeedReplantSubtotal(b045NeedReplant+b046NeedReplant);
        statBean.setACQunLiuNaoHasReplantRate(Util.accuracyInt(statBean.getACQunLiuNaoHasReplantSubtotal(), statBean.getACQunLiuNaoNeedReplantSubtotal(), 2));

        // 乙脑
        int b032HasReplant = statBean.getB032HasReplant();
        int b032NeedReplant = statBean.getB032NeedReplant();
        int b033HasReplant = statBean.getB033HasReplant();
        int b033NeedReplant = statBean.getB033NeedReplant();
        statBean.setYiNaoJianDuHuoHasReplantSubtotal(b032HasReplant+b033HasReplant);
        statBean.setYiNaoJianDuHuoNeedReplantSubtotal(b032NeedReplant+b033NeedReplant);
        statBean.setYiNaoJianDuHuoHasReplantRate(Util.accuracyInt(statBean.getYiNaoJianDuHuoHasReplantSubtotal(), statBean.getYiNaoJianDuHuoNeedReplantSubtotal(), 2));

        // 甲肝
        int b073HasReplant = statBean.getB073HasReplant();
        int b073NeedReplant = statBean.getB073NeedReplant();
        int b074HasReplant = statBean.getB074HasReplant();
        int b074NeedReplant = statBean.getB074NeedReplant();

        statBean.setJiaGanJianDuHuoHasReplantSubtotal(b073HasReplant+b074HasReplant);
        statBean.setJiaGanJianDuHuoNeedReplantSubtotal(b073NeedReplant+b074NeedReplant);
        statBean.setJiaGanJianDuHuoHasReplantRate(Util.accuracyInt(statBean.getJiaGanJianDuHuoHasReplantSubtotal(), statBean.getJiaGanJianDuHuoNeedReplantSubtotal(), 2));

        statBean.setHasReplantTotal(statBean.getKaJieHasReplantSubtotal() + statBean.getYiGanHasReplantSubtotal() + statBean.getJiHuiHasReplantSubtotal() + statBean.getBaiBaiPoHasReplantSubtotal()
                + statBean.getBaiPoHasReplantSubtotal() + statBean.getMaFengHasReplantSubtotal() + statBean.getAQunLiuNaoHasReplantSubtotal()
                + statBean.getACQunLiuNaoHasReplantSubtotal() + statBean.getYiNaoJianDuHuoHasReplantSubtotal() + statBean.getJiaGanJianDuHuoHasReplantSubtotal());
        statBean.setNeedReplantTotal(statBean.getKaJieNeedReplantSubtotal() + statBean.getYiGanNeedReplantSubtotal() + statBean.getJiHuiNeedReplantSubtotal() + statBean.getBaiBaiPoNeedReplantSubtotal()
                + statBean.getBaiPoNeedReplantSubtotal() + statBean.getMaFengNeedReplantSubtotal() + statBean.getAQunLiuNaoNeedReplantSubtotal()
                + statBean.getACQunLiuNaoNeedReplantSubtotal() + statBean.getYiNaoJianDuHuoNeedReplantSubtotal() + statBean.getJiaGanJianDuHuoNeedReplantSubtotal());
        statBean.setHasReplantTotalRate(Util.accuracyInt(statBean.getHasReplantTotal(), statBean.getNeedReplantTotal(), 2));

        return statBean;
    }

    @Override
    public PageInfo<AreaCdcUserVacData> getVacCertCheckStat(Integer level, String areaCode, String schType, String year, String rounds) {
        List<AreaCdcUserVacData> dataList = checkInfoMapper.queryVacCertCheckStat(level, areaCode, schType, year, rounds);
        //计算合计
        AreaCdcUserVacData totalBean = new AreaCdcUserVacData();
        totalBean.setAreaName("合计");
        for (AreaCdcUserVacData bean : dataList) {
            totalBean.setNeedCySchNum(totalBean.getNeedCySchNum() + bean.getNeedCySchNum());
            totalBean.setCySchNum(totalBean.getCySchNum() + bean.getCySchNum());
            totalBean.setStuCyNum(totalBean.getStuCyNum() + bean.getStuCyNum());
            totalBean.setRealCyNum(totalBean.getRealCyNum() + bean.getRealCyNum());
            totalBean.setHasCardNum(totalBean.getHasCardNum() + bean.getHasCardNum());
            totalBean.setPatchCardNum(totalBean.getPatchCardNum() + bean.getPatchCardNum());
            totalBean.setQcjzCount(totalBean.getQcjzCount() + bean.getQcjzCount());
            totalBean.setNeedReplantNum(totalBean.getNeedReplantNum() + bean.getNeedReplantNum());
            totalBean.setHasReplant(totalBean.getHasReplant() + bean.getHasReplant());
            totalBean.setKajieNeed(totalBean.getKajieNeed() + bean.getKajieNeed());
            totalBean.setKajieHas(totalBean.getKajieHas() + bean.getKajieHas());
            totalBean.setYiganNeed(totalBean.getYiganNeed() + bean.getYiganNeed());
            totalBean.setYiganHas(totalBean.getYiganHas() + bean.getYiganHas());
            totalBean.setJihuiNeed(totalBean.getJihuiNeed() + bean.getJihuiNeed());
            totalBean.setJihuiHas(totalBean.getJihuiHas() + bean.getJihuiHas());
            totalBean.setBaibaipoNeed(totalBean.getBaibaipoNeed() + bean.getBaibaipoNeed());
            totalBean.setBaibaipoHas(totalBean.getBaibaipoHas() + bean.getBaibaipoHas());
            totalBean.setBaipoNeed(totalBean.getBaipoNeed() + bean.getBaipoNeed());
            totalBean.setBaipoHas(totalBean.getBaipoHas() + bean.getBaipoHas());
            totalBean.setMafengNeed(totalBean.getMafengNeed() + bean.getMafengNeed());
            totalBean.setMafengHas(totalBean.getMafengHas() + bean.getMafengHas());
            totalBean.setAqunliunaoNeed(totalBean.getAqunliunaoNeed() + bean.getAqunliunaoNeed());
            totalBean.setAqunliunaoHas(totalBean.getAqunliunaoHas() + bean.getAqunliunaoHas());
            totalBean.setAcqunliunaoNeed(totalBean.getAcqunliunaoNeed() + bean.getAcqunliunaoNeed());
            totalBean.setAcqunliunaoHas(totalBean.getAcqunliunaoHas() + bean.getAcqunliunaoHas());
            totalBean.setYinaojianduNeed(totalBean.getYinaojianduNeed() + bean.getYinaojianduNeed());
            totalBean.setYinaojianduHas(totalBean.getYinaojianduHas() + bean.getYinaojianduHas());
            totalBean.setJiaganjianduNeed(totalBean.getJiaganjianduNeed() + bean.getJiaganjianduNeed());
            totalBean.setJiaganjianduHas(totalBean.getJiaganjianduHas() + bean.getJiaganjianduHas());
        }
        dataList.add(totalBean);
        return new PageInfo<>(dataList);
    }

    @Override
    public void outPut(ExcelUtil excelUtil, JSONObject info, List<StudentBase> list) {
        //设置查询条件
        /*String areaName = info.getString("areaName");
        String areaCode = info.getString("areaCode");
        String schType = info.getString("schType");
        String schName = info.getString("schName");*/
        String year = info.getString("year");
        String rounds = info.getString("rounds");
        String grade = info.getString("grade");
        String clazz = info.getString("class");
        String isjz = info.getString("isjz");
        String status = info.getString("status");
        String name = info.getString("name");
        String idcard = info.getString("idcard");
        String code = info.getString("code");
        StringBuilder  bb  = new StringBuilder();
        if (StringUtils.isNotBlank(year)) {
            bb.append("  学年：" + year);
        }
        if (StringUtils.isNotBlank(rounds)) {
            bb.append("  轮次：" + Constant.ROUND_MAP.get(rounds));
        }
        if (StringUtils.isNotBlank(grade)) {
            bb.append("  年级：" + Constant.GRADE_MAP.get(grade));
        }
        if (StringUtils.isNotBlank(clazz)) {
            bb.append("  班级：" + clazz);
        }
        if (StringUtils.isNotBlank(isjz)) {
            String isJz ="";
            if("0".equals(isjz)){
                isJz="是";
            }else if("1".equals(isjz)){
                isJz="否-需补种";
            }else if("-1".equals(isjz)){
                isJz="否-无需补种";
            }else {
                isJz="否";
            }
            bb.append("  是否种全：" +isJz);
        }
        if (StringUtils.isNotBlank(status)) {
            bb.append("   查验状态：" + Constant.CHECK_STATE_MAP.getOrDefault(status, " "));
        }
        if (StringUtils.isNotBlank(name)) {
            bb.append("  儿童姓名：" + name);
        }
        if (StringUtils.isNotBlank(idcard)) {
            bb.append("   身份证件号码：" + idcard);

        }
        if (StringUtils.isNotBlank(code)) {
            bb.append("   受种者编码：" + code);
        }
        excelUtil.fillCellData(1, (short) 0, bb.toString());

        // 创建新行
        int firstRowNum = 5;//第一行记录在excel文件中的位置
        HSSFFont font = excelUtil.createFont();
        font.setFontHeightInPoints((short) 9);
        font.setFontName("宋体");
        HSSFCellStyle style = excelUtil.createCellStyle();
        style = excelUtil.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);// 上边框
        style.setBorderBottom(BorderStyle.THIN);// 下边框
        style.setBorderLeft(BorderStyle.THIN);// 左边框
        style.setBorderRight(BorderStyle.THIN);// 右边框
        style.setAlignment(HorizontalAlignment.CENTER);// 左右居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
        style.setFont(font);// 设置样式的字体
        style.setFont(font);// 设置样式的字体
        style.setWrapText(true);//自动换行
        Iterator<StudentBase> iterator = list.iterator();
        int curRow = 0;
        //构建所有的行和列
        for (int j = 0; j < list.size(); j++) {
            curRow = firstRowNum + j ;
            HSSFRow mess = excelUtil.createRow(curRow);
            for (int i = 0; i < 42; i++) {
                excelUtil.createCell(mess, (short) i);
                excelUtil.setCellStyle(curRow, (short) i, style);
            }
        }
        //查验状态、是否种全、学校名称、年份、轮次、所在年级、所在班级、儿童编码、手机号码、儿童姓名、性别、出生日期、是否有接种证、儿童条码、户籍类型、查验日期、
        //卡介苗1、乙肝疫苗1-3、脊灰疫苗1-4、百白破疫苗1-4、白破疫苗1、麻疹类疫苗1-2、流脑疫苗（A群流脑疫苗1-2，A＋C群流脑疫苗1-2）、乙脑疫苗1-2、甲肝疫苗1、*水痘疫苗1-2
        int i = 0, rownum;
        int colnum;
        for (StudentBase bean : list) {
            String isjzz=" ";
            if("0".equals(bean.getStuIsjzz())||"0".equals(bean.getStuIsbjzz())){
                isjzz="是";
            }else if(( "1".equals(bean.getStuIsjzz()) &&   "1".equals(bean.getStuIsbjzz())) || ( "1".equals(bean.getStuIsjzz()) && StringUtils.isEmpty(bean.getStuIsbjzz())) ||  "2".equals(bean.getStuIsjz())){

                isjzz="否";
            }
            rownum = firstRowNum + i;
            colnum = 0;
            excelUtil.fillCellData(rownum, (short) colnum++, String.valueOf(i+1), style);     //序号
            excelUtil.fillCellData(rownum, (short) colnum++, com.shensu.util.StringUtils.isEmpty(Constant.CHECK_STATE_MAP.get(bean.getStuIsjzOrigin()))?"无接种证":Constant.CHECK_STATE_MAP.get(bean.getStuIsjzOrigin()), style);     //查验状态
            excelUtil.fillCellData(rownum, (short) colnum++, "0".equals(bean.getStuIsjz()) ? "是" : ("1".equals(bean.getStuIsjz())?"否-需补种":("-1".equals(bean.getStuIsjz())?"否-无需补种":" ")), style);     //是否种全
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getSchname(), style);     //学校名称
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getAcademicYear(), style);     //年份
            excelUtil.fillCellData(rownum, (short) colnum++, Constant.ROUND_MAP.get(bean.getRounds()), style);     //轮次
            excelUtil.fillCellData(rownum, (short) colnum++,bean.getGrade(), style);     //所在年级
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getClazzName(), style);     //所在班级
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getStuNo(), style);     //儿童编码
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getStuPhone(), style);     //手机号码
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getStuName(), style);     //儿童姓名
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getStuSex(), style);     //性别
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getStuBirth(), style);     //出生日期
            excelUtil.fillCellData(rownum, (short) colnum++, isjzz, style);     //是否有接种证
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getStuImuno(), style);     //儿童条码
            excelUtil.fillCellData(rownum, (short) colnum++, Constant.REGTYPE_MAP.get(bean.getStuType()), style);     //户籍类型
            excelUtil.fillCellData(rownum, (short) colnum++, DateUtil.formatShort(bean.getFirstStuSmrq()), style);     //查验日期


            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD001()), style);     //卡介苗1
            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD063()), style);     //乙肝疫苗1
            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD064()), style);     //乙肝疫苗2
            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD065()), style);     //乙肝疫苗3

            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD009()), style);     //脊灰疫苗1
            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD010()), style);     //脊灰疫苗2
            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD011()), style);     //脊灰疫苗3
            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD012()), style);     //脊灰疫苗4

            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD015()), style);     //百白破疫苗1
            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD016()), style);     //百白破疫苗2
            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD017()), style);     //百白破疫苗3
            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD018()), style);     //百白破疫苗4

            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD037()), style);     //白破疫苗1

            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD059()), style);     //麻疹类疫苗1
            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD060()), style);     //麻疹类疫苗2

            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD040()), style);     //A群流脑疫苗1
            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD041()), style);     //A群流脑疫苗2

            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD045()), style);     //A＋C群流脑疫苗1
            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD046()), style);     //A＋C群流脑疫苗2

            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD032()), style);     //乙脑疫苗1
            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD033()), style);     //乙脑疫苗2

            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD073()), style);     //甲肝疫苗1
            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD074()), style);     //甲肝疫苗1

            excelUtil.fillCellData(rownum, (short) colnum++, changeStateToShow(bean.getD050()), style);     //水痘疫苗1
            excelUtil.fillCellData(rownum, (short) colnum, changeStateToShow(bean.getD132()), style);     //水痘疫苗12
            i++;
        }
    }

    private  String  changeStateToShow(String  str ){
        String   changeResult  = " ";
        if("0".equals(str)){
            changeResult="X";
        }else if("1".equals(str)||"2".equals(str)){
            changeResult="√";
        }else if("-1".equals(str)){
            changeResult="O";
        }else {
            changeResult=" ";
        }
        return changeResult;
    }
    @Override
    public void outPutReplantRoster(ExcelUtil excelUtil, JSONObject info, List<Student> list) {
        //设置查询条件
        /*String areaName = info.getString("areaName");
        String areaCode = info.getString("areaCode");
        String schType = info.getString("schType");
        String schName = info.getString("schName");*/
        String year = info.getString("year");
        String rounds = info.getString("rounds");
        String grade = info.getString("grade");
        String className = info.getString("className");
        StringBuffer buffer = new StringBuffer();
        if (StringUtils.isNotBlank(year)) {
            buffer.append(" 学年：" + year);
        }
        if (StringUtils.isNotBlank(rounds)) {
            buffer.append(" 轮次：" + Constant.ROUND_MAP.get(rounds));
        }
        if (StringUtils.isNotBlank(grade)) {
            buffer.append(" 年级：" + Constant.GRADE_MAP.get(grade));
        }
        if (StringUtils.isNotBlank(className)) {
            buffer.append(" 班级：" + className );
        }
        excelUtil.fillCellData(1, (short) 0, buffer.toString());

        // 创建新行
        int firstRowNum = 4;//第一行记录在excel文件中的位置
        int firstPageCount = 1;//第一页的记录行个数
        int pageCount = 1;//每页显示的记录行个数
        int newRowCount = excelUtil.getNewRowCount(firstPageCount, list.size(), pageCount);//需要创建的行数
        int organLastRowNum = 4; //原始模板的最后一行
        int lastRowNum = organLastRowNum + newRowCount; //创建新行后的最后一行
        if (newRowCount > 0) {
            List<Integer> colnumlist = new ArrayList<>();
            for (int i = 0; i < 142; i++) {
                colnumlist.add(i);
            }
            //填表人、备注等占用的行下移到指定位置
            excelUtil.createNewRow(organLastRowNum + 1, colnumlist, lastRowNum + 1);
            excelUtil.fillCellData(lastRowNum + 1, (short) 0, excelUtil.getCellData(organLastRowNum + 1, (short) 0));
            //删除填表人、备注等占用的行
            excelUtil.removeOldRow(organLastRowNum + 1);
            excelUtil.createNewRowAll(organLastRowNum, colnumlist, newRowCount);//创建新行
        }
        //格式
        HSSFSheet sheet = excelUtil.getWb().getSheetAt(0);
        HSSFCellStyle style = sheet.getRow(firstRowNum).getCell(1).getCellStyle();

        //序号，学校，年级，班级，儿童姓名，出生日期，补充疫苗名称，手机号码
        int i = 0, rownum;
        int colnum;
        for (Student bean : list) {
            rownum = firstRowNum + i;
            colnum = 0;
            excelUtil.fillCellData(rownum, (short) colnum++, String.valueOf(i+1), style);     //序号
            //excelUtil.fillCellData(rownum, (short) colnum++, bean.getSchName(), style);     //学校名称
            excelUtil.fillCellData(rownum, (short) colnum++, Constant.GRADE_MAP.get(bean.getGrade()), style);     //所在年级
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getClazzName(), style);     //所在班级
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getStuName(), style);     //儿童姓名
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getStuBirth(), style);     //出生日期
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getNeedReplantRoster(), style);     //补充疫苗名称
            excelUtil.fillCellData(rownum, (short) colnum, bean.getStuPhone(), style);     //手机号码
            i++;
        }
    }

    @Override
    public void outputReplantVacStat(ExcelUtil excelUtil, JSONObject info, List<ReplantVacStatBean> list) {
        //设置查询条件
       /* String areaName = info.getString("areaName");
        String areaCode = info.getString("areaCode");
        String schType = info.getString("schType");
        String schName = info.getString("schName");*/
        String year = info.getString("year");
        String rounds = info.getString("rounds");
        String birthStart = info.getString("birthStart");
        String birthEnd = info.getString("birthEnd");
        String replantStart = info.getString("replantStart");
        String replantEnd = info.getString("replantEnd");
        String isFull = info.getString("isFull");
        int titleNum = 2;
        /*String supArea = "地区：" + areaName + " " + areaCode;
        excelUtil.fillCellData(1, (short) 0, supArea);
        if (StringUtils.isNotBlank(schType)) {
            excelUtil.fillCellData(1, (short) titleNum, "学校类型：" + (schType.equals("0") ? "幼托机构" : "小学") );
            titleNum += 2;
        }
        if (StringUtils.isNotBlank(schName)) {
            excelUtil.fillCellData(1, (short) titleNum,  "学校名称：" + schName);
            titleNum += 2;
        }*/
        if (StringUtils.isNotBlank(year)) {
            excelUtil.fillCellData(1, (short) titleNum, "年份：" + year);
            titleNum += 2;
        }
        if (StringUtils.isNotBlank(rounds)) {
            excelUtil.fillCellData(1, (short) titleNum, "轮次：" + Constant.ROUND_MAP.get(rounds));
            titleNum += 2;
        }
        if (StringUtils.isNotBlank(birthStart)) {
            excelUtil.fillCellData(1, (short) titleNum, "生日日期开始：" + birthStart);
        }
        if (StringUtils.isNotBlank(birthEnd)) {
            excelUtil.fillCellData(1, (short) titleNum, "生日日期结束：" + birthEnd);
        }
        if (StringUtils.isNotBlank(replantStart)) {
            excelUtil.fillCellData(1, (short) titleNum, "补种日期开始：" + replantStart);
        }
        if (StringUtils.isNotBlank(replantEnd)) {
            excelUtil.fillCellData(1, (short) titleNum, "补种日期结束：" + replantEnd);
        }
        if (StringUtils.isNotBlank(isFull)) {
            excelUtil.fillCellData(1, (short) titleNum, "是否完成补种：" + (isFull.equals("0") ? "是" : "否"));
        }

        // 创建新行
        int firstRowNum = 4;//第一行记录在excel文件中的位置
        int firstPageCount = 1;//第一页的记录行个数
        int pageCount = 1;//每页显示的记录行个数
        int newRowCount = excelUtil.getNewRowCount(firstPageCount, 11, pageCount);//需要创建的行数
        int organLastRowNum = 4; //原始模板的最后一行
        int lastRowNum = organLastRowNum + newRowCount; //创建新行后的最后一行
        if (newRowCount > 0) {
            List<Integer> colnumlist = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                colnumlist.add(i);
            }
            //填表人、备注等占用的行下移到指定位置
            excelUtil.createNewRow(organLastRowNum + 1, colnumlist, lastRowNum + 1);
            excelUtil.fillCellData(lastRowNum + 1, (short) 0, excelUtil.getCellData(organLastRowNum + 1, (short) 0));
            //删除填表人、备注等占用的行
            excelUtil.removeOldRow(organLastRowNum + 1);
            excelUtil.createNewRowAll(organLastRowNum, colnumlist, newRowCount);//创建新行
        }
        //格式
        HSSFSheet sheet = excelUtil.getWb().getSheetAt(0);
        HSSFCellStyle style = sheet.getRow(firstRowNum).getCell(1).getCellStyle();

        //疫苗种类、需补种剂次数、补种剂次数、补种率(%)
        ReplantVacStatBean bean = list.get(0);
        if (bean != null) {
            int rownum = firstRowNum;
            excelUtil.fillCellData(rownum, (short) 0, "卡介苗", style);
            excelUtil.fillCellData(rownum, (short) 1, bean.getKaJieNeedReplantSubtotal(), style);
            excelUtil.fillCellData(rownum, (short) 2, bean.getKaJieHasReplantSubtotal(), style);
            excelUtil.fillCellData(rownum, (short) 3, bean.getKaJieHasReplantRate(), style);

            rownum++;
            excelUtil.fillCellData(rownum, (short) 0, "乙肝疫苗", style);
            excelUtil.fillCellData(rownum, (short) 1, bean.getYiGanNeedReplantSubtotal(), style);
            excelUtil.fillCellData(rownum, (short) 2, bean.getYiGanHasReplantSubtotal(), style);
            excelUtil.fillCellData(rownum, (short) 3, bean.getYiGanHasReplantRate(), style);

            rownum++;
            excelUtil.fillCellData(rownum, (short) 0, "脊灰疫苗", style);
            excelUtil.fillCellData(rownum, (short) 1, bean.getJiHuiNeedReplantSubtotal(), style);
            excelUtil.fillCellData(rownum, (short) 2, bean.getJiHuiHasReplantSubtotal(), style);
            excelUtil.fillCellData(rownum, (short) 3, bean.getJiHuiHasReplantRate(), style);

            rownum++;
            excelUtil.fillCellData(rownum, (short) 0, "百白破", style);
            excelUtil.fillCellData(rownum, (short) 1, bean.getBaiBaiPoNeedReplantSubtotal(), style);
            excelUtil.fillCellData(rownum, (short) 2, bean.getBaiBaiPoHasReplantSubtotal(), style);
            excelUtil.fillCellData(rownum, (short) 3, bean.getBaiBaiPoHasReplantRate(), style);

            rownum++;
            excelUtil.fillCellData(rownum, (short) 0, "白破疫苗", style);
            excelUtil.fillCellData(rownum, (short) 1, bean.getBaiPoNeedReplantSubtotal(), style);
            excelUtil.fillCellData(rownum, (short) 2, bean.getBaiPoHasReplantSubtotal(), style);
            excelUtil.fillCellData(rownum, (short) 3, bean.getBaiPoHasReplantRate(), style);

            rownum++;
            excelUtil.fillCellData(rownum, (short) 0, "麻疹", style);
            excelUtil.fillCellData(rownum, (short) 1, bean.getMaFengNeedReplantSubtotal(), style);
            excelUtil.fillCellData(rownum, (short) 2, bean.getMaFengHasReplantSubtotal(), style);
            excelUtil.fillCellData(rownum, (short) 3, bean.getMaFengHasReplantRate(), style);

            rownum++;
            excelUtil.fillCellData(rownum, (short) 0, "A群流脑", style);
            excelUtil.fillCellData(rownum, (short) 1, bean.getAQunLiuNaoNeedReplantSubtotal(), style);
            excelUtil.fillCellData(rownum, (short) 2, bean.getAQunLiuNaoHasReplantSubtotal(), style);
            excelUtil.fillCellData(rownum, (short) 3, bean.getAQunLiuNaoHasReplantRate(), style);

            rownum++;
            excelUtil.fillCellData(rownum, (short) 0, "A+C群流脑", style);
            excelUtil.fillCellData(rownum, (short) 1, bean.getACQunLiuNaoNeedReplantSubtotal(), style);
            excelUtil.fillCellData(rownum, (short) 2, bean.getACQunLiuNaoHasReplantSubtotal(), style);
            excelUtil.fillCellData(rownum, (short) 3, bean.getACQunLiuNaoHasReplantRate(), style);

            rownum++;
            excelUtil.fillCellData(rownum, (short) 0, "乙脑疫苗", style);
            excelUtil.fillCellData(rownum, (short) 1, bean.getYiNaoJianDuHuoNeedReplantSubtotal(), style);
            excelUtil.fillCellData(rownum, (short) 2, bean.getYiNaoJianDuHuoHasReplantSubtotal(), style);
            excelUtil.fillCellData(rownum, (short) 3, bean.getYiNaoJianDuHuoHasReplantRate(), style);

            rownum++;
            excelUtil.fillCellData(rownum, (short) 0, "甲肝疫苗", style);
            excelUtil.fillCellData(rownum, (short) 1, bean.getJiaGanJianDuHuoNeedReplantSubtotal(), style);
            excelUtil.fillCellData(rownum, (short) 2, bean.getJiaGanJianDuHuoHasReplantSubtotal(), style);
            excelUtil.fillCellData(rownum, (short) 3, bean.getJiaGanJianDuHuoHasReplantRate(), style);

            rownum++;
            excelUtil.fillCellData(rownum, (short) 0, "合计", style);
            excelUtil.fillCellData(rownum, (short) 1, bean.getNeedReplantTotal(), style);
            excelUtil.fillCellData(rownum, (short) 2, bean.getHasReplantTotal(), style);
            excelUtil.fillCellData(rownum, (short) 3, bean.getHasReplantTotalRate(), style);
        }

    }

    @Override
    public void outputVacCertCheckStat(ExcelUtil excelUtil, JSONObject info, List<AreaCdcUserVacData> list) {
        //设置查询条件
        /*String areaName = info.getString("areaName");
        String areaCode = info.getString("areaCode");
        String schType = info.getString("schType");*/
        String year = info.getString("year");
        String rounds = info.getString("rounds");
        int titleNum = 2;
        /*String supArea = "地区：" + areaName + " " + areaCode;
        excelUtil.fillCellData(1, (short) 0, supArea);
        if (StringUtils.isNotBlank(schType)) {
            excelUtil.fillCellData(1, (short) titleNum, "学校类型：" + (schType.equals("0") ? "幼托机构" : "小学") );
            titleNum += 2;
        }*/
        if (StringUtils.isNotBlank(year)) {
            excelUtil.fillCellData(1, (short) titleNum, "年份：" + year);
            titleNum += 2;
        }
        if (StringUtils.isNotBlank(rounds)) {
            excelUtil.fillCellData(1, (short) titleNum, "轮次：" + Constant.ROUND_MAP.get(rounds));
        }

        // 创建新行
        int firstRowNum = 7;//第一行记录在excel文件中的位置
        int firstPageCount = 1;//第一页的记录行个数
        int pageCount = 1;//每页显示的记录行个数
        int newRowCount = excelUtil.getNewRowCount(firstPageCount, list.size(), pageCount);//需要创建的行数
        int organLastRowNum = 4; //原始模板的最后一行
        int lastRowNum = organLastRowNum + newRowCount; //创建新行后的最后一行
        if (newRowCount > 0) {
            List<Integer> colnumlist = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                colnumlist.add(i);
            }
            //填表人、备注等占用的行下移到指定位置
            excelUtil.createNewRow(organLastRowNum + 1, colnumlist, lastRowNum + 1);
            excelUtil.fillCellData(lastRowNum + 1, (short) 0, excelUtil.getCellData(organLastRowNum + 1, (short) 0));
            //删除填表人、备注等占用的行
            excelUtil.removeOldRow(organLastRowNum + 1);
            excelUtil.createNewRowAll(organLastRowNum, colnumlist, newRowCount);//创建新行
        }
        //格式
        HSSFSheet sheet = excelUtil.getWb().getSheetAt(0);
        HSSFCellStyle style = sheet.getRow(firstRowNum).getCell(1).getCellStyle();

        //报告单位，托幼机构小学（应查验数，查验数）、入托入学人数，查验人数，持接种证人数，补接种证人数，完成全程接种人数，需补种疫苗人数，完成全程补种人数，
        //各个疫苗的应补种剂次和补种剂次数（卡介苗、乙肝疫苗、脊灰疫苗、百白破疫苗、白破疫苗、麻疹类疫苗、流脑疫苗（A群流脑疫苗，A＋C群流脑疫苗）、乙脑疫苗、甲肝疫苗、*水痘疫苗）
        int i = 0, rownum;
        int colnum;
        for (AreaCdcUserVacData bean : list) {
            rownum = firstRowNum + i;
            colnum = 0;
            //excelUtil.fillCellData(rownum, (short) colnum++, String.valueOf(i+1), style);     //序号
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getAreaName(), style);     //报告单位
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getNeedCySchNum(), style);     //托幼机构小学应查验数
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getCySchNum(), style);     //托幼机构小学查验数
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getStuCyNum(), style);     //入托入学人数(所有儿童数)
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getRealCyNum(), style);     //查验人数(已查验的儿童数)
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getHasCardNum(), style);     //持预防接种证人数
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getPatchCardNum(), style);     //补预防接种证人数
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getQcjzCount(), style);     //完成全程接种人数
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getNeedReplantNum(), style);     //需补种疫苗人数
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getHasReplant(), style);     //完成全程补种人数

            excelUtil.fillCellData(rownum, (short) colnum++, bean.getKajieNeed(), style);     //卡介苗应补种剂次
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getKajieHas(), style);     //卡介苗补种剂次
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getYiganNeed(), style);     //乙肝应补种剂次
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getYiganHas(), style);     //乙肝补种剂次
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getJihuiNeed(), style);     //脊灰应补种剂次
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getJihuiHas(), style);     //脊灰补种剂次
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getBaibaipoNeed(), style);     //百白破应补种剂次
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getBaibaipoHas(), style);     //百白破补种剂次
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getBaipoNeed(), style);     //白破应补种剂次
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getBaipoHas(), style);     //白破补种剂次
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getMafengNeed(), style);     //麻腮风应补种剂次
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getMafengHas(), style);     //麻腮风补种剂次
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getAqunliunaoNeed(), style);     //A群流脑应补种剂次
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getAqunliunaoHas(), style);     //A群流脑补种剂次
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getAcqunliunaoNeed(), style);     //A群C群流脑应补种剂次
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getAcqunliunaoHas(), style);     //A群C群流脑补种剂次
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getYinaojianduNeed(), style);     //乙脑应补种剂次
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getYinaojianduHas(), style);     //乙脑补种剂次
            excelUtil.fillCellData(rownum, (short) colnum++, bean.getJiaganjianduNeed(), style);     //甲肝应补种剂次
            excelUtil.fillCellData(rownum, (short) colnum, bean.getJiaganjianduHas(), style);     //甲肝补种剂次

            i++;
        }
    }

    @Override
    public PageInfo<InforReplantMess> queryReplateMess(String schId, String year, String rounds, String grade, String clazz, String isjz, String isjzz, String status, String name, String idcard, String code, List<String> rowkeyList) {
        List<Student> studentList = checkInfoMapper.queryReplateMess(null, null, null, schId, year, rounds, grade, clazz, "1", isjzz, status, name, idcard, code, rowkeyList);
        List<InforReplantMess> list = new ArrayList<>();
        for (Student student: studentList) {
            InforReplantMess inforReplantMess = new InforReplantMess();
            inforReplantMess.setSchName(student.getSchName());
            inforReplantMess.setClaLevel(student.getGrade());
            inforReplantMess.setClaName(student.getClazzName());
            inforReplantMess.setStuName(student.getStuName());
            inforReplantMess.setStuNo(student.getStuNo());
            inforReplantMess.setStuSex(student.getStuSex());
            inforReplantMess.setStuBirth(student.getStuBirth());
            inforReplantMess.setStuAddr(student.getFResideAddr());
            inforReplantMess.setIfFull(student.getStuIsjz());
            inforReplantMess.setParentName(student.getStuParentname());
            SysStation belongStation =  fIndStationMess.findStationMess(student.getStuBelongStationcode());
            if(belongStation!=null) {
                inforReplantMess.setBelongPalceStation(belongStation.getOrganName());
                inforReplantMess.setBelongPlaceAddress(belongStation.getOrganAddress());
                inforReplantMess.setBelongPhone(belongStation.getLinkPhone());
            }
            inforReplantMess.setBelongSchoolStation(student.getBelongSchoolStation());
            Map<String, String> replantMap = this.getReplantMap(student);
            inforReplantMess.setReplantMap(replantMap);
            list.add(inforReplantMess);
        }
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<InforReplantMess> queryReplantCard(String schId, String year, String rounds, String grade, String clazz, String isjz, String isjzz, String status, String name, String idcard, String code, List<String> rowkeyList) {
        List<Student> studentList = checkInfoMapper.queryReplateMess(null, null, null, schId, year, rounds, grade, clazz, isjz, "1", status, name, idcard, code, rowkeyList);
        List<InforReplantMess> list = new ArrayList<>();
        for (Student student: studentList) {
            InforReplantMess inforReplantMess = new InforReplantMess();
            inforReplantMess.setSchName(student.getSchName());
            inforReplantMess.setClaLevel(student.getGrade());
            inforReplantMess.setClaName(student.getClazzName());
            inforReplantMess.setStuName(student.getStuName());
            inforReplantMess.setStuNo(student.getStuNo());
            inforReplantMess.setStuSex(student.getStuSex());
            inforReplantMess.setStuBirth(student.getStuBirth());
            inforReplantMess.setStuAddr(student.getFResideAddr());
            inforReplantMess.setIfFull(student.getStuIsjz());
            inforReplantMess.setParentName(student.getStuParentname());
            SysStation belongStation =  fIndStationMess.findStationMess(student.getStuBelongStationcode());
            if(belongStation!=null) {
                inforReplantMess.setBelongPalceStation(belongStation.getOrganName());
                inforReplantMess.setBelongPlaceAddress(belongStation.getOrganAddress());
                inforReplantMess.setBelongPhone(belongStation.getLinkPhone());
            }
            inforReplantMess.setBelongSchoolStation(student.getBelongSchoolStation());
            list.add(inforReplantMess);
        }
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<InforReplantMess> queryNoArchive(String schId, String year, String rounds, String grade, String clazz, String isjz, String isjzz, String status, String name, String idcard, String code, List<String> rowkeyList) {
        List<Student> studentList = checkInfoMapper.queryReplateMess(null, null, null, schId, year, rounds, grade, clazz, isjz, isjzz, "2", name, idcard, code, rowkeyList);
        List<InforReplantMess> list = new ArrayList<>();
        for (Student student: studentList) {
            InforReplantMess inforReplantMess = new InforReplantMess();
            inforReplantMess.setSchName(student.getSchName());
            inforReplantMess.setClaLevel(student.getGrade());
            inforReplantMess.setClaName(student.getClazzName());
            inforReplantMess.setStuName(student.getStuName());
            inforReplantMess.setStuNo(student.getStuNo());
            inforReplantMess.setStuCid(student.getCid());
            inforReplantMess.setStuSex(student.getStuSex());
            inforReplantMess.setStuBirth(student.getStuBirth());
            inforReplantMess.setStuAddr(student.getFResideAddr());
            inforReplantMess.setIfFull(student.getStuIsjz());
            inforReplantMess.setParentName(student.getStuParentname());
            inforReplantMess.setBelongPalceStation(student.getBelongPalceStation());
            inforReplantMess.setBelongPlaceAddress(student.getBelongPlaceAddress());
            inforReplantMess.setBelongPhone(student.getLinkPhone());
            inforReplantMess.setBelongSchoolStation(student.getBelongSchoolStation());
            list.add(inforReplantMess);
        }
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<InforReplantMess> queryCheckCert(String schId, String year, String rounds, String grade, String clazz, String isjz, String isjzz, String status, String name, String idcard, String code, List<String> rowkeyList) {
        List<Student> studentList = checkInfoMapper.queryReplateMess(null, null, null, schId, year, rounds, grade, clazz, "0", isjzz, status, name, idcard, code, rowkeyList);
        List<InforReplantMess> list = new ArrayList<>();
        for (Student student: studentList) {
            InforReplantMess inforReplantMess = new InforReplantMess();
            inforReplantMess.setSchName(student.getSchName());
            inforReplantMess.setClaLevel(student.getGrade());
            inforReplantMess.setClaName(student.getClazzName());
            inforReplantMess.setStuName(student.getStuName());
            inforReplantMess.setStuNo(student.getStuNo());
            inforReplantMess.setStuSex(student.getStuSex());
            inforReplantMess.setStuBirth(student.getStuBirth());
            inforReplantMess.setStuAddr(student.getFResideAddr());
            inforReplantMess.setIfFull(student.getStuIsjz());
            inforReplantMess.setParentName(student.getStuParentname());
            inforReplantMess.setBelongPalceStation(student.getBelongPalceStation());
            inforReplantMess.setBelongPlaceAddress(student.getBelongPlaceAddress());
            inforReplantMess.setBelongPhone(student.getLinkPhone());
            inforReplantMess.setBelongSchoolStation(student.getBelongSchoolStation());
            list.add(inforReplantMess);
        }
        return new PageInfo<>(list);
    }

    @Override
    public List<SchoolManageBean> getAllSchoolList() {
        return checkInfoMapper.queryAllSchoolList();
    }

    @Override
    public List<Student> queryStudentBySchIdAnySource(String schId) {
        return checkInfoMapper.queryStudentBySchId(schId);
    }

    public Map<String, String> getReplantMap(Student stu) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < Constant.fullList.size(); i++) {
            try {
                FullExcelField fef = Constant.fullList.get(i);
                Method getValueAfter = Student.class.getMethod("get" + fef.getFiledName() + "After");
                Object invoke = getValueAfter.invoke(stu);
                if (null==invoke) {
                    getValueAfter = Student.class.getMethod("get" + fef.getFiledName());
                    invoke = getValueAfter.invoke(stu);
                }
                if (RevaccinationStatUtil.reachTimeButNotVaccineState.equals(invoke)) {
                    if (com.shensu.util.StringUtils.isBlank(Constant.ymStr.get(fef.getFiledName()))) {
                        continue;
                    }
                    String ym =  Constant.ymStr.get(fef.getFiledName());
                    //将疫苗名称和剂次名称分来放在map集合中
                    if(com.shensu.util.StringUtils.isNumeric(ym.substring(ym.length()-1))){
                        map.put(ym.substring(0,ym.length()-1),"第"+ym.substring(ym.length()-1)+"剂次");
                    }else{
                        map.put(ym,"第1剂次");
                    }
                }

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return map;
    }

    /**
     * 新修改后需要补种的疫苗
     * 获得需补种疫苗
     * @param student
     * @return
     */
    public List<String> getNeedReplantVaccinumList(Student student) {
        Method getValueAfter;
        Method getValueState;
        List<String> needRelantList = new ArrayList<>();
        String needRelantStr = "";
        int num = 1;
        for (FullExcelField field : Constant.fullList) {
            try {
                //获取的是BXXXState 查验前的疫苗状态
                String filedName = field.getFiledName();
                //获取的是BXXXStateAfter 查验后的疫苗状态
                getValueAfter = Student.class.getMethod("get" + field.getFiledName() + "After");
                getValueState = Student.class.getMethod("get" + field.getFiledName() );
                //获取的是getBxxx 疫苗接种日期
                Method getValueAfter1 = Student.class.getMethod("get" + field.getFiledName().substring(0,field.getFiledName().length()-5));

                //获取state  和stateAfter的值
                String state = (String)getValueState.invoke(student);
                String stateAfter  =(String) getValueAfter.invoke(student);
                //System.out.println(RevaccinationStatUtil.reachTimeButNotVaccineState.equals(state));
                //System.out.println(RevaccinationStatUtil.reachTimeButNotVaccineState.equals(stateAfter) || StringUtils.isBlank(stateAfter));

                if (RevaccinationStatUtil.reachTimeButNotVaccineState.equals(stateAfter)
                        || (RevaccinationStatUtil.reachTimeButNotVaccineState.equals(state) && StringUtils.isBlank(stateAfter)) ) {
                    //System.out.println(Constant.ymStr.get(fef.getFiledName()));
                    if (StringUtils.isBlank(Constant.ymStr.get(field.getFiledName()))) {
                        continue;
                    }
                    needRelantStr += Constant.ymStr.get(field.getFiledName()) + ",";
                    //System.out.println(needRelantStr);
                    if (num % 4 == 0) {
                        needRelantStr = needRelantStr.substring(0, needRelantStr.length() - 1);
                        needRelantList.add(needRelantStr);
                        needRelantStr = "";
                    }
                    num++;
                }
            } catch (Exception e) {
                logger.info("字段[" + field.getFiledName() + "]不存在或get方法未找到!");
            }
        }

        if (needRelantStr.length() > 0) {
            needRelantStr = needRelantStr.substring(0, needRelantStr.length() - 1);
            needRelantList.add(needRelantStr);
        }
        // 疫苗名称替换
        List<String> needReplantVaccinumList = new ArrayList<>();
        for (String ymNaes: needRelantList) {
            String newYmName = ymNaes.replace("麻疹1", "麻风").replace("麻疹2", "麻腮风").replace("甲肝1", "甲肝");
            needReplantVaccinumList.add(newYmName);
        }
        return needReplantVaccinumList;
    }

    @Override
    public int checkByChildno(List<String> childnoList) {
        if (!CollectionUtils.isEmpty(childnoList)) {
            List<Student> studentList = checkInfoMapper.queryStuInfoByCode(childnoList);

            return checkChildInfo(studentList);
        }
        return 0;
    }

    @Override
    public int checkByRowkey(List<Long> rowkeyList) {
        if (!CollectionUtils.isEmpty(rowkeyList)) {
            List<Student> studentList = checkInfoMapper.queryStuInfoByRowkey(rowkeyList);
            for(int i=0;i<studentList.size();i++){
                Student student =studentList.get(i);
                List<InfoBean> list=null;
                if(StringUtils.isNotBlank(student.getStuNo())) {
                    list = saasInfoMapper.queryInfoByFchildno(student.getStuNo());
                }else{
                    list = saasInfoMapper.queryInfoByCid(student.getCid());
                }
                if(list!=null && list.size()>0){
                    InfoBean ss  = list.get(0);
                    Student stuNew = null;
                    if(stuNew!=null){
                        student.setFchildno(stuNew.getStuNo());
                        student.setStuNo(stuNew.getStuNo());
                        student.setStuName(stuNew.getStuName());
                        student.setStuBirth(stuNew.getStuBirth());
                        student.setStuImuno(stuNew.getStuImuno());
                        student.setStuSex(stuNew.getStuSex());
                        student.setStuPhone(stuNew.getStuPhone());
                        student.setStuFathername(stuNew.getStuFathername());
                        student.setStuFatherPhone(stuNew.getStuFatherPhone());
                        student.setStuMothername(stuNew.getStuMothername());
                        student.setStuMotherPhone(stuNew.getStuMotherPhone());
                        student.setStuType(stuNew.getStuType());
                        student.setStuBelongStationcode(stuNew.getStuBelongStationcode());
                        student.setCid(stuNew.getCid());
                        // 未建档学生查验不需要更新补证状态
                        if (!"2".equals(student.getStuIsjz())) {
                            student.setStuIsbjzz(stuNew.getStuIsjzz());
                        }
                        student.setFResideAddr(stuNew.getFResideAddr());

                    }
                }else{
                    studentList.remove(student);
                }
            }
            return checkChildInfo(studentList);
        }
        return 0;
    }

    /**
     * 查验学生
     * @param childList childList
     * @return Integer
     */
    @Transactional
    @Override
    public Integer checkChildInfo(List<Student> childList) {
        long start = System.currentTimeMillis();
        List<FutureTask<String>> futureTasks = new ArrayList<>();
        int poolSize = Runtime.getRuntime().availableProcessors();

        AtomicInteger count = new AtomicInteger(0);
        ExecutorService executorService = new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(5000)); //有界队列
        for (Student student: childList) {
            Callable<String> callable = () -> {
                boolean check4Saas = Student.SOURCE_SAAS.equals(student.getStuSourceid())||Student.SOURCE_EXCEL.equals(student.getStuSourceid())||Student.SOURCE_DOCTOR.equals(student.getStuSourceid()); //满足状态未建档或信息来源是扫码,则从saas库获取接种记录
                excuteCheck(student, check4Saas); //查验信息
                return Thread.currentThread().getName();
            };
            FutureTask<String> futureTask = new FutureTask<>(callable);
            futureTasks.add(futureTask);
            executorService.submit(futureTask);
        }

        for (FutureTask<String> task : futureTasks) {
            try {
                String completeThread = task.get();
                count.incrementAndGet();
                logger.info("完成任务: " + completeThread);
            } catch (InterruptedException | ExecutionException e) {
                logger.error(e.getMessage(), e);
            }
        }
        executorService.shutdown();

        //更新查验结果
        for (Student student : childList) {
            if (student.getIsChecked())
                checkInfoMapper.updateCheckInfo(student);
        }

        long end = System.currentTimeMillis();
        logger.info("使用时间：" + (end - start) / 1000.0 + "秒");
        return count.get();
    }

    /**
     * 查验学生
     * @param child child
     * @return Integer
     */
    @Override
    public Integer checkChildInfo(Student child) {
        boolean check4Saas = Student.SOURCE_SAAS.equals(child.getStuSourceid())||Student.SOURCE_EXCEL.equals(child.getStuSourceid())||Student.SOURCE_DOCTOR.equals(child.getStuSourceid()); //满足状态未建档或信息来源是扫码,则从saas库获取接种记录
        excuteCheck(child, check4Saas); //查验信息
        int count = checkInfoMapper.updateCheckInfo(child); //更新查验结果
        return count;
    }

    /**
     * 查验核心方法
     * @param student 学生信息
     * @param isSaas 是否有saas信息的学生
     * @return
     */
    public Student excuteCheck(Student student, boolean isSaas) {
        List<JzjlBean> list;
        if (isSaas) {
            if (StringUtils.isBlank(student.getStuNo()) && Student.STATE_NO_ARCHIVE.equals(student.getStuIsjz())) { //只有身份证
                list = saasInfoService.getJzjlByCid(student.getCid());
            } else {
                list = saasInfoService.getJzjlByFchildno(student.getStuNo());
            }
            //TODO 把fshotkind转换成特殊日期
            for (JzjlBean jzjl : list) {
                //接种类型不为空 且 接种类型不为正常接种 且 (接种日期为空或已种同效疫苗)
                if (StringUtils.isNotBlank(jzjl.getFShotKind()) && !PlantTypeAndDate.NORMAL.equals(jzjl.getFShotKind() )
                        && (StringUtils.isBlank(jzjl.getJzrq()) || PlantTypeAndDate.REPLACE.equals(jzjl.getFShotKind()))) {
                    String specialPlantDate = PlantTypeAndDate.SPECIAL_SHOT_DATE_MAP.get(jzjl.getFShotKind());
                    if("B138".equals(jzjl.getFbactid())||"B139".equals(jzjl.getFbactid())||"B140".equals(jzjl.getFbactid())||"B141".equals(jzjl.getFbactid())||"B699".equals(jzjl.getFbactid())||"B040".equals(jzjl.getFbactid())||"B041".equals(jzjl.getFbactid())){
                        // if("B040".equals(jzjl.getFbactid())||"B041".equals(jzjl.getFbactid())){
                        jzjl.setJzrq("");
                    }else {
                        jzjl.setJzrq(specialPlantDate);
                    }
                } else {
                    jzjl.setJzrq(jzjl.getJzrq());
                }
            }
            initJzjl4StuInfo(student, list);
        } else {
            list = getJzjlByStuInfo(student);
        }
        CheckPassVO checkPassResult = getCheckPassResult(student.getStuName(), student.getStuBirth(), list);

        if (!BeanUtil.isEmpty(student.getStuNo())) {
            String current = DateUtil.formatShort(new Date());
            student.setLastStuSmrq(current);
            if (StringUtils.isBlank(student.getFirstStuSmrq()) ) {
                student.setFirstStuSmrq(current);
            }
            // 未建档学生接种证状态更新
            if ("2".equals(student.getStuIsjz())) {
               // student.setStuIsjzz(StringUtils.isNotEmpty(student.getStuImuno()) ? "0" : "1");
                 student.setStuIsbjzz(StringUtils.isNotEmpty(student.getStuNo()) ? "0" : "1");
            }
            //如果是首次查验需要保存首次查验的状态在isjzOrigin字段中
            boolean hasNotCheck = StringUtils.isBlank(student.getStuIsjz());
            if(StringUtils.isBlank(student.getStuNo())){
                student.setStuIsjz(Student.STATE_NO_ARCHIVE);
            }else {
                student.setStuIsjz(checkPassResult.getIsPassCheck());
                RevaccinationStatUtil.initFbactInfo(student, checkPassResult.getJzjlList()); //初始化查验结果(针次)
            }
            //更新初始或年度查验的状态(是否种全, 是否完成补种)
            if (hasNotCheck) {
                if(StringUtils.isBlank(student.getStuNo())){
                    student.setStuIsjzOrigin(Student.STATE_NO_ARCHIVE);
                    student.setStuIsfullOrigin(student.getStuIsfull());
                }else {
                    student.setStuIsjzOrigin(checkPassResult.getIsPassCheck());
                    student.setStuIsfullOrigin(student.getStuIsfull());
                }
            }
            student.setIsChecked(true); //经过此次查验
        }
        return student;
    }

    /**
     * 获取查验结果
     * @param name
     * @param birth
     * @param list
     * @return
     */
    public CheckPassVO getCheckPassResult(String name, String birth, List<JzjlBean> list) {

        CheckPassChildInfo childInfo = new CheckPassChildInfo();
        //处理日期格式为yyyy-MM-dd
        childInfo.setBirth(DateUtil.formatShort(DateUtil.parseDate(birth)));
        childInfo.setJzjlList(list);
        childInfo.setName(name);

        CheckPassVO checkPassVO = new CheckPassVO();
        checkPassVO.setIsPassCheck("1");
        RxrtService rxrtService = new RxrtService();
        //查验核心逻辑
        JSONObject childInfoJson = (JSONObject) JSON.toJSON(childInfo);
        System.out.println(JSON.toJSONString(childInfoJson, JSONWriter.Feature.PrettyFormat));

        ResponseBean responseBean = rxrtService.getChildRxrtToPt(childInfoJson);
        JSONObject data = (JSONObject) JSON.toJSON(responseBean.getData());
        CheckPassVO checkPassVO1 = data.toJavaObject(CheckPassVO.class);

        BeanUtil.copyProperties(checkPassVO1, checkPassVO);
        checkPassVO.setCode(responseBean.getCode());

        if (data.containsKey("isPassCheck")) {
            String passCheck = data.getString("isPassCheck");
            checkPassVO.setIsPassCheck(passCheck);
        }

        System.out.println(JSON.toJSONString(checkPassVO, JSONWriter.Feature.PrettyFormat));
        return checkPassVO;
    }

    public List<JzjlBean> getJzjlByStuInfo(Student student) {
        String regEx = "^[b]{1}[0-9]{3}?$";
        Pattern pattern = Pattern.compile(regEx);
        List<JzjlBean> list = new ArrayList<>();
        PropertyDescriptor pd;
        Method readMethod;
        for (Field field : Student.class.getDeclaredFields()) {
            String fieldName = field.getName();
            if (pattern.matcher(fieldName).find()) {
                try {
                    pd = new PropertyDescriptor(fieldName, Student.class);
                    readMethod = pd.getReadMethod();
                    String shotDate = (String) readMethod.invoke(student);
                    if (StringUtils.isNotBlank(shotDate)
                            && (shotDate.length() != 10 || !shotDate.contains("-"))) { //非yyyy-MM-dd格式
                        shotDate = DateUtil.formatShort(shotDate);
                    }
                    if (StringUtils.isBlank(shotDate)) {
                        continue;
                    }
                    JzjlBean jzjlBean = new JzjlBean();
                    jzjlBean.setFbactid(fieldName.toUpperCase());
                    jzjlBean.setJzrq(shotDate);
                    list.add(jzjlBean);
                } catch (Exception e) {
                    logger.info("字段[" + fieldName + "]不存在或get方法未找到!");
                }
            }
        }
        return list;
    }

    /**
     * 复制saas库的接种记录到本地的学生信息字段中
     * @param student
     * @param list
     */
    public void initJzjl4StuInfo(Student student, List<JzjlBean> list) {
        String regEx = "^[b]{1}[0-9]{3}?$";
        Pattern pattern = Pattern.compile(regEx);
        Map<String, String> jzjlMap = new HashMap<>(list.size());
        for (JzjlBean jzjlBean : list) {
            if (StringUtils.isNotBlank(jzjlBean.getJzrq())) {
                jzjlMap.put(jzjlBean.getFbactid(), jzjlBean.getJzrq());
            }
        }
        PropertyDescriptor pd;
        Method readMethod;
        Method writeMethod;
        for (Field field : Student.class.getDeclaredFields()) {
            String fieldName = field.getName();
            if (pattern.matcher(fieldName).find()) {
                try {
                    String fbactid = fieldName.toUpperCase();
                    if (!jzjlMap.containsKey(fbactid)) {
                        continue;
                    }
                    String jzrq = jzjlMap.get(fbactid);
                    pd = new PropertyDescriptor(fieldName, Student.class);
                    readMethod = pd.getReadMethod();
                    String shotDate = (String) readMethod.invoke(student);
                    if (StringUtils.isNotBlank(shotDate) && (shotDate.length() != 10 || !shotDate.contains("-"))) { //非yyyy-MM-dd格式
                        shotDate = DateUtil.formatShort(shotDate);
                    }
                    if (jzrq.equals(shotDate)) {
                        continue;
                    }
                    writeMethod = pd.getWriteMethod();
                    writeMethod.invoke(student, jzrq);

                } catch (Exception e) {
                    logger.info("字段[" + fieldName + "]不存在或get方法未找到!");
                }
            }
        }
    }
}
