package com.shensu.service.reseedSummar;

import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.reseedSummar.StaticInforInspectionBean;
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
}
