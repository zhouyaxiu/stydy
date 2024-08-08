package com.shensu.jsjm.mapper;

import com.shensu.jsjm.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VaccinationMapper {

    int getSameDayFreeVacc(@Param("areaCode") String areaCode);

    int getSameDayCharge(@Param("areaCode") String areaCode);

    List<JZJL> getSameDayFreeJZJL(@Param("jzTableName") String jzTableName);

    List<JZJL> getSameDayChargeJZJL(@Param("jzTableName") String jzTableName);

    EpiStatAge getVaccRateByCity(int age);

    EpiStatAge getVaccRateByUnit(@Param("age")int age, @Param("areaCode")String areaCode);

    ScanVaccinationRate getScanVaccinationRates(@Param("startDate")String startDate, @Param("endDate")String endDate,
                                                @Param("areaCode")String areaCode,@Param("level") int level);

    ScanVaccinationRate getScanVaccinationRatesByCountry(@Param("startDate")String startDate, @Param("endDate")String endDate,
                                                         @Param("areaCode")String areaCode);

    List<NIPVacc> getNIPVaccs(@Param("areaCode")String areaCode, @Param("level") int level);

    GJJZJCRPT getVaccinationCounts(@Param("areaCode")String areaCode,@Param("level") int level);

    List<WisdomBirthYear> getWisdomBirthYear(@Param("areaCode")String areaCode,@Param("level") int level);

    List<WisdomBirthMonth> getWisdomBirthMonth(@Param("areaCode")String areaCode,@Param("level") int level);


    long getTodayShotPeople(@Param("areaCode")String areaCode,@Param("level") int level);

    long getTodayShotJC(@Param("areaCode")String areaCode,@Param("level") int level);

    long getChildBook(@Param("areaCode")String areaCode,@Param("level") int level);

    long getChildBook06(@Param("areaCode")String areaCode,@Param("level") int level,@Param("limitDate") String limitDate);

    long getChildBook718(@Param("areaCode")String areaCode,@Param("level") int level,@Param("limitDateStart") String limitDateStart,@Param("limitDateEnd") String limitDateEnd);

    long getChildBook18(@Param("areaCode")String areaCode,@Param("level") int level,@Param("limitDate") String limitDate);

    List<WisdomTodayData> getTodayShotInfo(@Param("dateStr") String dateStr);

    List<WisdomTodayData> getTodayChildBook(@Param("dateStr") String dateStr);

    List<WisdomTodayData> getTotalChildBook();

    void insertVaccinationInfo(@Param("list") List<WisdomTodayData> mergedList);

    void deleteVaccinationInfo();

    WisdomTodayData getVaccinationInfo(@Param("areaCode") String areaCode);
}
