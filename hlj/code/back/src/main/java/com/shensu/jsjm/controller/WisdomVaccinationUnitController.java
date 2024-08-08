package com.shensu.jsjm.controller;

import com.shensu.jsjm.service.CommonService;
import com.shensu.jsjm.service.VaccinationUnitService;
import com.shensu.jsjm.vo.AreaInputVo;
import com.shensu.jsjm.vo.AreaOutputVo;
import com.shensu.jsjm.vo.ClassUnitInputVo;
import com.shensu.jsjm.vo.WisdomClassUnitOutputVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description:todo
 * @author：GJ
 * @date：2023/11/20 10:06
 */
@RestController
@Api(tags = "0000.接种单位相关接口")
@RequestMapping("/vaccinationUnit")
public class WisdomVaccinationUnitController {

    private final static Logger log = LoggerFactory.getLogger(WisdomVaccinationUnitController.class);

    @Resource
    private VaccinationUnitService vaccinationUnitService;

    @Resource
    private CommonService commonService;
    /**
     * 接种单位分类
     * 接种单位类型参考：常规接种单位、产科接种单位、成人接种单位、狂犬病暴露处置门诊
     */
    @GetMapping({"/getClassification"})
    @ApiOperation("接种单位分类")
    public WisdomClassUnitOutputVo getClassification(ClassUnitInputVo inputVO) {
        return vaccinationUnitService.getWidomClassification(inputVO);
    }

    /**
     * 获取所有地区
     */
    @GetMapping({"/getArea"})
    @ApiOperation("获取所有地区")
    public AreaOutputVo getArea(AreaInputVo inputVo) {
        return commonService.getArea(inputVo);
    }


}
