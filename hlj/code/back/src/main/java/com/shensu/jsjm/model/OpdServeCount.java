package com.shensu.jsjm.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * 1222
 */
public class OpdServeCount {

    @ApiModelProperty(value = "当前预检人数")
    private int preExamNum;
    @ApiModelProperty(value = "当前登记人数")
    private int regNum;
    @ApiModelProperty(value = "当前接种人数")
    private int inocNum;
    @ApiModelProperty(value = "完成留观人数")
    private int completeNum;

    public int getPreExamNum() {
        return preExamNum;
    }

    public void setPreExamNum(int preExamNum) {
        this.preExamNum = preExamNum;
    }

    public int getRegNum() {
        return regNum;
    }

    public void setRegNum(int regNum) {
        this.regNum = regNum;
    }

    public int getInocNum() {
        return inocNum;
    }

    public void setInocNum(int inocNum) {
        this.inocNum = inocNum;
    }

    public int getCompleteNum() {
        return completeNum;
    }

    public void setCompleteNum(int completeNum) {
        this.completeNum = completeNum;
    }
}
