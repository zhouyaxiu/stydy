package com.shensu.mybatis.entity.check;

import lombok.Data;

/**
 * 接种记录对象
 */
@Data
public class JzjlBean {

	public String fbactid;	//疫苗编码

	public String jzrq;		//接种日期（到天）

	public String fShotKind; //字段类型（特殊日期）

	public String fBactType; //疫苗类别

	public String fBactName; //疫苗名称

}
