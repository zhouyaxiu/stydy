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

@RestController
@Api(tags = "0001.接种疫苗相关接口")
@RequestMapping("/Vaccination")
public class VaccinationController {

    private final static Logger log = LoggerFactory.getLogger(VaccinationController.class);

    @Resource
    private VaccinationService vaccinationService;

    @Resource
    private CommonService commonService;
    /**
     * 当日接种
     * 数据来源：epi_jzjl_[citycode]
     * 查询结果：地市名称/区县名称、免规疫苗剂次数、非免规疫苗剂次数
     */
    @GetMapping({"/getSameDayVaccination"})
    @ApiOperation("当日接种")
    public SameDayVaccOutputVo getSameDayVaccination(SameDayVaccInputVo inputVO) {
        return vaccinationService.getSameDayVaccination(inputVO);
    }

    /**
     * 疫苗接种率
     * 统计全省或者全市，分年龄组的疫苗接种率。
     * 静态统计，实时查询静态表。
     * 数据来源：统计分析->分年龄组/分地区接种情况统计
     * 查询条件：省编码/地市编码、年龄组（默认：2-3岁）
     * 查询结果：疫苗种类、接种率
     */
    @GetMapping({"/getVaccinationRates"})
    @ApiOperation("疫苗接种率")
    public VaccinationRateOutputVo getVaccinationRates(VaccinationRateInputVo inputVO) {
        return vaccinationService.getVaccinationRates(inputVO);
    }

    @GetMapping({"/getNIPVaccination"})
    @ApiOperation("0-7岁儿童NIP疫苗全程接种率")
    public NIPVaccOutputVo getNIPVaccination(SameDayVaccInputVo inputVO) {
        return vaccinationService.getNIPVaccination(inputVO);
    }

    @GetMapping({"/getVaccinationCounts"})
    @ApiOperation("非免归疫苗统计")
    public VaccinationCountOutputVo getVaccinationCounts(SameDayVaccInputVo inputVO) {
        return vaccinationService.getVaccinationCounts(inputVO);
    }


    @GetMapping({"/getWisdomScanVaccinationRates"})
    @ApiOperation("智慧决策扫码接种率")
    public ScanVaccinationRateOutputVo getWisdomScanVaccinationRates(ScanVaccinationRatesInputVo inputVO) {
        return vaccinationService.getWisdomScanVaccinationRates(inputVO);
    }

    /**
     * 实时库存
     * 统计全省或者全市，疫苗种类的实时库存数。
     * 实时从接口获取（南京技术中心提供）
     * 查询条件：省编码/地市编码、疫苗种类（支持多个）
     * 查询结果：地市名称/区县名称（包括省本级或者市本级）、效期小于1个月的实时库存数、效期为1-3个月的实时库存数、效期大于3个月的实时库存数
     * 横坐标：地区
     * 纵坐标：库存数（单位：万支）
     */
    @GetMapping({"/getInventory"})
    @ApiOperation("实时库存")
    public InventoryOutputVo getInventory(InventoryInputVo inputVO) {
        return vaccinationService.getInventory(inputVO);
    }

    @GetMapping({"/getWisdomInventory"})
    @ApiOperation("近效期库存")
    public WisdomInventoryOutputVo getWisdomInventory(InventoryInputVo inputVO) {
        return vaccinationService.getWisdomInventory(inputVO);
    }

    /**
     * 疫苗种类
     */
    @GetMapping({"/getVaccineType"})
    @ApiOperation("疫苗种类")
    public VaccineTypeOutputVo getVaccineType() {
        return commonService.getVaccineType();
    }
}
