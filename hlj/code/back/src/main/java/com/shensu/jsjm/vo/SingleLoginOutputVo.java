package com.shensu.jsjm.vo;

import io.swagger.annotations.ApiModelProperty;

public class SingleLoginOutputVo {

    @ApiModelProperty(value = "状态 0成功 1失败")
    private String code;

    @ApiModelProperty(value = "失败提示")
    private String msg;

    @ApiModelProperty(value = "地区编码层级 1省级 2市级")
    private String level;

    @ApiModelProperty(value = "地区编码")
    private String areaCode;

    @ApiModelProperty(value = "地区名称")
    private String areaName;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}
