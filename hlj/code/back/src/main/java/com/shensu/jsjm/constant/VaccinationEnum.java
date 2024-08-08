package com.shensu.jsjm.constant;

public enum VaccinationEnum {

    BCG("卡介","getSz_rate_001"),
    HepA("甲肝","getSz_rate_069"),
    DT("白破","getSz_rate_037"),
    IPV("脊灰","getSz_rate_009_all"),
    DPT("百白破","getSz_rate_015_all"),
    JEV("乙脑","getSz_rate_032_all"),
    MenA("流脑A","getSz_rate_040_all"),
    MenAC("流脑A+C","getSz_rate_045_all"),
    MV("麻疹","getSz_rate_059_all"),
    HepB("乙肝","getSz_rate_063_all"),
    EightV("八苗全程","getSz_rate_eight");

    private String name;
    private String rate;

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    VaccinationEnum(String name, String rate) {
        this.name = name;
        this.rate = rate;
    }

    public static void main(String[] args) {
        for (VaccinationEnum vaccinationEnum:VaccinationEnum.values()){
            System.out.println(vaccinationEnum.getName());
        }
    }
}
