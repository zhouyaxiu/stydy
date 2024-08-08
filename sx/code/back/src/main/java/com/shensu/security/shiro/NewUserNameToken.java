package com.shensu.security.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author zcy
 * @ClassName NewUserNameToken
 * @Description 免密登录token类
 * @createTime 2023/5/15
 */
public class NewUserNameToken extends UsernamePasswordToken {
    private static final long serialVersionUID = -2564928913725078138L;

    private LoginType type;

    public NewUserNameToken() {
        super();
    }

    /**
     * 免密登录
     */
    public NewUserNameToken(String username) {
        super(username, "", false, null);
        this.type = LoginType.NOPASSWD;
    }

    /**
     * 账号密码登录
     */
    public NewUserNameToken(String username, String password, boolean rememberMe) {
        super(username, password, rememberMe, null);
        this.type = LoginType.PASSWORD;
    }

    public LoginType getType() {
        return type;
    }

    public void setType(LoginType type) {
        this.type = type;
    }

}
