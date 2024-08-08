package com.shensu.mybatis.entity.check;

import lombok.Data;

/**
 * 个案信息对象
 */
@Data
public class  InfoBean {

	public String fchildno;		//个案编码

	public String cid;		//身份证件号码

	public String unitname;		//门诊名称

	public String status;		//状态

	public String stationcode;	//归属门诊编码

	public String imuno;		//条码

	public String name;			//受种者姓名

	public String birth;		//出生日期

	public String sex;			//性别

	public String phone;			//电话

	public String regtype;		//户籍类型

	public String fatherName;	//父亲姓名

	public String motherName;	//母亲姓名

	public String  otherLinkName;

	public String motherPhone;		//母亲电话

	public String fatherPhone;	//父亲电话

	public String  fResideAddr;//家庭住址



}
