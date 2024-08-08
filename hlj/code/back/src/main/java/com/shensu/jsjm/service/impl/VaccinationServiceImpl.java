package com.shensu.jsjm.service.impl;

import com.shensu.jsjm.mapper.CommonMapper;
import com.shensu.jsjm.model.*;
import com.shensu.jsjm.redis.RedisVaccinationService;
import com.shensu.jsjm.service.VaccinationService;
import com.shensu.jsjm.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.shensu.jsjm.constant.Constants.PROVINCE_CODE;

@Service
public class VaccinationServiceImpl implements VaccinationService {

    private final static Logger log = LoggerFactory.getLogger(VaccinationServiceImpl.class);
    @Resource
    private RedisVaccinationService redisVaccinationService;
    @Resource
    private CommonMapper commonMapper;


    /**
     * 当日接种
     */
    @Override
    public SameDayVaccOutputVo getSameDayVaccination(SameDayVaccInputVo inputVO) {
        SameDayVaccOutputVo outputVo = new SameDayVaccOutputVo();
        List<SameDayVacc> sameDayVaccs = redisVaccinationService.getSameDayVaccination(inputVO.getAreaCode(), inputVO.getLevel());
        outputVo.setSameDayVaccs(sameDayVaccs);
//        asyncTask.updateSameDayVaccination(inputVO.getAreaCode(), inputVO.getLevel());
        return outputVo;
    }

    /**
     * 疫苗接种率
     */
    @Override
    public VaccinationRateOutputVo getVaccinationRates(VaccinationRateInputVo inputVO) {
        VaccinationRateOutputVo outputVo = new VaccinationRateOutputVo();
        List<VaccinationRate> vaccinationRates = redisVaccinationService.getVaccinationRate(inputVO.getAreaCode(), inputVO.getAge());
        outputVo.setVaccinationRates(vaccinationRates);
//        asyncTask.updateVaccinationRate(inputVO.getAreaCode(), inputVO.getLevel(), inputVO.getAge());
        return outputVo;
    }



    @Override
    public NIPVaccOutputVo getNIPVaccination(SameDayVaccInputVo inputVO) {
        NIPVaccOutputVo outputVo = new NIPVaccOutputVo();
        List<NIPVacc> nipVaccs = redisVaccinationService.getNIPVaccination(inputVO.getAreaCode(), inputVO.getLevel());
        outputVo.setNIPVaccs(nipVaccs);
        return outputVo;
    }

    @Override
    public VaccinationCountOutputVo getVaccinationCounts(SameDayVaccInputVo inputVO) {
        VaccinationCountOutputVo vaccinationCountOutputVo = new VaccinationCountOutputVo();
        List<VaccinationCount> vaccinationCounts = redisVaccinationService.getVaccinationCounts(inputVO.getAreaCode(), inputVO.getLevel());
        vaccinationCountOutputVo.setVaccinationCounts(vaccinationCounts);
        return vaccinationCountOutputVo;
    }



    @Override
    public WisdomBirthYearOutputVo getWisdomBirthYear(SameDayVaccInputVo inputVO) {
        WisdomBirthYearOutputVo wisdomBirthYearOutputVo = new WisdomBirthYearOutputVo();
        List<WisdomBirthYear> wisdomBirthYearList = commonMapper.getWisdomBirthYear(PROVINCE_CODE.equals(inputVO.getAreaCode())? null : inputVO.getAreaCode());
        wisdomBirthYearOutputVo.setWisdomBirthYearList(wisdomBirthYearList);
        return wisdomBirthYearOutputVo;
    }

    @Override
    public WisdomBirthMonthOutputVo getWisdomBirthMonth(SameDayVaccInputVo inputVO) {
        WisdomBirthMonthOutputVo wisdomBirthMonthOutputVo = new WisdomBirthMonthOutputVo();
        List<WisdomBirthMonth> wisdomBirthMonthList = commonMapper.getWisdomBirthMonth(PROVINCE_CODE.equals(inputVO.getAreaCode())? null : inputVO.getAreaCode());
        wisdomBirthMonthOutputVo.setWisdomBirthMonthList(wisdomBirthMonthList);
        return wisdomBirthMonthOutputVo;
    }

    @Override
    public ScanVaccinationRateOutputVo getWisdomScanVaccinationRates(ScanVaccinationRatesInputVo inputVO) {
        ScanVaccinationRateOutputVo outputVo = new ScanVaccinationRateOutputVo();
        List<ScanVaccinationRate> scanVaccinationRates = redisVaccinationService.getScanVaccinationRate(inputVO.getAreaCode(),inputVO.getLevel());
        outputVo.setScanVaccinationRates(scanVaccinationRates);
        return outputVo;
    }


