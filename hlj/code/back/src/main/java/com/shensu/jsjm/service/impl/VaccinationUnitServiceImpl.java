package com.shensu.jsjm.service.impl;

import com.shensu.jsjm.constant.ClassUnitEnum;
import com.shensu.jsjm.mapper.VaccinationUnitMapper;
import com.shensu.jsjm.redis.RedisVaccinationUnitService;
import com.shensu.jsjm.service.VaccinationUnitService;
import com.shensu.jsjm.util.CommonUtil;
import com.shensu.jsjm.vo.ClassUnitInputVo;
import com.shensu.jsjm.vo.WisdomClassUnitOutputVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class VaccinationUnitServiceImpl implements VaccinationUnitService {

    @Resource
    private VaccinationUnitMapper vaccinationUnitMapper;

    @Resource
    private RedisVaccinationUnitService redisVaccinationUnitService;

    /**
     * 智慧决策: 接种单位分类
     */
    @Override
    public WisdomClassUnitOutputVo getWidomClassification(ClassUnitInputVo inputVO){

        WisdomClassUnitOutputVo wisdomClassUnitOutputVo = new WisdomClassUnitOutputVo();

        int rabiesUnitNum = redisVaccinationUnitService.getClassUnit(inputVO.getAreaCode(), ClassUnitEnum.RABIES_UNIT_NUM.name());
        //常规接种单位数量
        int commonUnitNum = redisVaccinationUnitService.getClassUnit(inputVO.getAreaCode(), ClassUnitEnum.COMMON_UNIT_NUM.name());
        //成人接种单位数量
        int adultUnitNum = redisVaccinationUnitService.getClassUnit(inputVO.getAreaCode(), ClassUnitEnum.ADULT_UNIT_NUM.name());
        //产科接种单位数量
        int hosUnitNum = redisVaccinationUnitService.getClassUnit(inputVO.getAreaCode(), ClassUnitEnum.HOS_UNIT_NUM.name());
        //新冠疫苗接种点数量
        int covidUnitNum = redisVaccinationUnitService.getClassUnit(inputVO.getAreaCode(), ClassUnitEnum.COVID_UNIT_NUM.name());

        String[] template = CommonUtil.productTemplate(inputVO.getAreaCode());

        int total = vaccinationUnitMapper.getUnitNumALL(template[0],template[1]);

        int nowTotal = vaccinationUnitMapper.getTodayUnitNumALL(template[0],template[1]);

        //当日开诊狂犬病暴露处置门诊
        int nowRabiesUnitNum = redisVaccinationUnitService.getTodayClassUnit(inputVO.getAreaCode(), ClassUnitEnum.NOW_RABIES_UNIT_NUM.name());
        //当日开诊常规接种单位数量
        int nowCommonUnitNum = redisVaccinationUnitService.getTodayClassUnit(inputVO.getAreaCode(), ClassUnitEnum.NOW_COMMON_UNIT_NUM.name());
        //当日开诊成人接种单位数量
        int nowAdultUnitNum = redisVaccinationUnitService.getTodayClassUnit(inputVO.getAreaCode(), ClassUnitEnum.NOW_ADULT_UNIT_NUM.name());
        //当日开诊产科接种单位数量
        int nowHosUnitNum = redisVaccinationUnitService.getTodayClassUnit(inputVO.getAreaCode(), ClassUnitEnum.NOW_HOS_UNIT_NUM.name());
        //当日开诊新冠疫苗接种点数量
        int nowCovidUnitNum = redisVaccinationUnitService.getTodayClassUnit(inputVO.getAreaCode(), ClassUnitEnum.NOW_COVID_UNIT_NUM.name());

        wisdomClassUnitOutputVo.setRabiesUnitNum(rabiesUnitNum);
        wisdomClassUnitOutputVo.setCommonUnitNum(commonUnitNum);
        wisdomClassUnitOutputVo.setAdultUnitNum(adultUnitNum);
        wisdomClassUnitOutputVo.setHosUnitNum(hosUnitNum);
        wisdomClassUnitOutputVo.setCOVIDUnitNum(covidUnitNum);
        wisdomClassUnitOutputVo.setTotal(total);

        wisdomClassUnitOutputVo.setNowRabiesUnitNum(nowRabiesUnitNum);
        wisdomClassUnitOutputVo.setNowCommonUnitNum(nowCommonUnitNum);
        wisdomClassUnitOutputVo.setNowAdultUnitNum(nowAdultUnitNum);
        wisdomClassUnitOutputVo.setNowHosUnitNum(nowHosUnitNum);
        wisdomClassUnitOutputVo.setNowCOVIDUnitNum(nowCovidUnitNum);
        wisdomClassUnitOutputVo.setNowTotal(nowTotal);

        return wisdomClassUnitOutputVo;

    }

}
