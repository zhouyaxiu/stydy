package com.shensu.controller.StudentInput;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.studentInput.ExcelImportResult;
import com.shensu.service.check.CheckInfoService;
import com.shensu.service.studentInputService.BatchExcelService;
import com.shensu.util.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.apache.poi.ss.usermodel.CellType.STRING;

/**
 * @author zcy
 * @ClassName BatchExcelController
 * @Description Excel批量导入
 * @createTime 2023/4/12
 */
@RestController
@RequestMapping("batchExcel")
public class BatchExcelController {
    private Logger logger = LoggerFactory.getLogger(BatchExcelController.class);

    @Autowired
    private BatchExcelService batchExcelService;
    @Autowired
    private CheckInfoService checkInfoService;

    /**
     * excel批量导入模板下载
     */
    @RequestMapping(value = "/downLoadImportMould")
    public void dowmLoadInfoCollectMould(HttpServletResponse response) {
        ExcelUtil excelUtil = new ExcelUtil();
        Resource resource = new ClassPathResource("/excel/studentInput");
        try {
            String excelName = null;
            String fileName = null;
            // Excel导入模板下载
            excelName = "ExcelBatchImport_module";
            fileName = "Excel批量导入";
            excelName = new String(excelName.getBytes(), StandardCharsets.ISO_8859_1);
            if (!excelUtil.readFromExcel(resource.getFilename(), excelName)) {
                throw new RuntimeException("找不到模板文件");
            }

            if (!excelUtil.writeExcelToResponse(response, new String(fileName.getBytes("GBK"), StandardCharsets.ISO_8859_1))) {
                throw new RuntimeException(new Exception("导出出错"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Excel导入
     */
    @RequestMapping(value = "/excelImport", method = RequestMethod.POST)
    public Object excelImport(@RequestParam(name = "file") MultipartFile files, @RequestParam(name = "params") String params, HttpServletResponse response) {
        ExcelUtil excelUtil = new ExcelUtil();
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            // 判断文件是否为空
            if (files == null || files.isEmpty()) {
                object.put("message", "请上传文件！");
            } else {
                Map<String, Object> info = Sm4Util.getParams(params);
                org.springframework.core.io.Resource resource = files.getResource();
                excelUtil.readFromExcel(resource,null);
                HSSFSheet sheet = excelUtil.getSheet();
                // 校验标题头
                sheet.getRow(0).getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(STRING);
                sheet.getRow(0).getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(STRING);
                sheet.getRow(0).getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(STRING);
                String title = sheet.getRow(0).getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
                if (!"身份证号码".equals(title)) {
                    object.put("message", "模板文件不正确，请使用正确的导入模板文件！");
                } else {
                    String schId = info.get("schId").toString();
                    String classId = info.get("classId").toString();
                    String className = info.get("className").toString();
                    String schStationCode = info.get("schStationCode").toString();

                    Map<String, Object> map = batchExcelService.excelImport(sheet,schId,classId,className,schStationCode);
                    object.put("message", map.get("message"));
                    object.put("code", Constants.ResultSuccessCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            com.setPopMsg(Constants.FAILMESSAGE);
            com.setSuccess(false);
            com.setCode(Constants.ResultFailCode);
            logger.error(e.getMessage(), e);
        }
        Calendar end = Calendar.getInstance();
        double sec = (end.getTimeInMillis() - start.getTimeInMillis()) / 1000.0;
        object.put("sec", sec);
        com.setData(object);
        TextTemplateUtil.responseStutas(com, response);
        return com.isSuccess() ? com.getData() : com.getPopMsg();
    }


    /**
     * 查询导入失败项
     */
    @RequestMapping(value = "/errorList", method = RequestMethod.POST)
    public Object errorList(@RequestParam(name = "params") String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            String schId = info.get("schId").toString();
            String claId = info.get("claId").toString();
            String pageNum = info.get("pageNum").toString();
            PageInfo<ExcelImportResult> results = batchExcelService.queryList(schId,claId, Constants.PAGESIZE, Integer.parseInt(pageNum));

            object.put("data", results);
        } catch (Exception e) {
            e.printStackTrace();
            com.setPopMsg(Constants.FAILMESSAGE);
            com.setSuccess(false);
            com.setCode(Constants.ResultFailCode);
            logger.error(e.getMessage(), e);
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
        Resource resource = new ClassPathResource("/excel/studentInput");
        try {
            String excelName = "excelError_export";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String fileName = "Excel批量导入失败信息汇总表" + format.format(new Date());

            excelName = new String(excelName.getBytes(), StandardCharsets.ISO_8859_1);
            if (!excelUtil.readFromExcel(resource.getFilename(), excelName)) {
                throw new RuntimeException("找不到模板文件");
            }
            String schId = info.get("schId").toString();
            String claId = info.get("claId").toString();
            String pageNum = info.get("pageNum").toString();
            PageInfo<ExcelImportResult> results = batchExcelService.queryList(schId, claId, Constants.EXPORTPAGESIZE, Integer.parseInt(pageNum));
            List<ExcelImportResult> list = results.getList();

            // 导出
            batchExcelService.outPut(excelUtil,info,list);

            if (!excelUtil.writeExcelToResponse(response, new String(fileName.getBytes("GBK"), StandardCharsets.ISO_8859_1))) {
                throw new RuntimeException(new Exception("导出出错"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
