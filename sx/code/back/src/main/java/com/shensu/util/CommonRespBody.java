package com.shensu.util;


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

    public CommonRespBody() {
        super();
    }

    public CommonRespBody(boolean success, String popMsg, Object data) {
        super();
        this.success = success;
        this.popMsg = popMsg;
        this.data = data;
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

    public Object getData() {
//        if (data instanceof List) {
//            return TextTemplateUtil.resetListMapKey((List)data);
//        }
//        if (data instanceof Map) {
//            return TextTemplateUtil.resetMapKey((Map)data);
//        }
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getOtherdata() {
        return otherdata;
    }

    public void setOtherdata(Object otherdata) {
        this.otherdata = otherdata;
    }

    public Boolean getOtherflag() {
        return otherflag;
    }

    public void setOtherflag(Boolean otherflag) {
        this.otherflag = otherflag;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

}
