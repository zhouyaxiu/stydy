package com.shensu.service.taskService;


import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.PageHelper;
import com.shensu.mybatis.entity.check.Student;
import com.shensu.mybatis.entity.notice.NoticeMess;
import com.shensu.mybatis.entity.school.SchUserBean;
import com.shensu.mybatis.entity.school.SchoolManageBean;
import com.shensu.mybatis.entity.systemmanage.SysArea;
import com.shensu.mybatis.entity.systemmanage.SysStation;
import com.shensu.mybatis.mapper.noticeMapper.NoticeMessMapper;
import com.shensu.mybatis.mapper.nurClass.NurClassMapper;
import com.shensu.mybatis.mapper.school.SchUserMapper;
import com.shensu.mybatis.mapper.school.SchoolMapper;
import com.shensu.mybatis.mapper.student.StudentInputMapper;
import com.shensu.mybatis.mapper.systemmanage.SysAreaMapper;
import com.shensu.mybatis.mapper.taskMapper.TaskMapper;
import com.shensu.task.DataTask;
import com.shensu.util.*;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 定时同步
 * */
@Service
public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Value("${downPath}")
    private String path;

    @Resource
    private TaskMapper taskMapper;
    @Resource
    private SchoolMapper schoolMapper;

    @Resource
    private NoticeMessMapper noticeMessMapper;

    @Resource
    private SchUserMapper schUserMapper;
    @Resource
    private SysAreaMapper areaMapper;



    @Resource
    private StudentInputMapper  studentInputMapper;
    //删除前一天的学生备份表
    public  void   truncatStuInfoBak(String tableName){
        taskMapper.truncatStuInfoBak(tableName);
    }
    //备份学生信息表
    public  void   backupStuInfon(){
        taskMapper.insertBakStuInfo();
    }
    //备份通知表
    public  void   backupNotice(){
        taskMapper.insertBakNotice();
    }

    //查询宁波表中的学生信息
    public List<Student> findStusBySchId(String schId){
          return  studentInputMapper.findStudentBySchId(schId);
    }

    public long   findStuCountBySchId(String schId){
        return  studentInputMapper.findStudentCountBySchId(schId);
    }

    public List<SchoolManageBean> findSchByAreaCode(){
        List<SchoolManageBean> schList  = schoolMapper.findAllSchoolByAreaCode("330600000000");
        return  schList;
    }

    public List<SchoolManageBean> findDownLoadSchools() throws  Exception{
        JSONObject obj = new JSONObject();
        obj.put("appKey", "appKey003");
        obj.put("secret", "cb0ca1baca9efff2b4a6f8a6337182de");
        obj.put("areaCode", "330600000000");
        JSONObject result = HttpUtils.sendGet(path + "/area/findAllSchool?params=" + Sm4Util.encryptParams(obj.toString()));
        List<SchoolManageBean> schList= JSONObject.parseArray(Sm4Util.decryptEcb(result.getString("data")), SchoolManageBean.class);
        return  schList;
    }


    public SysStation findStationMess(String areaCode) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("appKey", "appKey003");
            obj.put("secret", "cb0ca1baca9efff2b4a6f8a6337182de");
            obj.put("areaCode", areaCode);
            JSONObject result = HttpUtils.sendGet(path + "/area/findSassStation?params=" + Sm4Util.encryptParams(obj.toString()));
            SysStation station = JSONObject.parseObject(Sm4Util.decryptEcb(result.getString("data")), SysStation.class);
            return  station;
        }catch (Exception e){
            e.printStackTrace();
        }
      return  null;
    }

    public List<SchoolManageBean> findBelongStationMess(String areaCode ) throws  Exception{
        JSONObject obj = new JSONObject();
        obj.put("appKey", "appKey003");
        obj.put("secret", "cb0ca1baca9efff2b4a6f8a6337182de");
        obj.put("areaCode", areaCode);
        JSONObject result = HttpUtils.sendGet(path + "/area/findAllSchool?params=" + Sm4Util.encryptParams(obj.toString()));
        List<SchoolManageBean> schList= JSONObject.parseArray(Sm4Util.decryptEcb(result.getString("data")), SchoolManageBean.class);
        return  schList;
    }


    /**
     * 调用接口，获取当前时间前一天有变动的学校
     * */
    public List<SchoolManageBean> findSchByAreaCodeOrTime(String areaCode,String date) throws Exception {
        List<SchoolManageBean> schList = new ArrayList<>();
        // 设置参数
        JSONObject map = new JSONObject();
        map.put("areaCode",areaCode);
        map.put("time",date);
        map.put("appKey","appKey003");
        map.put("secret","cb0ca1baca9efff2b4a6f8a6337182de");
        String ecb = Sm4Util.encryptParams(map.toString());
        // 调用集成平台接口 验证token
        RestTemplate restTemplate = new RestTemplate();
        // 设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 设置参数
        LinkedMultiValueMap body=new LinkedMultiValueMap();
        body.add("params",ecb);
        // body.add("token",saasToken);
        // 请求体，包括请求数据 body 和 请求头 headers
        HttpEntity httpEntity = new HttpEntity(body,httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(path+"/sch/schoolList", HttpMethod.POST, httpEntity, String.class);
        int codeValue = responseEntity.getStatusCodeValue();
        if (521 == codeValue) {
            logger.error("学校查询接口报错！！！");
            return schList;
        }
        //解析返回的数据
        JSONObject jsTemp = JSONObject.parseObject(responseEntity.getBody());
        JSONArray data = jsTemp.getJSONArray("data");
        schList = JSONArray.parseArray(data.toJSONString(), SchoolManageBean.class);
        return  schList;
    }

    //同步学生信息表
    public void stuInfoTask()  throws Exception {
        //删除备份信息   备份今天内容
        truncatStuInfoBak("nur_stuinfo_bak");
        backupStuInfon();
        truncatStuInfoBak("nur_stuinfo");
        boolean result=true;
        //同步信息:一个学校一个学校的同步数据，失败的批次数据记录基本信息到数据库
        List<SchoolManageBean> schList = findDownLoadSchools();
        JSONObject obj = new JSONObject();
        obj.put("appKey", "appKey003");
        obj.put("secret", "cb0ca1baca9efff2b4a6f8a6337182de");
        JSONObject objStuParam = new JSONObject();
        objStuParam.put("appKey", "appKey003");
        objStuParam.put("secret", "cb0ca1baca9efff2b4a6f8a6337182de");
        if (schList != null && schList.size() > 0) {
            for (int i = 0; i < schList.size(); i++) {
                String schId = schList.get(i).getSchId();
                String stationCode  = schList.get(i).getSchStationCode();
                if (StringUtils.isNotEmpty(schId)) {
                    try {
                        obj.put("schId", schId);
                        //查询每个学校的学生的总数量
                        JSONObject resultStuNum = HttpUtils.sendGet(path + "/stuInfo/findStuNumBySchId?params=" + Sm4Util.encryptParams(obj.toString()));
                        //如果出现了问题记录下问题进行下一个学校学生的数据同步
                        if (resultStuNum.getBooleanValue("success") == false && ! (resultStuNum.get("success")==null) ){
                            result=false;
                            taskMapper.insertStuLogData(schId, stationCode, "同步保存失败" + resultStuNum.getString("popMsg"), "1");
                            continue;
                        }
                        long size = resultStuNum.getLong("data");
                        //每1000条分页一次进行查询
                        for (int j = 1; j <= ((size / Constants.BACKSIZE) + 1); j++) {
                            objStuParam.put("schId", schId);
                            objStuParam.put("pageNum", j);
                            JSONObject resultStus =
                                    HttpUtils.sendGet(path + "/stuInfo/findStuInfoBySchId?params=" + Sm4Util.encryptParams(objStuParam.toString()));
                            Object  oo =resultStus.get("success");
                            System.out.println(oo);
                            if (resultStus.getBooleanValue("success") == false && ! (resultStus.get("success")==null) ) {
                                result=false;
                                taskMapper.insertStuLogData(schId, stationCode, "同步保存失败" + resultStus.getString("popMsg"), "1");
                            } else {
                                List<Student> list = JSONObject.parseArray(Sm4Util.decryptEcb(resultStus.getString("data")), Student.class);
                                //批量插入
                                if (list != null && list.size() > 0) {
                                    studentInputMapper.bachInsertStudents(list);
                                }
                            }
                        }
                    } catch (Exception e) {
                        result=false;
                        logger.warn("批量保存失败");
                        //备份数据：那个学校，哪些学生没有成功
                        taskMapper.insertStuLogData(schId, "", "同步保存失败" + e.getCause(), "1");
                    }
                }
            }
            if(result) {
                taskMapper.insertStuLogData("", "330600000000", "保存成功", "0");
            }

        }
    }

    public void noticeTask() throws Exception {
        boolean result =true;
        //截断通知备份表 备份 清空表
        truncatStuInfoBak("nur_notice_bak");   backupNotice();   truncatStuInfoBak("nur_notice");
        //查询指定地区下面学校信息
        List<SchoolManageBean> listSchool = findDownLoadSchools();
        JSONObject obj = new JSONObject();
        obj.put("appKey", "appKey003");
        obj.put("secret", "cb0ca1baca9efff2b4a6f8a6337182de");
        String schId=null;
        try {
        //通过学校备份学校下面的通知信息
            for (int i = 0; i < listSchool.size(); i++) {
            schId= listSchool.get(i).getSchId();
            if (StringUtils.isNotEmpty(schId)) {
                obj.put("schId", schId);
                JSONObject resultNotice = HttpUtils.sendGet(path + "/notice/findNotice?params=" + Sm4Util.encryptParams(obj.toString()));
                //如果出现了问题记录下问题进行下一个学校学生的数据同步
                if (resultNotice.getBooleanValue("success") == false && ! (resultNotice.get("success")==null) ){
                    taskMapper.insertNoticeError(schId, "失败：" +resultNotice.getString("popMsg"),"1" );
                    result=false;
                    continue;
                }
                List<NoticeMess> noticeList  = JSONObject.parseArray(Sm4Util.decryptEcb(resultNotice.getString("data")), NoticeMess.class);
                //同步
                    if (noticeList != null && noticeList.size() > 0) {
                        noticeMessMapper.bachInsertNotice(noticeList);
                    }
                }
                }

        } catch (Exception e) {
                     result=false;
                    e.printStackTrace();
                    //记录错误信息
                    taskMapper.insertNoticeError(schId, "失败：" + e.getMessage(),"1");
        }
        if(result){
            taskMapper.insertNoticeError("", "成功：" ,"1");
        }
    }


    // 同步学校表
    public void SchInfoTask(String areaCode,String date) throws Exception {
        // 第一次同步全部数据，第二次同步增量数据
        int schCount = taskMapper.getSchCount();
        List<SchoolManageBean> schList = null;
        if (schCount == 0) {
            // 同步全部数据
            schList = findSchByAreaCodeOrTime(areaCode,null);
        } else {
            schList = findSchByAreaCodeOrTime(areaCode,date);
            if (!schList.isEmpty()) {
                schoolMapper.delSchoolBySch(schList);
            }
        }
        if (!schList.isEmpty()) {
            schoolMapper.batchInsertSch(schList);
        }
    }
    // 学校表同步到公共卫生平台
    public void SchToHealthPlatform() {
        int schCount = schoolMapper.getSchCount();
        List<SchoolManageBean> schList = null;
        if (schCount == 0) {
            schList = schoolMapper.getSchsForHealthPlatform(null);
        } else {
            String date = DateUtils.getDate();
            schList = schoolMapper.getSchsForHealthPlatform(date);
            schoolMapper.delSchoolBySchForHealthPlatform(schList);
        }

        if (!schList.isEmpty()) {
            schoolMapper.batchInsertSchForHealthPlatform(schList);
        }
    }

    public int StuToHealthPlatform() {
        //删除备份信息
        taskMapper.truncatStuForHealthPlatform("nur_stuinfo_bak");
        //备份今天内容
        taskMapper.insertBakStuForhealthPlatform();
        taskMapper.truncatStuForHealthPlatform("nur_stuinfo");
        //同步信息:一个学校一个学校的同步数据
        long start = System.currentTimeMillis();
        List<FutureTask<String>> futureTasks = new ArrayList<>();
        //IO密集型任务
        //int poolSize = Runtime.getRuntime().availableProcessors() * 2;
        int poolSize = 8;
        ExecutorService executorService = new ThreadPoolExecutor(poolSize, poolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(50000));

        final List<SchoolManageBean> schoolList = schoolMapper.getSchsForHealthPlatform(null);
        //默认分片大小
        int size = 30;
        List<List<SchoolManageBean>> partitionSchList = ListUtils.partition(schoolList, size);

        AtomicInteger count = new AtomicInteger(0);
        for (final List<SchoolManageBean> schools : partitionSchList) {
            Callable<String> callable = () -> {
                for (SchoolManageBean sch : schools) {
                    List<Student> stuList = null;
                    try {
                        stuList = studentInputMapper.queryStudentBySchIdAnySource(sch.getSchId());
                    } catch (Exception e) {
                        logger.error("学校[" + sch.getSchName() + "-" + sch.getFullCode() + "]查询学生信息异常");
                        logger.error(e.getMessage(), e);
                        continue;
                    }
                    if (CollectionUtils.isEmpty(stuList)) {
                        logger.info("学校[" + sch.getSchName() + "-" + sch.getFullCode() + "]无学生");
                        continue;
                    }
                    studentInputMapper.bachInsertHealthPlatformStus(stuList);
                    count.incrementAndGet();

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

    //同步学校用户表
    public void SchUserInfoTask () throws Exception {
        List<SchoolManageBean> schList = schoolMapper.findAllSchool();
        String schIds = schList.stream().map(r -> r.getSchId()).collect(Collectors.joining(","));
        JSONObject map = new JSONObject();
        map.put("schIds",schIds);
        map.put("appKey","appKey003");
        map.put("secret","cb0ca1baca9efff2b4a6f8a6337182de");
        String ecb = Sm4Util.encryptParams(map.toString());
        // 调用集成平台接口 验证token
        RestTemplate restTemplate = new RestTemplate();
        // 设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 设置参数
        LinkedMultiValueMap body=new LinkedMultiValueMap();
        body.add("params",ecb);
        // 请求体，包括请求数据 body 和 请求头 headers
        HttpEntity httpEntity = new HttpEntity(body,httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(path+"/sch/schUserList", HttpMethod.POST, httpEntity, String.class);
        int codeValue = responseEntity.getStatusCodeValue();
        if (521 == codeValue) {
            logger.error("学校查询接口报错！！！");
            return ;
        }
        //解析返回的数据
        JSONObject jsTemp = JSONObject.parseObject(responseEntity.getBody());
        JSONArray data = jsTemp.getJSONArray("data");
        List<SchUserBean> user = JSONArray.parseArray(data.toJSONString(), SchUserBean.class);
        if (!user.isEmpty()) {
            schUserMapper.delSchUserByIds(user);
            schUserMapper.batchInsertSChUSer(user);
        }
    }

    public void areaTask() {
        truncatStuInfoBak("sys_area");
        JSONObject obj = new JSONObject();
        obj.put("appKey", "appKey003");
        obj.put("secret", "cb0ca1baca9efff2b4a6f8a6337182de");
        //查询全部地区
        try {
            JSONObject result =
                    HttpUtils.sendGet(path + "/area/findAllArea?params=" + Sm4Util.encryptParams(obj.toString()));
            String ss = Sm4Util.decryptEcb(result.getString("data"));
            List<SysArea> list = JSONObject.parseArray(ss, SysArea.class);
            //将json转换成对象集合
            if (list != null && list.size() > 0) {
                areaMapper.bashInsertAllArea(list);
                taskMapper.insertAreaLog("0", "地区表同步成功");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            taskMapper.insertAreaLog("1", "地区表同步失败：" + e.getMessage());
        }

    }

    public void stationTask() {
        //查询全部地区
        truncatStuInfoBak("sys_Station");
        JSONObject obj = new JSONObject();
        obj.put("appKey", "appKey003");
        obj.put("secret", "cb0ca1baca9efff2b4a6f8a6337182de");
        try {
            JSONObject result =
                    HttpUtils.sendGet(path + "/area/findAllStation?params=" + Sm4Util.encryptParams(obj.toString()));
            String ss = Sm4Util.decryptEcb(result.getString("data"));
            List<SysStation> list = JSONObject.parseArray(ss, SysStation.class);
            areaMapper.bashInsertAllStation(list);
            taskMapper.insertAreaLog("0", "接种单位表表同步成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            taskMapper.insertAreaLog("1", "地区表同步失败：" + e.getMessage());
        }
    }
}
