package com.shensu.jsjm.vo;

import com.shensu.jsjm.model.PersonNum;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class VaccinationPersonOutputVo {

    @ApiModelProperty(value = "地区人数集合")
    private List<PersonNum> personNums;

    @ApiModelProperty(value = "人员总数")
    private int totalPersonNum;

    @ApiModelProperty(value = "疾控机构人员总数")
    private int cdcPersonNum;

    @ApiModelProperty(value = "接种单位人员总数")
    private int opcPersonNum;

    public int getTotalPersonNum() {
        return totalPersonNum;
    }

    public void setTotalPersonNum(int totalPersonNum) {
        this.totalPersonNum = totalPersonNum;
    }

    public int getCdcPersonNum() {
        return cdcPersonNum;
    }

    public void setCdcPersonNum(int cdcPersonNum) {
        this.cdcPersonNum = cdcPersonNum;
    }

    public int getOpcPersonNum() {
        return opcPersonNum;
    }

    public void setOpcPersonNum(int opcPersonNum) {
        this.opcPersonNum = opcPersonNum;
    }

    public List<PersonNum> getPersonNums() {
        return personNums;
    }

    public void setPersonNums(List<PersonNum> personNums) {
        this.personNums = personNums;
    }
}
