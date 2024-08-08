package com.shensu.jsjm.controller;

import com.shensu.jsjm.service.CommonService;
import com.shensu.jsjm.service.VaccinationService;
import com.shensu.jsjm.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Dell
 */
@RestController
@Api(tags = "0000.通用接口-地区相关接口")
@RequestMapping("/user")
public class CommonController {
    private final static Logger log = LoggerFactory.getLogger(CommonController.class);

    @Resource
    private CommonService commonService;

    @Resource
    private VaccinationService vaccinationService;

    /**
     * 总人数
     * 地图下方展示全省或者全市，当日实时数据。
     *
     * 查询条件：省编码/地市编码
     * 查询结果：当前预检人数，当前登记人数，当前接种人数，完成留观人数，当日完成人数，当日建档人数，当日接种剂次数，当日异地接种人数，当日变迁人数
     *
     * 数据来源：
     * 当前预检人数，当前登记人数，当前接种人数，完成留观人数，当日完成人数，实时从接口获取（数字化门诊提供）。
     * 当日建档人数，当日接种剂次数，当日异地接种人数，当日变迁人数，由免疫平台统计，表名screen_station_day_new。
     */
    @GetMapping({"/getTotal"})
    @ApiOperation("总人数")
    public TotalOutputVo getTotal(TotalInputVo inputVO) {
        return commonService.getTotal(inputVO);
    }


    @GetMapping({"/getWisdomBirthYear"})
    @ApiOperation("历年出生儿童建档数-年份")
    public WisdomBirthYearOutputVo getWisdomBirthYear(SameDayVaccInputVo inputVO) {
        return vaccinationService.getWisdomBirthYear(inputVO);
    }

    @GetMapping({"/getWisdomBirthMonth"})
    @ApiOperation("历年出生儿童建档数-月份")
    public WisdomBirthMonthOutputVo getWisdomBirthMonth(SameDayVaccInputVo inputVO) {
        return vaccinationService.getWisdomBirthMonth(inputVO);
    }


    /**
     * 年龄组
     */
    @GetMapping({"/getAges"})
    @ApiOperation("年龄组")
    public AgesOutputVo getAges() {
        return commonService.getAges();
    }


}
