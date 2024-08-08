package com.shensu.mybatis.entity.systemmanage;

import lombok.Data;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName VSysStation.java
 * @Description TODO
 * @createTime 2023年09月15日 17:13
 */
@Data
public class VSysStation {

    /**
     * 地市编码
     */
    private String citycode;

    /**
     * 地市名称
     */
    private String cityname;

    /**
     * 区县编码
     */
    private String countycode;

    /**
     * 区县名称
     */
    private String countyname;

    /**
     * 街道,社区编码
     */
    private String supcode;

    /**
     * 街道,社区名称
     */
    private String supname;

    /**
     * 接种单位编码
     */
    private String stationcode;

    /**
     * 接种单位名称
     */
    private String stationname;

    /**
     * 接种单位id
     */
    private String id;

    /**
     * 接种单位上级街道社区id
     */
    private String areaId;

    /**
     * 接种单位newBasecode
     */
    private String newBasecode;

}
