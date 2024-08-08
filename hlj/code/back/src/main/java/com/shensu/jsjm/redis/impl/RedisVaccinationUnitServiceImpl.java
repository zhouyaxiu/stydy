package com.shensu.jsjm.redis.impl;

import com.shensu.jsjm.constant.ClassUnitEnum;
import com.shensu.jsjm.mapper.VaccinationUnitMapper;
import com.shensu.jsjm.redis.RedisAreaService;
import com.shensu.jsjm.redis.RedisVaccinationUnitService;
import com.shensu.jsjm.service.RedisService;
import com.shensu.jsjm.util.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RedisVaccinationUnitServiceImpl implements RedisVaccinationUnitService {

    @Resource
    private VaccinationUnitMapper vaccinationUnitMapper;

    @Resource
    private RedisService redisService;

    @Override
    public int getTodayClassUnit(String areaCode, String type) {
        String[] template = CommonUtil.productTemplate(areaCode);
        ClassUnitEnum classType = ClassUnitEnum.valueOf(type);
        String key;
        String mapKey = areaCode;
        int num = 0;
        switch (classType) {
            case NOW_COMMON_UNIT_NUM:
                key = ClassUnitEnum.NOW_COMMON_UNIT_NUM.getKey();
                if (!redisService.hasMapKey(key, mapKey)) {
                    int commonUnitNum = vaccinationUnitMapper.getTodayUnitNumByType(template[0], template[1], "01");
                    redisService.addMap(key, mapKey, commonUnitNum);
                }
                num = (int) redisService.getMapField(key, mapKey);
                break;
            case NOW_ADULT_UNIT_NUM:
                key = ClassUnitEnum.NOW_ADULT_UNIT_NUM.getKey();
                if (!redisService.hasMapKey(key, mapKey)) {
                    int adultUnitNum = vaccinationUnitMapper.getTodayUnitNumByType(template[0], template[1], "02");
                    redisService.addMap(key, mapKey, adultUnitNum);
                }
                num = (int) redisService.getMapField(key, mapKey);
                break;
            case NOW_HOS_UNIT_NUM:
                key = ClassUnitEnum.NOW_HOS_UNIT_NUM.getKey();
                if (!redisService.hasMapKey(key, mapKey)) {
                    int hosUnitNum = vaccinationUnitMapper.getTodayUnitNumByType(template[0], template[1],"03");
                    redisService.addMap(key, mapKey, hosUnitNum);
                }
                num = (int) redisService.getMapField(key, mapKey);
                break;
            case NOW_RABIES_UNIT_NUM:
                key = ClassUnitEnum.NOW_RABIES_UNIT_NUM.getKey();
                if (!redisService.hasMapKey(key, mapKey)) {
                    int rabiesUnitNum = vaccinationUnitMapper.getTodayUnitNumByType(template[0], template[1], "04");
                    redisService.addMap(key, mapKey, rabiesUnitNum);
                }
                num = (int) redisService.getMapField(key, mapKey);
                break;
            case NOW_COVID_UNIT_NUM:
                key = ClassUnitEnum.NOW_COVID_UNIT_NUM.getKey();
                if (!redisService.hasMapKey(key, mapKey)) {
                    int covidUnitNum = vaccinationUnitMapper.getTodayUnitNumByType(template[0], template[1], "06");
                    redisService.addMap(key, mapKey, covidUnitNum);
                }
                num = (int) redisService.getMapField(key, mapKey);
                break;
        }
        return num;
    }

    /**
     * 接种单位分类
     */
    @Override
    public int getClassUnit(String areaCode, String type) {
        String[] template = CommonUtil.productTemplate(areaCode);
        System.out.println("codeTemplate1: " + template[0] + " ;codeTemplate2: " + template[1]);
        ClassUnitEnum classType = ClassUnitEnum.valueOf(type);
        String key;
        String mapKey = areaCode;
        int num = 0;
        switch (classType) {
            case COMMON_UNIT_NUM:
                key = ClassUnitEnum.COMMON_UNIT_NUM.getKey();
                if (!redisService.hasMapKey(key, mapKey)) {
                    int commonUnitNum = vaccinationUnitMapper.getUnitNumByType(template[0], template[1], "01");
                    redisService.addMap(key, mapKey, commonUnitNum);
                }
                num = (int) redisService.getMapField(key, mapKey);
                break;
            case ADULT_UNIT_NUM:
                key = ClassUnitEnum.ADULT_UNIT_NUM.getKey();
                if (!redisService.hasMapKey(key, mapKey)) {
                    int adultUnitNum = vaccinationUnitMapper.getUnitNumByType(template[0], template[1], "02");
                    redisService.addMap(key, mapKey, adultUnitNum);
                }
                num = (int) redisService.getMapField(key, mapKey);
                break;
            case HOS_UNIT_NUM:
                key = ClassUnitEnum.HOS_UNIT_NUM.getKey();
                if (!redisService.hasMapKey(key, mapKey)) {
                    int hosUnitNum = vaccinationUnitMapper.getUnitNumByType(template[0], template[1],"03");
                    redisService.addMap(key, mapKey, hosUnitNum);
                }
                num = (int) redisService.getMapField(key, mapKey);
                break;
            case RABIES_UNIT_NUM:
                key = ClassUnitEnum.RABIES_UNIT_NUM.getKey();
                if (!redisService.hasMapKey(key, mapKey)) {
                    int rabiesUnitNum = vaccinationUnitMapper.getUnitNumByType(template[0], template[1], "04");
                    redisService.addMap(key, mapKey, rabiesUnitNum);
                }
                num = (int) redisService.getMapField(key, mapKey);
                break;
            case COVID_UNIT_NUM:
                key = ClassUnitEnum.COVID_UNIT_NUM.getKey();
                if (!redisService.hasMapKey(key, mapKey)) {
                    int covidUnitNum = vaccinationUnitMapper.getUnitNumByType(template[0], template[1], "06");
                    redisService.addMap(key, mapKey, covidUnitNum);
                }
                num = (int) redisService.getMapField(key, mapKey);
                break;
        }
        return num;
    }

    @Override
    public void updateTodayClassUnitByType(String areaCode, String typeName) {
        String[] template = CommonUtil.productTemplate(areaCode);
        ClassUnitEnum classType = ClassUnitEnum.valueOf(typeName);
        String key;
        String mapKey = areaCode;
        switch (classType) {
            case NOW_COMMON_UNIT_NUM:
                key = ClassUnitEnum.NOW_COMMON_UNIT_NUM.getKey();
                int commonUnitNum = vaccinationUnitMapper.getTodayUnitNumByType(template[0], template[1], "01");
                redisService.addMap(key, mapKey, commonUnitNum);
                break;
            case NOW_ADULT_UNIT_NUM:
                key = ClassUnitEnum.NOW_ADULT_UNIT_NUM.getKey();
                int adultUnitNum = vaccinationUnitMapper.getTodayUnitNumByType(template[0], template[1], "02");
                redisService.addMap(key, mapKey, adultUnitNum);
                break;
            case NOW_HOS_UNIT_NUM:
                key = ClassUnitEnum.NOW_HOS_UNIT_NUM.getKey();
                int hosUnitNum = vaccinationUnitMapper.getTodayUnitNumByType(template[0], template[1],"03");
                redisService.addMap(key, mapKey, hosUnitNum);
                break;
            case NOW_RABIES_UNIT_NUM:
                key = ClassUnitEnum.NOW_RABIES_UNIT_NUM.getKey();
                int rabiesUnitNum = vaccinationUnitMapper.getTodayUnitNumByType(template[0], template[1], "04");
                redisService.addMap(key, mapKey, rabiesUnitNum);
                break;
            case NOW_COVID_UNIT_NUM:
                key = ClassUnitEnum.NOW_COVID_UNIT_NUM.getKey();
                int covidUnitNum = vaccinationUnitMapper.getTodayUnitNumByType(template[0], template[1], "06");
                redisService.addMap(key, mapKey, covidUnitNum);
                break;
        }
    }

    @Override
    public void updateClassUnitByType(String areaCode, String typeName) {
        String[] template = CommonUtil.productTemplate(areaCode);
        ClassUnitEnum classType = ClassUnitEnum.valueOf(typeName);
        String key;
        String mapKey = areaCode;
        switch (classType) {
            case COMMON_UNIT_NUM:
                key = ClassUnitEnum.COMMON_UNIT_NUM.getKey();
                int commonUnitNum = vaccinationUnitMapper.getUnitNumByType(template[0], template[1], "01");
                redisService.addMap(key, mapKey, commonUnitNum);
                break;
            case ADULT_UNIT_NUM:
                key = ClassUnitEnum.ADULT_UNIT_NUM.getKey();
                int adultUnitNum = vaccinationUnitMapper.getUnitNumByType(template[0], template[1], "02");
                redisService.addMap(key, mapKey, adultUnitNum);
                break;
            case HOS_UNIT_NUM:
                key = ClassUnitEnum.HOS_UNIT_NUM.getKey();
                int hosUnitNum = vaccinationUnitMapper.getUnitNumByType(template[0], template[1],"03");
                redisService.addMap(key, mapKey, hosUnitNum);
                break;
            case RABIES_UNIT_NUM:
                key = ClassUnitEnum.RABIES_UNIT_NUM.getKey();
                int rabiesUnitNum = vaccinationUnitMapper.getUnitNumByType(template[0], template[1], "04");
                redisService.addMap(key, mapKey, rabiesUnitNum);
                break;
            case COVID_UNIT_NUM:
                key = ClassUnitEnum.COVID_UNIT_NUM.getKey();
                int covidUnitNum = vaccinationUnitMapper.getUnitNumByType(template[0], template[1], "06");
                redisService.addMap(key, mapKey, covidUnitNum);
                break;
        }
    }


    @Override
    public void updateClassUnitAll(String areaCode) {
        this.updateClassUnitByType(areaCode, ClassUnitEnum.COMMON_UNIT_NUM.name());
        this.updateClassUnitByType(areaCode, ClassUnitEnum.ADULT_UNIT_NUM.name());
        this.updateClassUnitByType(areaCode, ClassUnitEnum.HOS_UNIT_NUM.name());
        this.updateClassUnitByType(areaCode, ClassUnitEnum.RABIES_UNIT_NUM.name());
        this.updateClassUnitByType(areaCode, ClassUnitEnum.COVID_UNIT_NUM.name());

        this.updateTodayClassUnitByType(areaCode, ClassUnitEnum.NOW_COMMON_UNIT_NUM.name());
        this.updateTodayClassUnitByType(areaCode, ClassUnitEnum.NOW_ADULT_UNIT_NUM.name());
        this.updateTodayClassUnitByType(areaCode, ClassUnitEnum.NOW_HOS_UNIT_NUM.name());
        this.updateTodayClassUnitByType(areaCode, ClassUnitEnum.NOW_RABIES_UNIT_NUM.name());
        this.updateTodayClassUnitByType(areaCode, ClassUnitEnum.NOW_COVID_UNIT_NUM.name());

    }


}
