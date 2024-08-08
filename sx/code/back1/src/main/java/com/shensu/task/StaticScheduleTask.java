package com.shensu.task;

import com.shensu.mybatis.entity.check.Student;
import com.shensu.mybatis.entity.inforInspection.UpdateStudentProperties;
import com.shensu.mybatis.entity.school.SchoolManageBean;
import com.shensu.service.check.CheckInfoService;
import com.shensu.service.check.SaasInfoService;
import com.shensu.service.systemmanage.SysUserService;
import com.shensu.util.DateUtil;
import com.shensu.util.Util;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zcy
 * @ClassName SaticScheduleTask
 * @Description 定时任务
 * @createTime 2022/8/18
 */
@EnableScheduling
@Component
public class StaticScheduleTask {
    /**
     * LOGGER : 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(StaticScheduleTask.class);

    @Autowired
    private CheckInfoService checkInfoService;

    @Autowired
    private SaasInfoService saasInfoService;

    @Autowired
    private SysUserService sysUserService;

    /**
     *  每日定时查验任务
     */
    //@Scheduled(cron = "0 11 01 * * ?")
    //@Scheduled(cron = "0 */5 * * * ?")
    private void checkDaily() {
        check(false);
        // 定时清楚用户禁用状态
        sysUserService.cleanUserStatus();
    }
    /**
     *  每年定时查验任务
     */
   // @Scheduled(cron = "0 38 10 9 6 ?")
    private void checkEveryYear() {
        check(true);
    }

    /**
     * 查验方法
     * @param eachYear 是否则是每年执行一次, 如是则清空状态
     */
    public int check(boolean eachYear) {

        final List<String> stuMsNameList = new ArrayList<>();
        final Map<String, Method> stuMsMap = new HashMap<>();

        Method[] stuMs = Student.class.getMethods();

        for (Method stuM : stuMs) {
            stuMsNameList.add(stuM.getName());
            stuMsMap.put(stuM.getName(), stuM);
        }

        long start = System.currentTimeMillis();
        List<FutureTask<String>> futureTasks = new ArrayList<>();
        //IO密集型任务
        //int poolSize = Runtime.getRuntime().availableProcessors() * 2;
        int poolSize = 8;
        ExecutorService executorService = new ThreadPoolExecutor(poolSize, poolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(50000));

        final List<SchoolManageBean> schoolList = checkInfoService.getAllSchoolList();
        //默认分片大小
        int size = 30;
        List<List<SchoolManageBean>> partitionSchList = ListUtils.partition(schoolList, size);

        AtomicInteger count = new AtomicInteger(0);
        for (final List<SchoolManageBean> schools : partitionSchList) {
            Callable<String> callable = () -> {
                for (SchoolManageBean sch : schools) {
                    List<Student> stuList = null;
                    try {
                        stuList = checkInfoService.queryStudentBySchIdAnySource(sch.getSchId());
                    } catch (Exception e) {
                        logger.error("学校[" + sch.getSchName() + "-" + sch.getFullCode() + "]查询学生信息异常");
                        logger.error(e.getMessage(), e);
                        continue;
                    }
                    if (CollectionUtils.isEmpty(stuList)) {
                        logger.info("学校[" + sch.getSchName() + "-" + sch.getFullCode() + "]无学生");
                        continue;
                    }

                    for (Student stu : stuList) {
                        if (Student.SOURCE_SAAS.equals(stu.getStuSourceid()) || Student.SOURCE_DOCTOR.equals(stu.getStuSourceid()) ||
                        Student.SOURCE_EXCEL.equals(stu.getStuSourceid())) { //如果数据来源是信息系统 或者 未建档
                            Student child;
                            if (StringUtils.isBlank(stu.getStuNo())) {
                                child = saasInfoService.getStuInfoByCid(stu.getCid());
                            } else {
                                child = saasInfoService.getStuInfoByFchildno(stu.getStuNo());
                            }
                            if (child != null) {
                                if ("2".equals(stu.getStuIsjz())) {
                                    stu.setStuNo(child.getStuNo());
                                    stu.setStuImuno(child.getStuImuno());
                                }
                                String  firstTime  = stu.getFirstStuSmrq();
                                 stu.setFirstStuSmrq(firstTime);
                                //equalsChildinfoScanCode(stu, child, stuMsMap);
                                if(stu!=null) {
                                    stu.setFchildno(child.getStuNo());
                                    stu.setStuNo(child.getStuNo());
                                    stu.setStuName(child.getStuName());
                                    stu.setStuBirth(DateUtil.formatShort(child.getStuBirth()));
                                    stu.setStuImuno(child.getStuImuno());
                                    stu.setStuSex(child.getStuSex());
                                    stu.setStuPhone(child.getStuPhone());
                                    stu.setStuFathername(child.getStuFathername());
                                    stu.setStuFatherPhone(child.getStuFatherPhone());
                                    stu.setStuMothername(child.getStuMothername());
                                    stu.setStuMotherPhone(child.getStuMotherPhone());
                                    stu.setStuType(child.getStuType());
                                    stu.setStuBelongStationcode(child.getStuBelongStationcode());
                                    stu.setCid(child.getCid());
                                    // 未建档学生查验不需要更新补证状态
                                    if (!"2".equals(child.getStuIsjz()) && StringUtils.isNotEmpty(stu.getStuImuno())) {
                                        stu.setStuIsbjzz("0");
                                    }
                                    stu.setFResideAddr(child.getFResideAddr());
                                }
                                if (eachYear) {
                                    clearState(stu); //清空所有state, stateAfter
                                    resetJzzState(stu); //扫码录入的重置接种证状态
                                }
                                checkInfoService.checkChildInfo(stu);
                                count.incrementAndGet();
                            } else {
                                logger.info("saas库未找到[" +sch.getSchName() + "-" + sch.getFullCode() + " : " + stu.getStuName() + "-" + stu.getStuNo() + "-" + stu.getStuImuno() + "]学生相关信息");
                            }
                        } else { //如果数据来源是校医录入、excel导入等途径
                            if (eachYear) {
                                clearState(stu);
                                resetJzzState4NoSaasData(stu); //非扫码录入的重置接种证状态
                            }
                            checkInfoService.checkChildInfo(stu);
                            count.incrementAndGet();
                        }
                    }

                }
                return Thread.currentThread().getName();
            };
            FutureTask<String> futureTask = new FutureTask<>(callable);
            futureTasks.add(futureTask);
            executorService.submit(futureTask);
        }

        for (FutureTask<String> futureTask : futureTasks) {
            try {
                String completeThread = futureTask.get();
                logger.info("完成任务: " + completeThread);
            } catch (InterruptedException | ExecutionException e) {
                logger.error(e.getMessage(), e);
            }
        }

        //清理线程池
        executorService.shutdown();
        long end = System.currentTimeMillis();
        logger.info("使用时间：" + (end - start) / 1000.0 + "秒");
        return count.get();
    }


