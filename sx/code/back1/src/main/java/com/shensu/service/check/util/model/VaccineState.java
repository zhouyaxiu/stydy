package com.shensu.service.check.util.model;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName VaccineState.java
 * @Description TODO
 * @createTime 2022年11月25日 17:22
 */
public class VaccineState {

    private String shotDate;
    private String state;

    public VaccineState() {}

    public VaccineState(String shotDate, String state) {
        this.shotDate = shotDate;
        this.state = state;
    }

    public String getShotDate() {
        return shotDate;
    }

    public void setShotDate(String shotDate) {
        this.shotDate = shotDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
