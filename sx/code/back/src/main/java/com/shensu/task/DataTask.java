package com.shensu.task;

import com.shensu.service.taskService.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * 数据同步
 * */
@EnableScheduling
@Component
public class DataTask {
    private static final Logger logger = LoggerFactory.getLogger(DataTask.class);
    @Resource
    private TaskService  taskService;

    @Scheduled(cron ="0 0 2 * * ?")
    public  void  stuInfoTask(){
        try {
            logger.info("查验库开始执行");
            //同步学生信息表数据
            taskService.stuInfoTask();
            logger.info("查验库结束");
            // 同步学生信息到公共卫生平台
            logger.info("平台库开始执行");
           // taskService.StuToHealthPlatform();
            logger.info("平台库查验库结束");
        }catch (Exception e){
            logger.error(e.getMessage());
            logger.error("查验或者平台批量保存失败");
        }
    }
    //通知模板数据的定时同步
    @Scheduled(cron ="0 0 2 * * ?")
    public  void  noticeTask(){
        try {
            logger.info("通知信息开始执行");
            //同步学生信息表数据
            taskService.noticeTask();
            logger.info("通知信息结束");
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            logger.error("批量保存失败");
        }

    }
    /**
     * 同步学校表，学校用户表
     * */
    @Scheduled(cron ="0 0 1 * * ?")
    public  void  schAndSchUserTask(){
        try {
            logger.info("查验库学校用户开始执行");
            String areaCode = "330600000000";
            // 获取前一天的时间
            LocalDate localDate = LocalDate.now().minusDays(1);
            String date = localDate.toString();
            taskService.SchInfoTask(areaCode,date);
            taskService.SchUserInfoTask();
            logger.info("查验库学校用户结束");
            //同步学校表到公共卫生平台
            logger.info("平台库学校用户开始执行");
            //taskService.SchToHealthPlatform();
            logger.info("平台库学校用户执行结束");
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
    //地区表同步
    @Scheduled(cron =" 0 0 0 1,15 * ?")
    public  void  areaTask(){
        try {
            logger.info("地区表开始执行");
            //同步学生信息表数据
           taskService.areaTask();
            logger.info("地区表结束");
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }

    }
    //同步接种单位表 0 0 */3 * * *
    @Scheduled(cron =" 0 0 0 1,15 * ?")
    public  void  sysStationTask(){
        try {
            logger.info("地区表开始执行");
            //同步学生信息表数据
            taskService.stationTask();
            logger.info("地区表开始执行结束");
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }

    }
}
