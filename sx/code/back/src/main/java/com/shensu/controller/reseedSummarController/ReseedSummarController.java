package com.shensu.controller.reseedSummarController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.reseedSummar.StaticInforInspectionBean;
import com.shensu.mybatis.entity.reseedSummar.StaticInforInspectionBeanOne;
import com.shensu.mybatis.entity.studentInput.ExcelImportResult;
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
import java.util.List;
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

            String vaccineName = info.get("vaccineName").toString();
            if ("水痘疫苗".equals(vaccineName)) {
                excelName = "StaticInforInspectionVARExcel";
            } else if ("流感疫苗".equals(vaccineName)) {
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


    /**
     * 列表查询
     */
    @RequestMapping(value = "/queryListForOne", method = RequestMethod.POST)
    public Object queryListForOne(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            String areaCode = info.get("areaCode").toString();
            String areaLevel = info.get("areaLevel").toString();
            String schId = info.get("schId").toString();
            String schType = info.get("schType").toString();
            String year = info.get("year").toString();
            String grade = info.get("grade").toString();
            String round = info.get("round").toString();
            //增加班级选项
            String className = info.get("className").toString();

            String isStation = info.get("isStation").toString();

            List<StaticInforInspectionBeanOne> beans = reseedSummarService.queryListOne(areaCode,areaLevel,schId,schType,year,grade,round,isStation,className);
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
    @RequestMapping(value = "/outputForOne")
    public void outputForOne(String params, HttpServletResponse response) {
        Map<String, Object> info = Sm4Util.getParams(params);
        ExcelUtil excelUtil = new ExcelUtil();
        Resource resource = new ClassPathResource("/excel/reseedSummar");
        try {
            String excelName = null;
            String isStation = info.get("isStation").toString();
            String areaLevelSelect=info.get("areaLevelSelect").toString();
            String areaLevel = info.get("areaLevel").toString();
            String schId = info.get("schId").toString();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            if("4".equals(areaLevel) && areaLevelSelect.equals("4")){
                if ("0".equals(isStation)) {
                    excelName = "StaticInforInspectionStationExcel";
                }
            }else{
                //判断 如果是 登陆不是4 看看isStation是多少
                if("0".equals(isStation)){
                    //看看是否选择了学校
                    if(StringUtils.isNotEmpty(schId)){
                        excelName = "StaticInforInspectionExcelSelectSchool";
                    }else{
                        excelName = "StaticInforInspectionExcel";
                    }
                }else{
                    excelName = "StaticInforInspectionExcel";
                }
            }

            String fileName = "补种汇总表" + format.format(new Date());

            excelName = new String(excelName.getBytes(), StandardCharsets.ISO_8859_1);
            if (!excelUtil.readFromExcel(resource.getFilename(), excelName)) {
                throw new RuntimeException("找不到模板文件");
            }
            String areaCode = info.get("areaCode").toString();
            String schType = info.get("schType").toString();
            String year = info.get("year").toString();
            String grade = info.get("grade").toString();
            String round = info.get("round").toString();
            //增加班级选项
            String className = info.get("className").toString();
            List<StaticInforInspectionBeanOne> beans = reseedSummarService.queryListOne(areaCode,areaLevel,schId,schType,year,grade,round,isStation,className);

            // 导出
            String areaName = info.get("areaName").toString();
            String schName = info.get("schName").toString();

            String fullAreaName = areaName;
            reseedSummarService.outputOne(excelUtil,beans,fullAreaName,schType,schName,year,grade,round,isStation,className,areaLevelSelect);
            if (!excelUtil.writeExcelToResponse(response, new String(fileName.getBytes("GBK"), StandardCharsets.ISO_8859_1))) {
                throw new RuntimeException(new Exception("导出出错"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
