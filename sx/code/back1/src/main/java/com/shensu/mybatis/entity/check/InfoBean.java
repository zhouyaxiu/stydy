package com.shensu.mybatis.entity.check;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 个案信息对象
 */
@Data
public class InfoBean {

	public String fchildno;		//个案编码

	public String cid;		//身份证件号码

	public String unitname;		//门诊名称

	public String status;		//状态

	public String stationcode;	//归属门诊编码

	protected String stationName; //归属门诊名称

	public String imuno;		//条码

	public String name;			//受种者姓名

	public String birth;		//出生日期

	public String sex;			//性别

	public String regtype;		//户籍类型

	public String fathername;	//母亲姓名

	public String mothername;	//父亲姓名

	public String otherLinkName;
	public String otherLinkPhone;//其他联系人电话
	public String mobphone;		//联系电话

	public String otherphone;	//其他电话

	public String fppdw;		//PPD试剂反应宽，结核菌素接种反应“宽”

	public String fppdh;		//PPD试剂反应高，结核菌素接种反应“高”

	public String fResideAddr;//家庭住址

	public String dwelladdr;//家庭住址

	/*
	public String mazhen; //麻疹

	public String shuidou1; //水痘1

	public String shuidou2; //水痘2

	public String liugan1; //流感1

	public String liugan2; //流感2
	*/







	/*
	private String fluFir; //流感1

	private String fluFirId; //流感1剂次

	private String fluFirName; //流感1剂次名称

	private String fluSec; //流感2

	private String fluSecId; //流感2剂次

	private String fluSecName; //流感2剂次名称

	private String varFir; //水痘1

	private String varFirId; //水痘1剂次

	private String varFirName; //水痘1剂次名称

	private String varSec; //水痘2

	private String varSecId; //水痘2剂次

	private String varSecName; //水痘2剂次名称

	private String measles; //麻疹

	private String measlesId; //麻疹

	private String measlesName; //麻疹
	*/



}
