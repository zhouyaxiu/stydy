package com.shensu.util;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 版权：(C) 版权所有 2019-2022  苏州沈苏自动化有限公司
 * <简述>返回通用消息提示语
 * <详细描述>
 *
 * @author ztt
 * @version $Id$
 * @see
 * @since
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonRespBody {
    /**
     * 是否成功
     */
    private boolean success = false;
    /**
     * 弹出提示信息
     */
    private String popMsg;
    /**
     * 回传详情
     */
    private Object data;

    private Object otherdata;

    private Boolean otherflag;
    /**
     * @Fields code : 返回状态码
     */
    private Integer code;

    /**
     * @Fields errorCode : 业务需要
     */
    private Integer errorCode;



}
