package com.shensu.jsjm.service;

import com.shensu.jsjm.vo.*;

public interface VaccinationService {
    SameDayVaccOutputVo getSameDayVaccination(SameDayVaccInputVo inputVO);

    VaccinationRateOutputVo getVaccinationRates(VaccinationRateInputVo inputVO);

    NIPVaccOutputVo getNIPVaccination(SameDayVaccInputVo inputVO);

    VaccinationCountOutputVo getVaccinationCounts(SameDayVaccInputVo inputVO);

    WisdomBirthYearOutputVo getWisdomBirthYear(SameDayVaccInputVo inputVO);

    WisdomBirthMonthOutputVo getWisdomBirthMonth(SameDayVaccInputVo inputVO);


    ScanVaccinationRateOutputVo getWisdomScanVaccinationRates(ScanVaccinationRatesInputVo inputVO);

    WisdomInventoryOutputVo getWisdomInventory(InventoryInputVo inputVO);

    InventoryOutputVo getInventory(InventoryInputVo inputVO);
}
