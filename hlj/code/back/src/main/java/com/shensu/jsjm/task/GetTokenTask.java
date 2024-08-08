package com.shensu.jsjm.task;

import com.shensu.jsjm.constant.RedisKeyEnum;
import com.shensu.jsjm.service.GetTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * 获取token定时任务
 * */


@Component
@EnableScheduling
@EnableAsync
public class GetTokenTask {
    private static final Logger logger = LoggerFactory.getLogger(GetTokenTask.class);

    @Autowired
    private GetTokenService getTokenService;

    /**
     * 获取auth token
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void updateAuthToken() {
        logger.info("====开始更新auth token====");
        getTokenService.setAuthToken(RedisKeyEnum.auth_token_user_get.getKey());
        getTokenService.setAuthToken(RedisKeyEnum.auth_token_user_post.getKey());
        logger.info("【更新auth token结束】");
    }

}
