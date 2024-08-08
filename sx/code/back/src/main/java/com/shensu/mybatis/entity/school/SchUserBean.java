package com.shensu.mybatis.entity.school;

import lombok.Data;

/**
 * @author zcy
 * @ClassName SchUserBean
 * @Description
 * @createTime 2023/4/6
 */
@Data
public class SchUserBean {
    private String id;

    private String schId;

    private String loginName;

    private String password;

    private String addTime;

    private String updateTime;

    private String invalidCount;

    private String isaccess;

    private String loginCount;

    private String forcePwd;
}
