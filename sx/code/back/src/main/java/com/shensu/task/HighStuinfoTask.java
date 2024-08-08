package com.shensu.task;

import com.shensu.service.taskService.HighStuinfoTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 定时更新查验内容
 * */
@EnableScheduling
@Component
public class HighStuinfoTask {
    private static final Logger logger = LoggerFactory.getLogger(DataTask.class);

    @Resource
    private HighStuinfoTaskService highStuinfoService;
    //同步接种单位表 0 0 */3 * * *
    //@Scheduled(cron ="0 0/30 * * * ?")
    @Scheduled(cron ="0 0 3 * * ?")
    public  void  checkTask(){
        try {
            logger.info("--查验开始");
            //同步学生信息表数据
            highStuinfoService.updateState();
            highStuinfoService.checkHighStudent("0");
            logger.info("---查验结束");
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
    //@Scheduled(cron ="0 0/20 * * * ?")
    @Scheduled(cron ="0 0 1 * * ?")
    public  void  stuinfoTask(){
        try {
            logger.info("学生同步开始");
            //同步学生信息表数据
            highStuinfoService.upDateHighInfo();
            logger.info("---查验结束");
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }

    }
    /**
     *  更新未匹配名单
     * */
    @Scheduled(cron ="0 0 0 * * ?")
    public void updateCheckAgain() {
        try {
            highStuinfoService.updateCheckAgain();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
    /***
     *  年级自动升级
     */
    @Scheduled(cron = "0 0 0 1 9 ?")
    public void claLevelUpgrade() {
        try {
            highStuinfoService.claLevelUp();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
}
