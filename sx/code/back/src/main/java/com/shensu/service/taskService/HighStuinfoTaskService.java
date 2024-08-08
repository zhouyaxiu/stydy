package com.shensu.service.taskService;


import com.shensu.mybatis.entity.check.*;
import com.shensu.mybatis.entity.highStudent.HighStudCheck;
import com.shensu.mybatis.entity.highStudent.HighStudent;
import com.shensu.mybatis.entity.nurClass.ClassInfo;
import com.shensu.mybatis.entity.round.Round;
import com.shensu.mybatis.entity.school.SchoolManageBean;
import com.shensu.mybatis.entity.studentInput.ExcelImportResult;
import com.shensu.mybatis.entity.systemmanage.VSysStation;
import com.shensu.mybatis.mapper.check.SaasDataMapper;
import com.shensu.mybatis.mapper.highStu.HighStuMapper;
import com.shensu.mybatis.mapper.nurClass.NurClassMapper;
import com.shensu.mybatis.mapper.student.ExcelImportResutlMapper;
import com.shensu.mybatis.mapper.student.StudentInputMapper;
import com.shensu.service.check.SaasInfoService;
import com.shensu.service.roundService.RoundService;
import com.shensu.service.scanCodeService.ScanCodeService;
import com.shensu.service.school.SchoolManageService;
import com.shensu.util.CheckFunction;
import com.shensu.util.DateUtil;
import com.shensu.util.DateUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author  ztt
 * */

@Service
public class HighStuinfoTaskService {
    @Resource
   private RoundService  roundService;
    @Autowired
    private SaasDataMapper saasDataMapper;
    @Resource
    private HighStuMapper highStuMapper;

    @Autowired
    private NurClassMapper classMapper;

    @Autowired
    private ExcelImportResutlMapper importResutlMapper;

    @Autowired
    private SaasInfoService saasInfoService;

    @Resource
    private SchoolManageService  schoolManageService;

    @Resource
    private StudentInputMapper studentInputMapper;
    @Resource
    private ScanCodeService  scanCodeService;


    private static final Logger logger = LoggerFactory.getLogger(HighStuinfoTaskService.class);

