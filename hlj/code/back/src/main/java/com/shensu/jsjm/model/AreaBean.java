package com.shensu.jsjm.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class AreaBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String          id;
    private String          code;
    private String          name;
    private String          baseCode;
    private String          simpleName;
    private List<AreaBean>  children;
    private List<AreaBean>  stations;
    private List<AreaBean>  cys;
    private AreaBean  parentArea;
    private int level;
    public static final int type_station = 1;
    public static final int type_cy      = 2;
    private String          isRab;
    private int stationOrCy;

}