    private boolean equalsChildinfoScanCode(Student stu, Student child,
                                            Map<String, Method> stuMsMap) {
        boolean result = true;
        String childBxxxValue = null;
        String stuBxxxValue = null;
        String methodGetChildName = null;
        String methodSetStuName = null;
        for (int i = 0; i < Util.AtNightUpdateStudent.size(); i++) {
            UpdateStudentProperties updateStudentPropertie = Util.AtNightUpdateStudent.get(i);
            if (updateStudentPropertie.getType() != 1) {
                methodGetChildName = "get" + updateStudentPropertie.getPropertiesName().substring(0, 1).toUpperCase() + updateStudentPropertie.getPropertiesName().substring(1);
                methodSetStuName = "set" + updateStudentPropertie.getPropertiesName().substring(0, 1).toUpperCase() + updateStudentPropertie.getPropertiesName().substring(1);
                try {
                    childBxxxValue = (String) stuMsMap.get(methodGetChildName).invoke(child);
                    stuBxxxValue = (String) stuMsMap.get(methodGetChildName).invoke(stu);

                    childBxxxValue = childBxxxValue == null ? "" : childBxxxValue.trim();
                    stuBxxxValue = stuBxxxValue == null ? "" : stuBxxxValue.trim();


                    if (!"".equals(childBxxxValue) || !"".equals(stuBxxxValue)) {//排除都没有值的情况
                        if ("".equals(childBxxxValue) || "".equals(stuBxxxValue)) {//有一个是空值
                            if ("".equals(childBxxxValue)) {
                                stuMsMap.get(methodSetStuName).invoke(stu, "");
                            } else {
                                String insertValue = null;
                                try {
                                    insertValue = childBxxxValue;
                                    if (updateStudentPropertie.getType() == 2) {
                                        insertValue = Util.getFixedDate(Util.getDateByString(childBxxxValue, "yyyy-MM-dd"), "yyyy-MM-dd");
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    continue;
                                }
                                stuMsMap.get(methodSetStuName).invoke(stu, insertValue);
                            }
                            result = false;
                        } else {//都不是空
                            String stuCompareValue = null;
                            String childCompareValue = null;
                            try {
                                stuCompareValue = stuBxxxValue;
                                childCompareValue = childBxxxValue;
                                if (updateStudentPropertie.getType() == 2) {
                                    stuCompareValue = Util.getFixedDate(Util.getDateByString(stuBxxxValue, "yyyy-MM-dd"), "yyyy-MM-dd");
                                    childCompareValue = Util.getFixedDate(Util.getDateByString(childBxxxValue, "yyyy-MM-dd"), "yyyy-MM-dd");
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                                continue;
                            }
                            if (!stuCompareValue.equals(childCompareValue)) {
                                stuMsMap.get(methodSetStuName).invoke(stu, childCompareValue);
                                result = false;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return result;
    }


    /**
     * 重置接种证状态
     * @param stu
     */
    public void resetJzzState(Student stu) {
        if (StringUtils.isNotBlank(stu.getStuImuno())) {
            stu.setStuIsjzz("0");
            stu.setStuIsbjzz("1");
        } else {
            stu.setStuIsjzz("1");
            stu.setStuIsbjzz("1");
        }
    }

    /**
     * 非扫码录入的重置接种证状态
     * @param stu
     */
    public void resetJzzState4NoSaasData(Student stu) {
        if ("0".equals(stu.getStuIsjzz()) || "0".equals(stu.getStuIsbjzz())) { //去年有证或完成补证
            stu.setStuIsjzz("0");
            stu.setStuIsbjzz("1");
        } else {
            if (StringUtils.isBlank(stu.getStuIsjzz())) {
                if (StringUtils.isNotBlank(stu.getStuImuno())) {
                    stu.setStuIsjzz("0");
                    stu.setStuIsbjzz("1");
                } else {
                    stu.setStuIsjzz("1");
                    stu.setStuIsbjzz("1");
                }
            }
        }
    }

    /**
     * 清空之前的状态值
     * @param stu
     */
    public void clearState(Student stu) {
        stu.setStuIsjz(null);
        stu.setStuIsfull(null);
        //乙肝疫苗
        stu.setB063State(null);
        stu.setB064State(null);
        stu.setB065State(null);
        stu.setB063StateAfter(null);
        stu.setB064StateAfter(null);
        stu.setB065StateAfter(null);
        //卡介苗
        stu.setB001State(null);
        stu.setB001StateAfter(null);
        //脊灰疫苗
        stu.setB009State(null);
        stu.setB010State(null);
        stu.setB011State(null);
        stu.setB012State(null);
        stu.setB009StateAfter(null);
        stu.setB010StateAfter(null);
        stu.setB011StateAfter(null);
        stu.setB012StateAfter(null);
        //百白破疫苗
        stu.setB015State(null);
        stu.setB016State(null);
        stu.setB017State(null);
        stu.setB018State(null);
        stu.setB015StateAfter(null);
        stu.setB016StateAfter(null);
        stu.setB017StateAfter(null);
        stu.setB018StateAfter(null);
        //白破疫苗
        stu.setB037State(null);
        stu.setB037StateAfter(null);
        //麻疹类疫苗
        stu.setB059State(null);
        stu.setB060State(null);
        stu.setB059StateAfter(null);
        stu.setB060StateAfter(null);
        //A群流脑疫苗
        stu.setB040State(null);
        stu.setB041State(null);
        stu.setB040StateAfter(null);
        stu.setB041StateAfter(null);
        //A+C群流脑疫苗
        stu.setB045State(null);
        stu.setB046State(null);
        stu.setB045StateAfter(null);
        stu.setB046StateAfter(null);
        //乙脑疫苗
        stu.setB032State(null);
        stu.setB033State(null);
        stu.setB032StateAfter(null);
        stu.setB033StateAfter(null);
        //甲肝疫苗
        stu.setB073State(null);
        stu.setB073StateAfter(null);

        stu.setB074State(null);
        stu.setB074StateAfter(null);
    }

}
