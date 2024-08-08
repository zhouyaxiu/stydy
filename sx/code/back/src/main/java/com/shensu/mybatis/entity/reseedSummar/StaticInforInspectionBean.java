package com.shensu.mybatis.entity.reseedSummar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StaticInforInspectionBean {
	// 轮次
    private String roundName;
    // 学年
    private String year;
    // 学校
    private String schName;
    // 班级
    private String claName;
    // 学生总数
    private int stuTotal;
    // 本省应种
    private int thisProvinceYz;
    // 外省应种
    private int outProvinceYz;
    // 本省实种
    private int thisProvinceSz;
    // 外省实种
    private int outProvinceSz;
    // 水痘1已种
    private int varOneSz;
    // 水痘2已种
    private int varTwoSz;
    // 流感
    private int fluSz;
}
