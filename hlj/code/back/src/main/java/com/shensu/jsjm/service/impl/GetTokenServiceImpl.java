package com.shensu.jsjm.service.impl;

import com.shensu.jsjm.constant.RedisKeyEnum;
import com.shensu.jsjm.service.GetTokenService;
import com.shensu.jsjm.service.RedisService;
import com.shensu.jsjm.util.HttpUtil;
import com.shensu.jsjm.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GetTokenServiceImpl implements GetTokenService {

    private static final Logger log = LoggerFactory.getLogger(GetTokenServiceImpl.class);

    @Autowired
    private RedisService redisService;
    @Resource
    private PropertyUtil propertyUtil;

    @Override
    public void setAuthToken(String key) {
        String userName = "";
        String password = "";

        if (RedisKeyEnum.auth_token_user_get.getKey().equals(key)) {
            userName = propertyUtil.getTokenUsernameGet();
            password = propertyUtil.getTokenPasswordGet();
        } else {
            userName = propertyUtil.getTokenUsernamePost();
            password = propertyUtil.getTokenPasswordPost();
        }

        String postStr = "username=" + userName + "&password=" + password;
        String urlstr = propertyUtil.getTokenUrl();
        String token = HttpUtil.postHttpToken(postStr, urlstr, propertyUtil.getTokenHearAuthoriKey(), propertyUtil.getTokenHearAuthoriValue());
        if (token == null) {
            log.error("getPersonNumUrl get token error");
        }

        redisService.set(key,token);
    }

}
