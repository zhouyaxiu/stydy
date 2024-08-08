package com.shensu.jsjm.constant;

/**
 * 接种单位分类
 */
public enum ClassUnitEnum {

    COMMON_UNIT_NUM("常规接种单位数量", "visual_commonUnitNum"),
    HOS_UNIT_NUM("产科接种单位数量", "visual_hosUnitNum"),
    COVID_UNIT_NUM("新冠疫苗接种点数量","visual_COVIDUnitNum"),
    ADULT_UNIT_NUM("成人接种单位数量", "visual_adultUnitNum"),
    RABIES_UNIT_NUM("狂犬病暴露处置门诊数量","visual_rabiesUnitNum"),
    NOW_COMMON_UNIT_NUM("当日开诊常规接种单位数量", "visual_nowCommonUnitNum"),
    NOW_HOS_UNIT_NUM("当日开诊产科接种单位数量", "visual_nowHosUnitNum"),
    NOW_ADULT_UNIT_NUM("当日开诊成人接种单位数量", "visual_nowAdultUnitNum"),
    NOW_RABIES_UNIT_NUM("当日开诊狂犬病暴露处置门诊数量","visual_nowRabiesUnitNum")
    ,
    NOW_COVID_UNIT_NUM("当日开诊新冠疫苗接种点数量","visual_nowCOVIDUnitNum")
    ;


    ClassUnitEnum(String name, String key) {
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
