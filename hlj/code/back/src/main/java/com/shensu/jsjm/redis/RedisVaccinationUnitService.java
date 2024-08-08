package com.shensu.jsjm.redis;

public interface RedisVaccinationUnitService {


    int getClassUnit(String areaCode,String type);

    void updateClassUnitByType(String areaCode, String type);

    void updateClassUnitAll(String areaCode);

    int getTodayClassUnit(String areaCode, String type);

    void updateTodayClassUnitByType(String areaCode, String type);

}
