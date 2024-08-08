package com.shensu.jsjm.service;

import com.shensu.jsjm.model.OpdServeCount;
import com.shensu.jsjm.model.VaccineType;
import com.shensu.jsjm.vo.*;

/**
 * @author Dell
 */
public interface CommonService {

    AreaOutputVo getArea(AreaInputVo inputVo);

    TotalOutputVo getTotal(TotalInputVo inputVO);


    void neonateYearCount();

    void neonateMonthCount();

    VaccineTypeOutputVo getVaccineType();

    AgesOutputVo getAges();

}
