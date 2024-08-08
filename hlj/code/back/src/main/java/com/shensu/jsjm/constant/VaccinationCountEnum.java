package com.shensu.jsjm.constant;

public enum VaccinationCountEnum {
    SZ02("乙肝疫苗", "getSZ02"),
    SZ06("白破疫苗", "getSZ06"),
    SZ04("百白破疫苗", "getSZ04"),
    SZ08("破伤风疫苗", "getSZ08"),
    SZ14("麻风疫苗", "getSZ14"),
    SZ13("麻腮疫苗", "getSZ13"),
    SZ12("麻腮风疫苗", "getSZ12"),
    SZ10("腮腺炎疫苗", "getSZ10"),
    SZ18("乙脑疫苗", "getSZ18"),
    SZ17("A群C群流脑疫苗", "getSZ17"),
    SZ19("甲肝疫苗", "getSZ19"),
    SZ20("甲乙肝疫苗", "getSZ20"),
    SZ23("Hib疫苗", "getSZ23"),
    SZ22("水痘疫苗", "getSZ22"),
    SZ24("轮状病毒疫苗", "getSZ24"),
    SZ21("流感疫苗", "getSZ21"),
    SZ25("肺炎球菌疫苗", "getSZ25"),
    SZ28("狂犬病疫苗", "getSZ28"),
    SZ30("伤寒疫苗", "getSZ30"),
    SZ37("布病疫苗", "getSZ37"),
    SZ35("鼠疫疫苗", "getSZ35"),
    SZ38("霍乱疫苗", "getSZ38"),
    SZ47("森林脑炎疫苗", "getSZ47"),
    SZ03("脊灰灭活疫苗", "getSZ03"),
    SZ52("戊肝疫苗", "getSZ52"),
    SZ50("百白破-IPV-Hib五联疫苗", "getSZ50"),
    SZ49("百白破Hib联合疫苗", "getSZ49"),
    SZ53("A群C群流脑-Hib联合疫苗", "getSZ53"),
    SZ54("EV71疫苗", "getSZ54"),
    SZ55("HPV疫苗", "getSZ55"),
    SZ2202("带状疱疹疫苗", "getSZ2202");

    private String name;
    private String value;

    VaccinationCountEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
