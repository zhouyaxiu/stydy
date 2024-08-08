package com.shensu.jsjm.vo;

import com.shensu.jsjm.model.ScanVaccinationRate;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ScanVaccinationRateOutputVo {

    @ApiModelProperty(value = "扫码接种率集合")
    private List<ScanVaccinationRate> scanVaccinationRates;

    public List<ScanVaccinationRate> getScanVaccinationRates() {
        return scanVaccinationRates;
    }

    public void setScanVaccinationRates(List<ScanVaccinationRate> scanVaccinationRates) {
        this.scanVaccinationRates = scanVaccinationRates;
    }
}
