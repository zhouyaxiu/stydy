package com.shensu.jsjm.service.impl;

import com.shensu.jsjm.mapper.CommonMapper;
import com.shensu.jsjm.model.LabelValueBean;
import com.shensu.jsjm.model.SimpleAreaBean;
import com.shensu.jsjm.model.VaccineType;
import com.shensu.jsjm.model.WisdomTodayData;
import com.shensu.jsjm.redis.RedisAreaService;
import com.shensu.jsjm.redis.RedisVaccinationService;
import com.shensu.jsjm.service.CommonService;
import com.shensu.jsjm.util.CommonUtil;
import com.shensu.jsjm.vo.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dell
 */
@Service
public class CommonServiceImpl implements CommonService {

    @Resource
    private RedisAreaService redisAreaService;
    @Resource
    private RedisVaccinationService redisVaccinationService;

    @Resource
    private CommonMapper commonMapper;



    @Override
    public AreaOutputVo getArea(AreaInputVo inputVo) {
        SimpleAreaBean area = redisAreaService.getArea(inputVo);
        AreaOutputVo outputVo = new AreaOutputVo();
        outputVo.setAreaMapCode(area);
        return outputVo;
    }

    @Override
    public TotalOutputVo getTotal(TotalInputVo inputVO) {
        TotalOutputVo outputVo = new TotalOutputVo();
        outputVo.setWisdomTodayData(redisVaccinationService.getWisdomTodayData(inputVO.getAreaCode()));
        return outputVo;
    }

    @Override
    @Async
    public void neonateYearCount() {
        List<String> yearList = new ArrayList<>();
        int currentYear = Year.now().getValue();

        for (int year = currentYear; year >= currentYear - 10; year--) {
            yearList.add(String.valueOf(year));
        }

        commonMapper.deleteNeonateYear();
        for (String yearStr:yearList) {
            commonMapper.saveNeonateYearCount(yearStr);
        }
    }

    @Override
    @Async
    public void neonateMonthCount() {
        int currentYear = YearMonth.now().getYear();
        commonMapper.deleteNeonateMonth();
        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(currentYear, month);
            LocalDate firstDay = yearMonth.atDay(1);
            LocalDate lastDay = yearMonth.atEndOfMonth();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String firstDayStr = firstDay.format(formatter);
            String lastDayStr = lastDay.format(formatter);

            commonMapper.saveNeonateMonthCount(firstDayStr,lastDayStr);
        }
    }


    @Override
    public VaccineTypeOutputVo getVaccineType() {
        List<VaccineType> vaccineTypeList = commonMapper.getVaccineType();
        VaccineTypeOutputVo outputVo = new VaccineTypeOutputVo();
        outputVo.setVaccineTypeList(vaccineTypeList);
        return outputVo;
    }

    @Override
    public AgesOutputVo getAges() {
        AgesOutputVo outputVo = new AgesOutputVo();
        List<LabelValueBean> ageMonthList = CommonUtil.getAgeMonthList();
        outputVo.setAges(ageMonthList);
        return outputVo;
    }

}
