package com.shensu.jsjm.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VaccinationUnitMapper {

    int getUnitNumByType(@Param("template1") String template1, @Param("template2")String template2, @Param("type")String type);

    int getTodayUnitNumByType(@Param("template1") String template1, @Param("template2")String template2, @Param("type")String type);

    int getUnitNumALL(@Param("template1") String template1, @Param("template2")String template2);

    int getTodayUnitNumALL(@Param("template1") String template1, @Param("template2")String template2);
}
