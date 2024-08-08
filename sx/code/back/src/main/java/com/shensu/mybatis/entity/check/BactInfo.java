package com.shensu.mybatis.entity.check;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName BactInfo.java
 * @Description TODO
 * @createTime 2023年09月18日 14:22
 */
@Data
public class BactInfo {

    @ApiModelProperty(value = "流感1")
    private String fluFir;

    @ApiModelProperty(value = "流感1剂次")
    private String fluFirId;

    @ApiModelProperty(value = "流感1剂次名称")
    private String fluFirName;

    @ApiModelProperty(value = "流感2")
    private String fluSec;

    @ApiModelProperty(value = "流感2剂次")
    private String fluSecId;

    @ApiModelProperty(value = "流感2剂次名称")
    private String fluSecName;

    @ApiModelProperty(value = "水痘1")
    private String varFir;

    @ApiModelProperty(value = "水痘1剂次")
    private String varFirId;

    @ApiModelProperty(value = "水痘1剂次名称")
    private String varFirName;

    @ApiModelProperty(value = "水痘2")
    private String varSec;

    @ApiModelProperty(value = "水痘2剂次")
    private String varSecId;

    @ApiModelProperty(value = "水痘2剂次名称")
    private String varSecName;

    @ApiModelProperty(value = "麻疹")
    private String measles;

    @ApiModelProperty(value = "麻疹")
    private String measlesId;

    @ApiModelProperty(value = "麻疹")
    private String measlesName;

}
