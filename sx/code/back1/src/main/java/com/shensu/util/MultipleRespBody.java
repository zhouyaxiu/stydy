package com.shensu.util;


import com.github.pagehelper.PageInfo;

/**
 * 版权：(C) 版权所有 2019-2022  苏州沈苏自动化有限公司
 * <简述>返回通用消息提示语
 * <详细描述>
 *
 * @author gelongyu
 * @version $Id$
 * @see
 * @since
 */
public class MultipleRespBody<T> {
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
    private PageInfo<T> data;

    /**
     * @Fields sec : 操作用时秒数
     */
    private Double sec;

    /**
     * @Fields code : 返回状态码
     */
    private Integer code;

    /**
     * @Fields errorCode : 业务需要
     */
    private Integer errorCode;

    public MultipleRespBody() {
        super();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getPopMsg() {
        return popMsg;
    }

    public void setPopMsg(String popMsg) {
        this.popMsg = popMsg;
    }

    public PageInfo<T> getData() {
        return data;
    }

    public void setData(PageInfo<T> data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public Double getSec() {
        return sec;
    }

    public void setSec(Double sec) {
        this.sec = sec;
    }
}
