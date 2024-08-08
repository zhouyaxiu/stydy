package com.shensu.security.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

/**
 * @author zcy
 * @ClassName MyHashedCredentialsMatcher
 * @Description  免密登录相关
 * @createTime 2023/5/15
 */
public class MyHashedCredentialsMatcher extends HashedCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        NewUserNameToken easyUsernameToken = (NewUserNameToken) token;

        //免密登录,不验证密码
        if (LoginType.NOPASSWD.equals(easyUsernameToken.getType())) {
            return true;
        }

        //密码登录
        String inPassword = new String(easyUsernameToken.getPassword());
        //获得数据库中的密码
        String dbPassword = new String((char[]) info.getCredentials());
        if(StringUtils.isNotEmpty(inPassword) && StringUtils.isNotEmpty(dbPassword)) {
            return this.equals(inPassword, dbPassword);
        }
        return false;
    }

}
