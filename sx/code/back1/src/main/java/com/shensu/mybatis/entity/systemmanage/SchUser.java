package com.shensu.mybatis.entity.systemmanage;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zcy
 * @ClassName SchUser
 * @Description
 * @createTime 2023/4/7
 */
@Data
public class SchUser implements Serializable {
    /**
     * 用户主键
     * */
    private String id;
    /**
     * 用户对应的学校主键
     * */
    private String schId;
    /**
     * 账户名
     * */
    private String loginName;
    /**
     * 密码
     * */
    private String password;
    /**
     * 添加时间
     * */
    private Date addTime;
    /**
     * 修改时间
     * */
    private Date updateTime;
    /**
     * 登录失败次数
     * */
    private int invalidCount;
    /**
     * 是否禁用登录  （0禁用、1未禁用）
     * */
    private int isaccess;

    /**
     * 登录次数
     * */
    private int loginCount;
    /**
     * 是否需要强制修改密码  1：需要
     * */
    private String forcePwd;
    /**
     * 修改密码剩余天数
     * */
    private String forceMsg;

    private String schStationCode;

    private String schStationId;

    private String schStationName;

    private String schInvalidStatus;

    private String schType;

    private String schName;
}
