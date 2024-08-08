package com.shensu.jsjm.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
//@ConfigurationProperties(prefix = "page")
//@PropertySource({"classpath:application-dev.properties"})
public class PropertyUtil {

    @Value("${oauth.pwd}")
    private String oauthPwd;

    @Value("${oauth.account}")
    private String oauthAccount;

    @Value("${oauth.smKey}")
    private String oauthSmKey;

    @Value("${oauth.public.rsaKey}")
    private String rsaKey;

    @Value("${interface.post.authUrl}")
    private String authUrl;

    @Value("${interface.post.opdServeCountUrl}")
    private String opdServeCountUrl;

    @Value("${interface.post.inventoryUrl}")
    private String inventoryUrl;

    @Value("${interface.post.deviceUrl}")
    private String deviceUrl;

    @Value("${access.token.url}")
    private String accessTokenUrl;

    @Value("${access.user.url}")
    private String accessUserUrl;

    @Value("${singleLogin2}")
    private String singleLogin2;

    @Value("${interface.get.personFormUrl}")
    private String personFormUrl;

    @Value("${interface.get.personNumUrl}")
    private String personNumUrl;

    @Value("${interface.post.tokenUrl}")
    private String tokenUrl;

    @Value("${token.body.username_get}")
    private String tokenUsernameGet;

    @Value("${token.body.password_get}")
    private String tokenPasswordGet;

    @Value("${token.body.username_post}")
    private String tokenUsernamePost;

    @Value("${token.body.password_post}")
    private String tokenPasswordPost;

    @Value("${token.header.authori.key}")
    private String tokenHearAuthoriKey;

    @Value("${token.header.authori.value}")
    private String tokenHearAuthoriValue;

    @Value("${interface.post.wisdomInventoryUrl}")
    private String wisDomInventoryUrl;
}
