package com.shensu.service.reseedSummar;

import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.reseedSummar.StaticInforInspectionBean;
import com.shensu.mybatis.entity.reseedSummar.StaticInforInspectionBeanOne;
import com.shensu.util.ExcelUtil;

import java.util.List;
import java.util.Map;

/**
 * @author zcy
 * @ClassName ReseedSummarService
 * @Description
 * @createTime 2023/4/19
 */
public interface ReseedSummarService {
    /**
     *  补种汇总表查询（疾控端门诊端通用）
     * */
    PageInfo<StaticInforInspectionBean> queryList(Map<String,Object> info);

    void output(ExcelUtil excelUtil, List<StaticInforInspectionBean> list, Map<String,Object> info);

    /**
     *  补种汇总表查询（疾控端门诊端通用）
     * @param isStation 0: 门诊端 1：疾控端
     * */
    List<StaticInforInspectionBeanOne> queryListOne(String areaCode, String areaLevel, String schId, String schType, String year,
                                                    String grade, String round, String isStation, String className);

    void outputOne(ExcelUtil excelUtil, List<StaticInforInspectionBeanOne> list, String areaName, String schType, String schName, String year,
                String grade, String round, String isStation,String className,String loginLevel);
}
