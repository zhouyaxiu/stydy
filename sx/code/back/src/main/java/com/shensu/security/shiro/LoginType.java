package com.shensu.security.shiro;

/**
 * @author zcy
 * @ClassName LoginType
 * @Description
 * @createTime 2023/5/15
 */
public enum LoginType {
    /** 密码登录 */
    PASSWORD("password"),
    /** 密码登录 */
    NOPASSWD("nopassword");
    /** 状态值 */
    private String code;
    private LoginType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
