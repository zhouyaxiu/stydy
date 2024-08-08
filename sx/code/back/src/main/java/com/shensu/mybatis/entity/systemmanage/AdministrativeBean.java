package com.shensu.mybatis.entity.systemmanage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhangtt
 * 上级接种单位实体类
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class AdministrativeBean implements Serializable {
	private long rowNum;
	private String baseCode;
	private String id;
	private String code;
	private String name;
	private String supId;
	private AdministrativeBean superAdministrative;
	private String updateDate;
	private String updateUser;
	private String tableStatus;
}
