package com.shensu.jsjm.constant;

public enum HttpResultCodeEnum {
    HTTP_SUCCESS("2000","查询成功"),
    HTTP_EXCEPTION("2030", "未知异常"),
    HTTP_PARAMETER_NOT_NULL("2038", "参数不能为空"),
    HTTP_DECRYPT_ERROR("2039", "解密数据失败"),
    HTTP_ENTITY_ERROR("2032", "请求实体错误"),
    HTTP_TOKEN_EXPIRED("2033", "token过期"),
    HTTP_ROLES_NULL("2034", "权限列表为空"),
    HTTP_NO_ROLE("2035", "无权限操作")
    ;

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    HttpResultCodeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
