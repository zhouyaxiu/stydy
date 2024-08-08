package com.shensu.util;

import java.util.HashMap;
import java.util.Map;

public class PlantTypeAndDate {


    public final static String NORMAL = "1";


    public final static String REPLACE = "6";

    public static Map<String, String> SPECIAL_SHOT_DATE_MAP = new HashMap<>();


    static {
        //shotDateMap.put("1", "");
        SPECIAL_SHOT_DATE_MAP.put("2", "1983-01-01");
        //SPECIAL_SHOT_DATE_MAP.put("3", "1981-01-01");
        SPECIAL_SHOT_DATE_MAP.put("4", "1985-01-01");
        //SPECIAL_SHOT_DATE_MAP.put("5", "1982-01-01");
        SPECIAL_SHOT_DATE_MAP.put("6", "1980-01-01");
        SPECIAL_SHOT_DATE_MAP.put("7", "1984-01-01");
        SPECIAL_SHOT_DATE_MAP.put("8", "1986-01-01");
    }

    private String plantType;

    private String plantDate;

    private String shotKind;

    public String getPlantType() {
        return plantType;
    }

    public void setPlantType(String plantType) {
        this.plantType = plantType;
    }

    public String getPlantDate() {
        return plantDate;
    }

    public void setPlantDate(String plantDate) {
        this.plantDate = plantDate;
    }

    public String getShotKind() {
        return shotKind;
    }

    public void setShotKind(String shotKind) {
        this.shotKind = shotKind;
    }
}