    /**
     * 实时库存
     */
    @Override
    public InventoryOutputVo getInventory(InventoryInputVo inputVO) {
        InventoryOutputVo outputVo = new InventoryOutputVo();

        // 测试数据
//        List<Inventory> inventoryList = new ArrayList<>();
//        Inventory inventory = new Inventory();
//        inventory.setGBNAME("哈尔滨市");
//        inventory.setGOODSTYPE("01");
//        inventory.setGOODSSORTCODE("01");
//        inventory.setSTORENUM(60);
//        inventory.setLessOneMonthNum(10);
//        inventory.setLessThreeMonthNum(20);
//        inventory.setMoreThreeMonthNum(30);
//        inventoryList.add(inventory);
//        inventory = new Inventory();
//        inventory.setGBNAME("鸡西市");
//        inventory.setGOODSTYPE("01");
//        inventory.setGOODSSORTCODE("01");
//        inventory.setSTORENUM(60);
//        inventory.setLessOneMonthNum(10);
//        inventory.setLessThreeMonthNum(20);
//        inventory.setMoreThreeMonthNum(30);
//        inventoryList.add(inventory);
//        inventory = new Inventory();
//        inventory.setGBNAME("大庆市");
//        inventory.setGOODSTYPE("01");
//        inventory.setGOODSSORTCODE("01");
//        inventory.setSTORENUM(60);
//        inventory.setLessOneMonthNum(10);
//        inventory.setLessThreeMonthNum(20);
//        inventory.setMoreThreeMonthNum(30);
//        inventoryList.add(inventory);
//        inventory = new Inventory();
//        inventory.setGBNAME("鹤岗市");
//        inventory.setGOODSTYPE("01");
//        inventory.setGOODSSORTCODE("01");
//        inventory.setSTORENUM(60);
//        inventory.setLessOneMonthNum(10);
//        inventory.setLessThreeMonthNum(20);
//        inventory.setMoreThreeMonthNum(30);
//        inventoryList.add(inventory);
//        outputVo.setInventoryList(inventoryList);

        List<Inventory> inventoryList = redisVaccinationService.getInventory(inputVO.getAreaCode(), inputVO.getVaccineId());
        outputVo.setInventoryList(inventoryList);
        return outputVo;
    }

    /**
     * 近效期库存
     * */
    @Override
    public WisdomInventoryOutputVo getWisdomInventory(InventoryInputVo inputVO) {
        WisdomInventoryOutputVo wisdomInventoryOutputVo = new WisdomInventoryOutputVo();
        // 测试数据
//        List<WisdomInventory> wisdomInventoryList = new ArrayList<>();
//        List<WisdomInventory> NIPwisdomInventoryList = new ArrayList<>();
//
//        WisdomInventory wisdomInventory = new WisdomInventory();
//        wisdomInventory.setGOODSTYPE("01");
//        wisdomInventory.setGOODSSORTCODE("01");
//        wisdomInventory.setGOODSSORTNAME("卡介苗");
//        wisdomInventory.setProvinceSTORENUM(60);
//        wisdomInventory.setProvinceLessOneMonthNum(20);
//        wisdomInventory.setProvinceLessThreeMonthNum(30);
//        wisdomInventory.setProvinceMoreThreeMonthNum(30);
//
//        wisdomInventory.setCitySTORENUM(60);
//        wisdomInventory.setCityLessOneMonthNum(20);
//        wisdomInventory.setCityLessThreeMonthNum(30);
//        wisdomInventory.setCityMoreThreeMonthNum(30);
//
//
//        wisdomInventory.setCouSTORENUM(50);
//        wisdomInventory.setCouLessOneMonthNum(20);
//        wisdomInventory.setCouLessThreeMonthNum(30);
//        wisdomInventory.setCouMoreThreeMonthNum(30);
//
//        wisdomInventory.setUnitSTORENUM(40);
//        wisdomInventory.setUnitLessOneMonthNum(20);
//        wisdomInventory.setUnitLessThreeMonthNum(30);
//        wisdomInventory.setUnitMoreThreeMonthNum(30);
//        NIPwisdomInventoryList.add(wisdomInventory);
//        wisdomInventoryList.add(wisdomInventory);
//        wisdomInventory = new WisdomInventory();
//        wisdomInventory.setGOODSTYPE("02");
//        wisdomInventory.setGOODSSORTCODE("02");
//        wisdomInventory.setGOODSSORTNAME("乙肝");
//        wisdomInventory.setProvinceSTORENUM(10);
//        wisdomInventory.setProvinceLessOneMonthNum(20);
//        wisdomInventory.setProvinceLessThreeMonthNum(30);
//        wisdomInventory.setProvinceMoreThreeMonthNum(30);
//
//        wisdomInventory.setCitySTORENUM(60);
//        wisdomInventory.setCityLessOneMonthNum(20);
//        wisdomInventory.setCityLessThreeMonthNum(30);
//        wisdomInventory.setCityMoreThreeMonthNum(30);
//
//
//        wisdomInventory.setCouSTORENUM(50);
//        wisdomInventory.setCouLessOneMonthNum(20);
//        wisdomInventory.setCouLessThreeMonthNum(30);
//        wisdomInventory.setCouMoreThreeMonthNum(30);
//
//        wisdomInventory.setUnitSTORENUM(40);
//        wisdomInventory.setUnitLessOneMonthNum(20);
//        wisdomInventory.setUnitLessThreeMonthNum(30);
//        wisdomInventory.setUnitMoreThreeMonthNum(30);
//
//        wisdomInventoryList.add(wisdomInventory);
//        wisdomInventoryOutputVo.setWisdomInventoryList(wisdomInventoryList);
//        NIPwisdomInventoryList.add(wisdomInventory);
//        wisdomInventoryOutputVo.setNIPwisdomInventoryList(NIPwisdomInventoryList);

        List<WisdomInventory> wisdomInventoryList = redisVaccinationService.getWisdomInventory(inputVO.getAreaCode());
        wisdomInventoryOutputVo.setWisdomInventoryList(wisdomInventoryList);
        List<WisdomInventory> NIPwisdomInventoryList = redisVaccinationService.getNIPWisdomInventory(inputVO.getAreaCode());
        wisdomInventoryOutputVo.setNIPwisdomInventoryList(NIPwisdomInventoryList);
        return wisdomInventoryOutputVo;
    }

}
