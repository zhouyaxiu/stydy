package com.shensu.jsjm.redis;

import com.shensu.jsjm.model.*;

import java.util.List;

public interface RedisVaccinationService {

    void setSameDayVaccination(String areaCode, int level);

    List<SameDayVacc> getSameDayVaccination(String areaCode, int level);

    void setVaccinationRate(String areaCode, int level, int age);

    List<VaccinationRate> getVaccinationRate(String areaCode, int age);

    void setScanVaccinationRate(String areaCode, int level);

    List<ScanVaccinationRate> getScanVaccinationRate(String areaCode, int level);


    List<Inventory> getInventory(String areaCode, String vaccineId);

    void setInventory(String areaCode);

    void setNIPVaccination(String areaCode, int level);

    List<NIPVacc> getNIPVaccination(String areaCode, int level);

    void setVaccinationCounts(String areaCode, int level);

    List<VaccinationCount> getVaccinationCounts(String areaCode, int level);

    void setWisdomTodayData();
    WisdomTodayData getWisdomTodayData(String areaCode);

    List<ScanVaccinationRate> getWisdomScanVaccinationRate(String areaCode, String month ,int level);

    List<WisdomInventory> getWisdomInventory(String areaCode);

    List<WisdomInventory> getNIPWisdomInventory(String areaCode);

    void setWisdomInventory(String areaCode);
}
