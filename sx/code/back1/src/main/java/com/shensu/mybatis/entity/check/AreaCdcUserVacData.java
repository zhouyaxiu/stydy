package com.shensu.mybatis.entity.check;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName AreaCdcUserVacData.java
 * @Description 接种证查验统计结果
 * @createTime 2023年04月13日 16:11
 */
@Data
@ApiModel("接种证查验统计结果")
public class AreaCdcUserVacData {

	@ApiModelProperty(value = "单位编码")
    private String areaCode;
	@ApiModelProperty(value = "单位名称")
	private String areaName;
	@ApiModelProperty(value = "学校")
	private String schName;
	@ApiModelProperty(value = "学校id")
	private String schId;
	@ApiModelProperty(value = "班级名称")
	private String claName;
	@ApiModelProperty(value = "需查验班级数")
	private int needClassNum;
	@ApiModelProperty(value = "查验班级数")
	private int classNum;
	@ApiModelProperty(value = "辖区学校数")
	private int schNum = 0;
	@ApiModelProperty(value = "托幼机构小学应查验数")
	private int needCySchNum = 0;
	@ApiModelProperty(value = "已查验的学校数")
	private int cySchNum = 0;
	@ApiModelProperty(value = "入托入学人数(所有儿童数)")
	private int stuCyNum = 0;
	@ApiModelProperty(value = "查验人数(已查验的儿童数)")
	private int realCyNum = 0;
	@ApiModelProperty(value = "持预防接种证人数")
	private int hasCardNum = 0;
	@ApiModelProperty(value = "补预防接种证人数")
	private int patchCardNum = 0;
	@ApiModelProperty(value = "完成全程接种人数")
	private int qcjzCount = 0;
	@ApiModelProperty(value = "需补种疫苗人数")
	private int needReplantNum = 0;
	@ApiModelProperty(value = "完成全程补种人数")
	private int hasReplant = 0;
	@ApiModelProperty(value = "卡介苗补种剂次")
	private int kajieHas = 0;
	@ApiModelProperty(value = "卡介苗应补种剂次")
	private int kajieNeed = 0;
	@ApiModelProperty(value = "乙肝补种剂次")
	private int yiganHas = 0;
	@ApiModelProperty(value = "乙肝应补种剂次")
	private int yiganNeed = 0;
	@ApiModelProperty(value = "脊灰补种剂次")
	private int jihuiHas = 0;
	@ApiModelProperty(value = "脊灰应补种剂次")
	private int jihuiNeed = 0;
	@ApiModelProperty(value = "百白破补种剂次")
	private int baibaipoHas = 0;
	@ApiModelProperty(value = "百白破应补种剂次")
	private int baibaipoNeed = 0;
	@ApiModelProperty(value = "白破补种剂次")
	private int baipoHas = 0;
	@ApiModelProperty(value = "白破应补种剂次")
	private int baipoNeed = 0;
	@ApiModelProperty(value = "麻腮风补种剂次")
	private int mafengHas = 0;
	@ApiModelProperty(value = "麻腮风应补种剂次")
	private int mafengNeed = 0;
	@ApiModelProperty(value = "A群流脑补种剂次")
	private int aqunliunaoHas = 0;
	@ApiModelProperty(value = "A群流脑应补种剂次")
	private int aqunliunaoNeed = 0;
	@ApiModelProperty(value = "A群C群流脑补种剂次")
	private int acqunliunaoHas = 0;
	@ApiModelProperty(value = "A群C群流脑应补种剂次")
	private int acqunliunaoNeed = 0;
	@ApiModelProperty(value = "乙脑补种剂次")
	private int yinaojianduHas = 0;
	@ApiModelProperty(value = "乙脑应补种剂次")
	private int yinaojianduNeed = 0;

	@ApiModelProperty(value = "甲肝补种剂次")
	private int jiaganjianduHas = 0;
	@ApiModelProperty(value = "甲肝应补种剂次")
	private int jiaganjianduNeed = 0;


}
