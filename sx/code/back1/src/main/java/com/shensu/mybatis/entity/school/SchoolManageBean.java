package com.shensu.mybatis.entity.school;

import lombok.Data;

/**
 * @author zcy
 * @ClassName SchoolManageBean
 * @Description 学校管理功能对应的学校类
 * @createTime 2022/8/19
 */
@Data
public class SchoolManageBean {
    /**
     * 主键
     */
    private String schId;
    /**
     * 学校编码  （3位，同一接种点不能重复）
     */
    private String schCode;
    /**
     * 学校名称
     */
    private String schName;
    /**
     * 密码
     */
    private String schPwd;
    /**
     * 学校类型  0：幼托机构、1：小学
     */
    private String schType;
    /**
     * 地址
     */
    private String schAddress;
    /**
     * 电话
     */
    private String schPhone;
    /**
     * 联系人
     */
    private String schLinkMan;
    /**
     * 接种点编码
     */
    private String schStationCode;
    /**
     * 接种点id
     */
    private String schStationId;
    /**
     * 授权状态 （登录失败次数过多）
     */
    private String schIsAccess;
    /**
     * 修改时间
     */
    private String schUpdateTime;
    /**
     * 编码  10位接种点编码+3位sch_code
     */
    private String fullCode;
    /**
     * 接种点名称
     */
    private String schStationName;
    /**
     * 登录失败次数
     */
    private String invalidCount;
    /**
     * 最后登录失败时间
     */
    private String invalidLastTime;
    /**
     * 账号状态 1：禁用 标记删除
     */
    private String invalidStatus;

    private String supCode;

    private String supName;
}
