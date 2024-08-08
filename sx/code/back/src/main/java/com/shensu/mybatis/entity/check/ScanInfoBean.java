package com.shensu.mybatis.entity.check;

import lombok.Data;

/**
 * 个案信息对象
 */
@Data
public class ScanInfoBean {

	public String fchildno;		//个案编码

	public String cid;		//身份证件号码

	public String unitname;		//门诊名称

	public String status;		//状态

	public String stationcode;	//归属门诊编码

	protected String stationName; //归属门诊名称

	public String imuno;		//条码

	public String name;			//受种者姓名

	public String birth;		//出生日期

	public String sex;			//性别代号

	public String sexName;     //性别展示

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

}
