package com.shensu.mybatis.entity.systemmanage;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 对应用户管理数据库表字段
 *
 * @author zcy
 */
@Data
public class SysUser implements Serializable {
    /**
     * 对应数据库Id字段
     */
    private String id;
    /**
     * 登陆账号
     */
    private String loginName;
    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户所属地区ID
     */
    private String areaId;
    /**
     * 用户是否可用 1 -- 可用 ； 0 -- 禁用
     */
    private String onOff;
    /**
     * 修改时间
     */
    private String updateDate;
    /**
     * 修改用户
     */
    private String updateUser;

    /**
     * 数据状态
     */
    private String tableStatus;
    /**
     * 用户登陆密码
     */
    private String userPassword;
    /**
     * 系统风格
     */
    private String userStyle;
    /**
     * 登录失败次数
     */
    private  int invalidCount ;
    /**
     * 登录失败最后的时间
     */
     private Date invalidLasttime;
    /**
     * 1：帐号为禁用状态
     */
     private int invalidStatus;
    /**
     * 用户类型：0-管理员，1-普通用户
     */
    private int userType;
    /**
     *  集成平台密码
     * */
    private String jcptPassword;

     private String  cId;

     private String areaCode;

     private String supAreaId;

     private String areaName;

    /**
     * 是否强制修改密码 1：需要
     */
     private String forcePwd;

    /**
     * 是否需要提示  不为空则提示
     */
    private String forceMsg;


}
