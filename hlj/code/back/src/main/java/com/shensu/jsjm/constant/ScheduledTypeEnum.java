package com.shensu.jsjm.constant;

public enum ScheduledTypeEnum {
    ClassUnit("接种单位分类", "ClassUnit"),
    SameDayVaccination("当日接种","SameDayVaccination"),
    NIPVaccination("国家免疫规划疫苗全程接种率","NIPVaccination"),
    VaccinationCount("当年非免疫规划疫苗接种统计","VaccinationCount"),
    VaccinationRate("疫苗接种率","VaccinationRate"),
    ScanVaccinationRate("扫码接种率","ScanVaccinationRate"),
    Inventory("实时库存","Inventory"),
    WisdomInventory("智慧决策实时库存","WisdomInventory"),
    NeonateYear("新生儿年份统计","NeonateYear"),
    NeonateMonth("新生儿月份统计","NeonateMonth"),
    ;

    private String name;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    ScheduledTypeEnum(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
