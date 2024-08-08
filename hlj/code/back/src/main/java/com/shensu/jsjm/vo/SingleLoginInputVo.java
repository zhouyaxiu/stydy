package com.shensu.jsjm.vo;

import io.swagger.annotations.ApiModelProperty;

public class SingleLoginInputVo {

    @ApiModelProperty(value = "token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