    public void  checkHighStudent(String state) throws Exception {

        long start = System.currentTimeMillis();
        List<FutureTask<String>> futureTasks = new ArrayList<>();
        //IO密集型任务
        //int poolSize = Runtime.getRuntime().availableProcessors() * 2;
        int poolSize = 8;
        ExecutorService executorService = new ThreadPoolExecutor(poolSize, poolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(50000));
        //查询到所有的在查验时间内的轮次信息
        final List<Round> list  = roundService.findAllRounds(state);        //默认分片大小
        int size = 30;
        List<List<Round>> partitionRoundList = ListUtils.partition(list, size);
        AtomicInteger count = new AtomicInteger(0);
        //一个批次一个批次的查询
        for(final List<Round> rounds : partitionRoundList){
            Callable<String> callable = () -> {
                for (Round round : rounds) {
                    List<HighStudent> listCheckStu = null;
                    try {
                        listCheckStu =   highStuMapper.findAllCheckStuds(round.getRoundId());
                    } catch (Exception e) {
                    }
                    if (CollectionUtils.isEmpty(listCheckStu)) {
                        continue;
                    }
                    for(int j=0;j<listCheckStu.size();j++){

                        HighStudent highStudent = listCheckStu.get(j);
                        HighStudCheck stuCheck  = new HighStudCheck();
                        //将每个学生的查验结果进行保存
                        if("麻腮风疫苗".equals(round.getRoundVaccName())){
                            CheckFunction.checkMaZhen(highStudent,stuCheck,round.getStartTime(),round.getEndTime());
                        }else if("水痘疫苗".equals(round.getRoundVaccName())){
                            CheckFunction.shuiDou(highStudent,stuCheck);
                        }else if("流感疫苗".equals(round.getRoundVaccName())){
                            CheckFunction.liuGan(highStudent,stuCheck);
                        }
                        highStuMapper.updateCheckResult(stuCheck,highStudent.getStuNo(),round.getRoundId());
                        count.incrementAndGet();
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

    }

    public void updateState() {
        List<Round> roundList = roundService.findAllRounds("0");
        roundList.forEach(round->{
            if(!DateUtils.isEffectiveDate(DateUtils.getNowDate(),DateUtils.toDate(round.getStartTime()),DateUtils.toDate(round.getEndTime()))){
                logger.info("开始更新不需要参与查验的轮次: " + round.getRoundName());
                roundService.updateRound(round.getRoundId());
            }
        });
    }


    public void claLevelUp() {
        // 备份表
        String bakTableName = LocalDate.now().toString().replaceAll("-", "");
        classMapper.backUpClass(bakTableName);
        classMapper.backUpStu(bakTableName);
        // 删除毕业班及毕业班学生
        List<String> finishCla = classMapper.getFinishCla();
        highStuMapper.deleteStuByClaIds(finishCla);
        classMapper.deleteClassByIds(finishCla);
        // 更新年级
        classMapper.batchUpdateClassLevel();
        // 更新班级名称
        classMapper.batchUpdateClassName();
    }

    public void updateCheckAgain() {
        List<ExcelImportResult> allStuAgain = importResutlMapper.getAllStuAgain();
        List<HighStudent> list = new ArrayList<>();
        List<String> cids = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        Map<String, Integer> totalMap = new HashMap<>();
        for (ExcelImportResult bean : allStuAgain) {
            String cid = bean.getCid();
            List<InfoBean> infoByCid = saasInfoService.getInfoByCid(cid);
            if ( infoByCid.isEmpty()) continue;
            if ( infoByCid.size() != 1) continue;
            HighStudent stu = highStuMapper.getStuByCid(cid);
            if (null != stu) {
                cids.add(cid);
                continue;
            }
            // 判断班级人数是否达到上限
            String claId = bean.getClaId();
            if (!totalMap.containsKey(claId)) {
                ClassInfo classById = classMapper.getClassById(claId);
                totalMap.put(claId,Integer.parseInt(classById.getClaTotal()));
            }
            if (!map.containsKey(claId)) {
                int num = classMapper.getStudentNumByClassId(claId);
                map.put(claId,num);
            }
            Integer stuNum = map.get(claId);
            if (totalMap.get(claId) <  stuNum+1) {
                cids.add(cid);
                continue;
            }
            HighStudent highStudent = saasInfoService.sassBeanToStudent(infoByCid.get(0));
            highStudent.setSchid(bean.getSchId());
            highStudent.setStuClacode(bean.getClaId());
            highStudent.setStuStationcode(bean.getStationCode());
            list.add(highStudent);
            cids.add(cid);
            map.put(claId,stuNum+1);
        }
        // 新增学生
        if (!list.isEmpty()) {
            // 批量插入学生
            highStuMapper.batchInsertStu(list);
        }
        // 删除匹配成功的身份证号
        if (!cids.isEmpty()) {
            importResutlMapper.delAgainByCids(cids);
        }
    }

    public void upDateHighInfo() {
        long start = System.currentTimeMillis();
        List<FutureTask<String>> futureTasks = new ArrayList<>();
        //IO密集型任务
        //int poolSize = Runtime.getRuntime().availableProcessors() * 2;
        int poolSize = 8;
        ExecutorService executorService = new ThreadPoolExecutor(poolSize, poolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(50000));
        //查询到所有的在查验时间内的轮次信息
        final List<SchoolManageBean> list  = schoolManageService.findAllSchools();        //默认分片大小
        int size = 30;
        List<List<SchoolManageBean>> partitionSchList = ListUtils.partition(list, size);
        AtomicInteger count = new AtomicInteger(0);
        //一个批次一个批次的查询
        for(final List<SchoolManageBean> schools : partitionSchList){
            Callable<String> callable = () -> {
                for (SchoolManageBean school : schools) {
                    List<StudentInfo> listCheckStu = null;
                    try {
                        listCheckStu =   studentInputMapper.findStuInfoByNos(school.getSchId());
                    } catch (Exception e) {
                    }
                    if (CollectionUtils.isEmpty(listCheckStu)) {
                        continue;
                    }
                    for(int j=0;j<listCheckStu.size();j++){
                        //数据库原有信息
                        StudentInfo oldStudent = listCheckStu.get(j);
                        //新的查验内容
                        List<ScanInfoBean> infoBeanList = saasDataMapper.queryInfoByFchildno(oldStudent.getStuNo());
                        if(infoBeanList!=null && infoBeanList.size()==1){
                            //将这个学生进行查验后结果返回
                            ScanInfoBean infoBean = infoBeanList.get(0);
                            //查验前的学生
                            StudentInfo stuInfo = scanCodeService.sassBeanToStuInfo(infoBean);
                            //直接插入提示保存成功
                            //查询该学生是否在其他学校中存储，如果在其他学校中，则继续添加这个学生，但是需要给用户提示信息
                            List<JzjlBean> jzjlList = saasDataMapper.queryJzjlByFchildnoOrdered(stuInfo.getStuNo());
                            stuInfo = scanCodeService.initBactInfo(stuInfo, jzjlList);
                            stuInfo.setStuStationcode(oldStudent.getStuStationcode());
                            stuInfo.setStuStreetcode(oldStudent.getStuStreetcode());
                            stuInfo.setStuCountycode(oldStudent.getStuCountycode());
                            stuInfo.setStuCitycode(oldStudent.getStuCitycode());
                            stuInfo.setRowkey(oldStudent.getRowkey());
                            stuInfo.setSchid(oldStudent.getSchid());
                            stuInfo.setClassId(oldStudent.getClassId());
                            studentInputMapper.updateStuInfo(stuInfo);
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
    }
}
