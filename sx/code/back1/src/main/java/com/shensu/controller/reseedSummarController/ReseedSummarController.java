package com.shensu.controller.reseedSummarController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.reseedSummar.StaticInforInspectionBean;
import com.shensu.service.reseedSummar.ReseedSummarService;
import com.shensu.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @author zcy
 * @ClassName ReseedSummarController
 * @Description 特定疫苗接种证查验汇总表
 * @createTime 2023/4/19
 */
@RestController
@RequestMapping("reseedSummar")
public class ReseedSummarController {
    /**
     * @Fields LOGGER : 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReseedSummarController.class);

    @Autowired
    private ReseedSummarService reseedSummarService;

    /**
     * 列表查询
     */
    @RequestMapping(value = "/queryList", method = RequestMethod.POST)
    public Object queryList(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            info.put("pageSize",Constants.PAGESIZE);
            PageInfo<StaticInforInspectionBean> beans = reseedSummarService.queryList(info);
            object.put("data", beans);
        } catch (Exception e) {
            e.printStackTrace();
            com.setPopMsg(Constants.FAILMESSAGE);
            com.setSuccess(false);
            com.setCode(Constants.ResultFailCode);
            LOGGER.error(e.getMessage(), e);
        }
        Calendar end = Calendar.getInstance();
        double sec = (end.getTimeInMillis() - start.getTimeInMillis()) / 1000.0;
        object.put("sec", sec);
        com.setData(object);
        TextTemplateUtil.responseStutas(com, response);
        return com.isSuccess() ? com.getData() : com.getPopMsg();
    }


    /**
     * 列表导出
     */
    @RequestMapping(value = "/output")
    public void output(String params, HttpServletResponse response) {
        Map<String, Object> info = Sm4Util.getParams(params);
        ExcelUtil excelUtil = new ExcelUtil();
        Resource resource = new ClassPathResource("/excel/reseedSummar");
        try {
            String excelName = null;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String fileName = "特定疫苗接种证查验汇总表" + format.format(new Date());

            String roundName = info.get("vaccineName").toString();
            if ("水痘疫苗".equals(roundName)) {
                excelName = "StaticInforInspectionVARExcel";
            } else if ("流感疫苗".equals(roundName)) {
                excelName = "StaticInforInspectionFLUExcel";
            } else {
                excelName = "StaticInforInspectionMMRExcel";
            }
            excelName = new String(excelName.getBytes(), StandardCharsets.ISO_8859_1);
            if (!excelUtil.readFromExcel(resource.getFilename(), excelName)) {
                throw new RuntimeException("找不到模板文件");
            }
            info.put("pageSize",Constants.EXPORTPAGESIZE);
            PageInfo<StaticInforInspectionBean> list = reseedSummarService.queryList(info);
            reseedSummarService.output(excelUtil,list.getList(),info);
            if (!excelUtil.writeExcelToResponse(response, new String(fileName.getBytes("GBK"), StandardCharsets.ISO_8859_1))) {
                throw new RuntimeException(new Exception("导出出错"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
