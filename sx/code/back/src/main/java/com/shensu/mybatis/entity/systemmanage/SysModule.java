package com.shensu.mybatis.entity.systemmanage;

import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 对应模块管理数据库表字段
 * @author liuwei
 */
@Data
public final class SysModule implements Serializable {
	/**
	 * 模块ID
	 */
	private String id;
	/**
	 * 模块编号--树形排序字段
	 */
	private String moduleCode;
	/**
	 * 模块名称
	 */
	private String moduleName;
	/**
	 * 模块URL
	 */
	private String moduleUrl;
	/**
	 * 模块说明
	 */
	private String moduleExplain;
	/**
	 * 模块启用状态--  0-禁用 1-可用 默认1
	 */
	private String onOff;
	/**
	 * 父模块ID
	 */
	private String supId;
	/**
	 * 新增修改时间
	 */
	private String updateDate;
	/**
	 * 新增修改用户
	 */
	private String updateUser;
	/**
	 * 数据状态-- 0-正常 1-删除 默认0
	 */
	private String tableStatus;
	/**
	 * 模块简称
	 */
	private String moduleShortName;
	/**
	 * 模块开放级别
	 */
	private String moduleLevel;
	/**
	 * 子模块对象
	 */
	private List<SysModule> supModules;

	/**
	 * 包含的操作：增删改导出
	 */
	private String containOptions;
	private String[] containOptionsArray;


	@Override
	public String toString() {
		return "SysModule{" +
				"id='" + id + '\'' +
				", moduleCode='" + moduleCode + '\'' +
				", moduleName='" + moduleName + '\'' +
				", moduleShortName='" + moduleShortName + '\'' +
				", moduleUrl='" + moduleUrl + '\'' +
				", moduleExplain='" + moduleExplain + '\'' +
				", onOff='" + onOff + '\'' +
				", supId='" + supId + '\'' +
				", supModules=" + supModules +
				", moduleLevel='" + moduleLevel + '\'' +
				", updateDate='" + updateDate + '\'' +
				", updateUser='" + updateUser + '\'' +
				", tableStatus='" + tableStatus + '\'' +
				", containOptions='" + containOptions + '\'' +
				", containOptionsArray=" + Arrays.toString(containOptionsArray) +
				'}';
	}
}
