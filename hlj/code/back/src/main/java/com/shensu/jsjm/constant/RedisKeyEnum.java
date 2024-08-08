package com.shensu.jsjm.constant;

public enum RedisKeyEnum {

    visual_personNum("人数", "visual_personNum"),
    visual_personForm("人员构成", "visual_personForm"),
    visual_same_day_vaccination("当日接种", "visual_same_day_vaccination"),
    visual_nip_vaccination("国家免疫规划疫苗全程接种率", "visual_nip_vaccination"),
    visual_vaccination_rate("疫苗接种率","visual_vaccination_rate"),

    visual_vaccination_count("非免疫苗接量","visual_vaccination_count"),

    visual_wisdomTodayData("智慧决策当日数据","visual_wisdomTodayData"),

    visual_wisdomBirthYear("历年出生儿童建档数","visual_wisdomBirthYear"),

    visual_wisdomBirthMonth("当年出生儿童建档数","visual_wisdomBirthMonth"),


    visual_scan_vaccination_rate("扫码接种率","visual_scan_vaccination_rate"),

    visual_areaMapCode("省市区code","visual_areaMapCode"),
    visual_allAreaMapCode("全部地区code","visual_allAreaMapCode"),
    visual_resultAllInfo("统计数据","visual_resultAllInfo"),
    visual_resultAllTodayInfo("统计数据当天","visual_resultAllTodayInfo"),
    visual_device("冷链设备情况","visual_device"),
    visual_inventory("实时库存","visual_inventory"),
    visual_wisdomInventory("实时库存","visual_wisdomInventory"),
    visual_nanjing_token("南京token令牌","visual_nanjing_token"),
    visual_area_supCodeAndCityCode("省市code和层级","visual_area_supCodeAndCityCode"),


    auth_token_user_get("接口请求token get请求","auth_token_user_get"),
    auth_token_user_post("接口请求token post请求","auth_token_user_post"),

    ;


    RedisKeyEnum(String name, String key) {
        this.name = name;
        this.key = key;
    }

    private String name;
    private String key;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
