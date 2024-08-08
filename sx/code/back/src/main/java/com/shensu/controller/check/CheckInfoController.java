package com.shensu.controller.check;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.check.AreaCdcUserVacData;
import com.shensu.mybatis.entity.check.ReplantVacStatBean;
import com.shensu.mybatis.entity.check.Student;
import com.shensu.mybatis.entity.check.StudentBase;
import com.shensu.mybatis.entity.inforInspection.InforReplantMess;
import com.shensu.service.check.CheckInfoService;
import com.shensu.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName CheckInfoController.java
 * @Description 查验接种信息
 * @createTime 2023年04月04日 15:08
 */
@Api(tags="查验接种信息")
@RestController
@RequestMapping("checkinfo")
public class CheckInfoController {

    private static final Logger logger = LoggerFactory.getLogger(CheckInfoController.class);

    @Autowired
    private CheckInfoService checkInfoService;

    //查询查验接种
    //选择地区、学校类型、学校名称、学年、轮次、年级、班级、是否种全、查验状态、儿童姓名、身份证件号码、编码
    @Operation(
            summary = "查验接种信息查询",
            description = "支持对已添加或导入成功的学生进行查询查验情况，疾控端只能查询"
    )
    @RequestMapping(value = "queryList", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaLevel", value = "区域层级", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "areaCode", value = "区域编码", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "schType", value = "学校类型", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "schId", value = "学校主键", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "year", value = "学年", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "rounds", value = "轮次", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "grade", value = "年级", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "class", value = "班级", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "isjz", value = "是否种全", dataTypeClass = Integer.class, paramType = "query"),
            @ApiImplicitParam(name = "status", value = "查验状态", dataTypeClass = Integer.class, paramType = "query"),
            @ApiImplicitParam(name = "name", value = "儿童姓名", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "idcard", value = "身份证件号码", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "code", value = "儿童编码", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", dataTypeClass = Integer.class, paramType = "query", required = true)
    })
    public MultipleRespBody<StudentBase> queryList(@ApiIgnore String params, HttpServletResponse response) {
        MultipleRespBody<StudentBase> result = new MultipleRespBody<>();
        result.setSuccess(true);
        Calendar start = Calendar.getInstance();
        try {
            JSONObject info = Sm4Util.getParameters(params);
            //System.out.println(info.toJSONString());
            Integer level = info.getInteger("areaLevel");
            String areaCode = info.getString("areaCode");
            String schType = info.getString("schType");
            String schId = info.getString("schId");
            String year = info.getString("year");
            String rounds = info.getString("rounds");
            String grade = info.getString("grade");
            String clazz = info.getString("class");
            String isjz = info.getString("isjz");
            String status = info.getString("status");
            String name = info.getString("name");
            String idcard = info.getString("idcard");
            String code = info.getString("code");
            Integer pageNum = info.getInteger("pageNum");
            Integer pageSize = info.getInteger("pageSize");

            PageInfo<StudentBase> page = checkInfoService.getList(level, areaCode, schType, schId, year, rounds, grade, clazz, isjz, status, name, idcard, code, pageNum, pageSize);

            result.setData(page);
        } catch (Exception e) {
            result.setPopMsg(Constants.FAILMESSAGE);
            result.setSuccess(false);
            result.setCode(Constants.ResultFailCode);
            logger.error(e.getMessage(), e);
        }
        Calendar end = Calendar.getInstance();
        double sec = (end.getTimeInMillis() - start.getTimeInMillis()) / 1000.0;
        result.setSec(sec);
        TextTemplateUtil.responseStatus(result, response);
        return result;
    }

    /**
     * 查验接种信息导出
     */
    @Operation(
            summary = "查验接种信息导出",
            description = "支持对已添加或导入成功的学生进行查询查验情况，疾控端只能查询"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaLevel", value = "区域层级", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "areaName", value = "区域名称", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "areaCode", value = "区域编码", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "schType", value = "学校类型", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "schName", value = "学校名称", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "schId", value = "学校主键", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "year", value = "学年", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "rounds", value = "轮次", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "grade", value = "年级", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "class", value = "班级", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "isjz", value = "是否种全", dataTypeClass = Integer.class, paramType = "query"),
            @ApiImplicitParam(name = "status", value = "查验状态", dataTypeClass = Integer.class, paramType = "query"),
            @ApiImplicitParam(name = "name", value = "儿童姓名", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "idcard", value = "身份证件号码", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "code", value = "儿童编码", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", dataTypeClass = Integer.class, paramType = "query", required = true)
    })
    @RequestMapping(value = "/output", method = {RequestMethod.GET})
    public void output(@ApiIgnore String params, HttpServletResponse response) {
        ExcelUtil excelUtil = new ExcelUtil();
        Resource resource = new ClassPathResource("/excel/check");
        try {
            String excelName = "checkinfo_export";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String fileName = "查验接种信息" + format.format(new Date());

            excelName = new String(excelName.getBytes(), StandardCharsets.ISO_8859_1);
            if (!excelUtil.readFromExcel(resource.getFilename(), excelName)) {
                throw new RuntimeException("找不到模板文件");
            }
            JSONObject info = Sm4Util.getParameters(params);
            //System.out.println(info.toJSONString());
            Integer level = info.getInteger("areaLevel");
            String areaCode = info.getString("areaCode");
            String schType = info.getString("schType");
            String schId = info.getString("schId");
            String year = info.getString("year");
            String rounds = info.getString("rounds");
            String grade = info.getString("grade");
            String clazz = info.getString("class");
            String isjz = info.getString("isjz");
            String status = info.getString("status");
            String name = info.getString("name");
            String idcard = info.getString("idcard");
            String code = info.getString("code");
            Integer pageNum = info.getInteger("pageNum");
            Integer pageSize = Constants.EXPORTPAGESIZE;
            PageInfo<StudentBase> pageInfo = checkInfoService.getList(level, areaCode, schType, schId, year, rounds, grade, clazz, isjz, status, name, idcard, code, pageNum, pageSize);
            List<StudentBase> list = pageInfo.getList();
            checkInfoService.outPut(excelUtil, info, list);
            if (!excelUtil.writeExcelToResponse(response, new String(fileName.getBytes("GBK"), StandardCharsets.ISO_8859_1))) {
                throw new RuntimeException(new Exception("导出出错"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    //单个或批量删除
    @Operation(
            summary = "单个信息查询",
            description = "单个信息查询,传递主键,使用英文','分隔"
    )
    @RequestMapping(value = "query", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rowkey", value = "主键", dataTypeClass = String.class, paramType = "query", required = true)
    })
    public CommonRespBody query(@ApiIgnore @RequestParam String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            JSONObject info = Sm4Util.getParameters(params);
            String rowkey = info.getString("rowkey");
            StudentBase student = checkInfoService.getOneById(rowkey);
            object.put("data", student);
        } catch (Exception e) {
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
        return com;
    }

    //单个查验和批量查验
    @Operation(
            summary = "根据主键批量查验",
            description = "单个或批量查验,传递单个或多个主键,使用英文','分隔"
    )
    @RequestMapping(value = "check", method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rowkey", value = "主键", dataTypeClass = String.class, paramType = "query", allowMultiple = true, required = true)
    })
    public CommonRespBody check(@ApiIgnore @RequestParam String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            JSONObject info = Sm4Util.getParameters(params);
            String rowkeys = info.getString("rowkey");
            List<String> paramList = StrUtil.split(rowkeys, ",");
            List<Long> rowkeyList = paramList.stream().map(Long::valueOf).collect(Collectors.toList());
            Integer rows = checkInfoService.checkByRowkey(rowkeyList);
            object.put("data", rows);
        } catch (Exception e) {
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
        return com;
    }

    //通过个案编码，单个查验和批量查验
    @Operation(
            summary = "根据个案编码批量查验",
            description = "单个或批量查验，传递单个或多个个案编码"
    )
    @RequestMapping(value = "checkByChildno", method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "childno", value = "个案编码", dataTypeClass = String.class, paramType = "query", allowMultiple = true, required = true)
    })
    public CommonRespBody checkByChildno(@ApiIgnore @RequestParam String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            JSONObject info = Sm4Util.getParameters(params);
            String childnos = info.getString("childno");
            List<String> childnoList = StrUtil.split(childnos, ",");
            Integer rows = checkInfoService.checkByChildno(childnoList);
            object.put("data", rows);
        } catch (Exception e) {
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
        return com;
    }

    //单个或批量删除
    @Operation(
            summary = "单个、批量删除",
            description = "单个或批量删除，传递单个或多个主键"
    )
    @RequestMapping(value = "delete", method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rowkey", value = "主键", dataTypeClass = String.class, paramType = "query", allowMultiple = true, required = true)
    })
    public CommonRespBody delete(@ApiIgnore @RequestParam String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            JSONObject info = Sm4Util.getParameters(params);
            String rowkeys = info.getString("rowkey");
            List<String> rowkeyList = StrUtil.split(rowkeys, ",");
            Integer rows = checkInfoService.batchDeleteById(rowkeyList);
            object.put("data", rows);
        } catch (Exception e) {
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
        return com;
    }

    //批量编辑保存(学年，年级，班级，轮次)
    @Operation(
            summary = "批量编辑保存",
            description = "批量编辑保存"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rowkey", value = "主键", dataTypeClass = String.class, paramType = "query", allowMultiple = true, required = true),
            @ApiImplicitParam(name = "year", value = "学年", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "grade", value = "年级", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "class", value = "班级名称", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "rounds", value = "轮次", dataTypeClass = String.class, paramType = "query", required = true)
    })
    @RequestMapping(value = "save4MultipleEdit", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public CommonRespBody save4MultipleEdit(@ApiIgnore @RequestParam String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            JSONObject info = Sm4Util.getParameters(params);
            String year = info.getString("year");
            String grade = info.getString("grade");
            String clazz = info.getString("class");
            String rounds = info.getString("rounds");
            String rowkeys = info.getString("rowkey");
            List<String> rowkeyList = StrUtil.split(rowkeys, ",");
            //year学年, grade年级, clazz班级，三者组合应该具有唯一性
            Integer rows = checkInfoService.batchUpdate(rowkeyList, year, grade, clazz, rounds);
            object.put("data", rows);
        } catch (Exception e) {
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
        return com;
    }


    //需补种名单列表
    //选择地区、学校类型、学校名称、学年、轮次、年级、班级、是否种全、查验状态、儿童姓名、身份证件号码、编码
    @Operation(
            summary = "需补种名单列表",
            description = "查询的是当前时间点所有需补种的人,该功能接种单位和疾控均能看到"
    )
    @RequestMapping(value = "queryReplantRoster", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaLevel", value = "区域层级", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "areaCode", value = "区域编码", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "schType", value = "学校类型", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "schId", value = "学校主键", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "year", value = "学年", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "rounds", value = "轮次", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "grade", value = "年级", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", dataTypeClass = Integer.class, paramType = "query", required = true)
    })
    public MultipleRespBody<Student> queryReplantRoster(@ApiIgnore String params, HttpServletResponse response) {
        MultipleRespBody<Student> result = new MultipleRespBody<>();
        result.setSuccess(true);
        Calendar start = Calendar.getInstance();
        try {
            JSONObject info = Sm4Util.getParameters(params);
            Integer level = info.getInteger("areaLevel");
            String areaCode = info.getString("areaCode");
            String schType = info.getString("schType");
            String schId = info.getString("schId");
            String year = info.getString("year");
            String rounds = info.getString("rounds");
            String grade = info.getString("grade");
            String  className = info.getString("className");
            Integer pageNum = info.getInteger("pageNum");
            Integer pageSize = info.getInteger("pageSize");
            PageInfo<Student> page = checkInfoService.getReplantRosterPage(level, areaCode, schType, schId, year, rounds, grade,className, pageNum, pageSize);
            result.setData(page);
        } catch (Exception e) {
            result.setPopMsg(Constants.FAILMESSAGE);
            result.setSuccess(false);
            result.setCode(Constants.ResultFailCode);
            logger.error(e.getMessage(), e);
        }
        Calendar end = Calendar.getInstance();
        double sec = (end.getTimeInMillis() - start.getTimeInMillis()) / 1000.0;
        result.setSec(sec);
        TextTemplateUtil.responseStatus(result, response);
        return result;
    }


    /**
     * 需补种名单列表导出
     * @param params
     * @param response
     */
    @Operation(
            summary = "需补种名单列表导出",
            description = "查询的是当前时间点所有需补种的人,该功能接种单位和疾控均能看到"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaLevel", value = "区域层级", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "areaName", value = "区域名称", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "areaCode", value = "区域编码", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "schType", value = "学校类型", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "schId", value = "学校主键", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "schName", value = "学校名称", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "year", value = "学年", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "rounds", value = "轮次", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "grade", value = "年级", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", dataTypeClass = Integer.class, paramType = "query", required = true)
    })
    @RequestMapping(value = "/outputReplantRoster", method = {RequestMethod.GET})
    public void outputReplantRoster(@ApiIgnore String params, HttpServletResponse response) {
        ExcelUtil excelUtil = new ExcelUtil();
        Resource resource = new ClassPathResource("/excel/check");
        try {
            String excelName = "replant_roster_export";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String fileName = "需补种名单列表" + format.format(new Date());

            excelName = new String(excelName.getBytes(), StandardCharsets.ISO_8859_1);
            if (!excelUtil.readFromExcel(resource.getFilename(), excelName)) {
                throw new RuntimeException("找不到模板文件");
            }
            JSONObject info = Sm4Util.getParameters(params);
            Integer level = info.getInteger("areaLevel");
            String areaCode = info.getString("areaCode");
            String schType = info.getString("schType");
            String schId = info.getString("schId");
            String year = info.getString("year");
            String rounds = info.getString("rounds");
            String grade = info.getString("grade");
            String  className = info.getString("className");

            Integer pageNum = info.getInteger("pageNum");
            Integer pageSize = Constants.EXPORTPAGESIZE;
            PageInfo<Student> page = checkInfoService.getReplantRosterPage(level, areaCode, schType, schId, year, rounds, grade,className, pageNum, pageSize);
            List<Student> list = page.getList();
            checkInfoService.outPutReplantRoster(excelUtil, info, list);
            if (!excelUtil.writeExcelToResponse(response, new String(fileName.getBytes("GBK"), StandardCharsets.ISO_8859_1))) {
                throw new RuntimeException(new Exception("导出出错"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    //补种疫苗统计表
    //选择地区（采用树图，最低到接种单位，接种单位默认本单位），学校类型，学年，轮次，出生日期，补种日期，是否完成补种
    @Operation(
            summary = "补种疫苗统计表",
            description = "支持统计所在辖区内各疫苗需补种剂次数、补种剂次数和补种率情况"
    )
    @RequestMapping(value = "queryReplantVacStat", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaLevel", value = "区域层级", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "areaCode", value = "区域编码", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "schType", value = "学校类型", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "year", value = "学年", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "rounds", value = "轮次", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "birthStart", value = "出生日期开始", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "birthEnd", value = "出生日期结束", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "replantStart", value = "补种日期开始", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "replantEnd", value = "补种日期结束", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "isFull", value = "是否完成补种", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", dataTypeClass = Integer.class, paramType = "query", required = true)
    })
    public MultipleRespBody<JSONObject> queryReplantVacStat(@ApiIgnore String params, HttpServletResponse response) {
        MultipleRespBody<JSONObject> result = new MultipleRespBody<>();
        result.setSuccess(true);
        Calendar start = Calendar.getInstance();
        try {
            JSONObject info = Sm4Util.getParameters(params);
            Integer level = info.getInteger("areaLevel");
            String areaCode = info.getString("areaCode");
            String schType = info.getString("schType");
            //新增学校信息
            String schId = info.getString("schId");
            String grade =info.getString("grade");
            String className =info.getString("className");

            String year = info.getString("year");
            String rounds = info.getString("rounds");
            String birthStart = info.getString("birthStart");
            String birthEnd = info.getString("birthEnd");
            String replantStart = info.getString("replantStart");
            String replantEnd = info.getString("replantEnd");

            String isFull = info.getString("isFull"); // 删除不做
            PageInfo<JSONObject> page = checkInfoService.getReplantVaccinumStatistics(level, areaCode, schType, year, rounds, birthStart, birthEnd, replantStart, replantEnd, isFull,schId,grade,className);
            result.setData(page);
        } catch (Exception e) {
            result.setPopMsg(Constants.FAILMESSAGE);
            result.setSuccess(false);
            result.setCode(Constants.ResultFailCode);
            logger.error(e.getMessage(), e);
        }
        Calendar end = Calendar.getInstance();
        double sec = (end.getTimeInMillis() - start.getTimeInMillis()) / 1000.0;
        result.setSec(sec);
        TextTemplateUtil.responseStatus(result, response);
        return result;
    }

    @Operation(
            summary = "补种疫苗统计表导出",
            description = "支持统计所在辖区内各疫苗需补种剂次数、补种剂次数和补种率情况"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaLevel", value = "区域层级", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "areaName", value = "区域名称", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "areaCode", value = "区域编码", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "schType", value = "学校类型", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "schId", value = "学校主键", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "schName", value = "学校名称", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "year", value = "学年", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "rounds", value = "轮次", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "birthStart", value = "出生日期开始", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "birthEnd", value = "出生日期结束", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "replantStart", value = "补种日期开始", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "replantEnd", value = "补种日期结束", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "isFull", value = "是否完成补种", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", dataTypeClass = Integer.class, paramType = "query", required = true)
    })
    @RequestMapping(value = "/outputReplantVacStat", method = {RequestMethod.GET})
    public void outputReplantVacStat(@ApiIgnore String params, HttpServletResponse response) {
        ExcelUtil excelUtil = new ExcelUtil();
        Resource resource = new ClassPathResource("/excel/check");
        try {
            String excelName = "replant_vacstat_export";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String fileName = "补种疫苗统计表" + format.format(new Date());

            excelName = new String(excelName.getBytes(), StandardCharsets.ISO_8859_1);
            if (!excelUtil.readFromExcel(resource.getFilename(), excelName)) {
                throw new RuntimeException("找不到模板文件");
            }
            JSONObject info = Sm4Util.getParameters(params);
            Integer level = info.getInteger("areaLevel");
            String areaCode = info.getString("areaCode");
            String schType = info.getString("schType");
            String year = info.getString("year");
            String rounds = info.getString("rounds");
            String birthStart = info.getString("birthStart");
            String birthEnd = info.getString("birthEnd");
            String replantStart = info.getString("replantStart");
            String replantEnd = info.getString("replantEnd");

            //新增学校信息
            String schId = info.getString("schId");
            String grade =info.getString("grade");
            String className = info.getString("className");

            String isFull = info.getString("isFull");
            ReplantVacStatBean statBean = checkInfoService.getReplantVaccinumStat(level, areaCode, schType, year, rounds, birthStart, birthEnd, replantStart, replantEnd, isFull,schId,grade,className);
            checkInfoService.outputReplantVacStat(excelUtil, info, statBean);
            if (!excelUtil.writeExcelToResponse(response, new String(fileName.getBytes("GBK"), StandardCharsets.ISO_8859_1))) {
                throw new RuntimeException(new Exception("导出出错"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    //接种证查验汇总表(疾控门诊均可见，不分剂次)
    //选择地区（采用树图，最低到接种单位，接种单位默认本单位），学校类型，学年，轮次，出生日期，补种日期，是否完成补种
    @Operation(
            summary = "接种证查验汇总表",
            description = "支持按区域、接种门诊以及学校统计补种汇总情况(疾控门诊均可见，不分剂次)"
    )
    @RequestMapping(value = "queryVacCertCheckStat", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaLevel", value = "区域层级", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "areaCode", value = "区域编码", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "schType", value = "学校类型", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "year", value = "学年", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "rounds", value = "轮次", dataTypeClass = Integer.class, paramType = "query", required = true)
    })
    public MultipleRespBody<AreaCdcUserVacData> queryVacCertCheckStat(@ApiIgnore String params, HttpServletResponse response) {
        MultipleRespBody<AreaCdcUserVacData> result = new MultipleRespBody<>();
        result.setSuccess(true);
        Calendar start = Calendar.getInstance();
        try {
            JSONObject info = Sm4Util.getParameters(params);
            Integer level = info.getInteger("areaLevel");
            String areaCode = info.getString("areaCode");
            String schType = info.getString("schType");
            String year = info.getString("year");
            String rounds = info.getString("rounds");
            PageInfo<AreaCdcUserVacData> page = checkInfoService.getVacCertCheckStat(level, areaCode, schType, year, rounds);
            result.setData(page);
        } catch (Exception e) {
            result.setPopMsg(Constants.FAILMESSAGE);
            result.setSuccess(false);
            result.setCode(Constants.ResultFailCode);
            logger.error(e.getMessage(), e);
        }
        Calendar end = Calendar.getInstance();
        double sec = (end.getTimeInMillis() - start.getTimeInMillis()) / 1000.0;
        result.setSec(sec);
        TextTemplateUtil.responseStatus(result, response);
        return result;
    }

    /**
     * 接种证查验汇总表(疾控门诊均可见，不分剂次)
     * @param params
     * @param response
     */
    @Operation(
            summary = "接种证查验汇总表导出",
            description = "支持按区域、接种门诊以及学校统计补种汇总情况(疾控门诊均可见，不分剂次)"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaLevel", value = "区域层级", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "areaName", value = "区域名称", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "areaCode", value = "区域编码", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "schType", value = "学校类型", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "year", value = "学年", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "rounds", value = "轮次", dataTypeClass = Integer.class, paramType = "query", required = true)
    })
    @RequestMapping(value = "/outputVacCertCheckStat", method = {RequestMethod.GET})
    public void outputVacCertCheckStat(@ApiIgnore String params, HttpServletResponse response) {
        ExcelUtil excelUtil = new ExcelUtil();
        Resource resource = new ClassPathResource("/excel/check");
        try {
            String excelName = "vac_certcheck_export";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String fileName = "接种证查验情况汇总表" + format.format(new Date());

            excelName = new String(excelName.getBytes(), StandardCharsets.ISO_8859_1);
            if (!excelUtil.readFromExcel(resource.getFilename(), excelName)) {
                throw new RuntimeException("找不到模板文件");
            }
            JSONObject info = Sm4Util.getParameters(params);
            Integer level = info.getInteger("areaLevel");
            String areaCode = info.getString("areaCode");
            String schType = info.getString("schType");
            String year = info.getString("year");
            String rounds = info.getString("rounds");
            PageInfo<AreaCdcUserVacData> page = checkInfoService.getVacCertCheckStat(level, areaCode, schType, year, rounds);
            List<AreaCdcUserVacData> list = page.getList();
            checkInfoService.outputVacCertCheckStat(excelUtil, info, list);
            if (!excelUtil.writeExcelToResponse(response, new String(fileName.getBytes("GBK"), StandardCharsets.ISO_8859_1))) {
                throw new RuntimeException(new Exception("导出出错"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 打印补种通知单
     */
    @Operation(
            summary = "打印补种通知单",
            description = "打印补种通知单"
    )
    @RequestMapping(value = "queryReplantMess", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rowkey", value = "主键", dataTypeClass = String.class, paramType = "query", allowMultiple = true),
            @ApiImplicitParam(name = "areaLevel", value = "区域层级", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "areaCode", value = "区域编码", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "schType", value = "学校类型", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "schId", value = "学校主键", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "year", value = "学年", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "rounds", value = "轮次", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "grade", value = "年级", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "class", value = "班级", dataTypeClass = String.class, paramType = "query"),
            //@ApiImplicitParam(name = "isjz", value = "是否种全", dataTypeClass = Integer.class, paramType = "query"),
            //@ApiImplicitParam(name = "status", value = "查验状态", dataTypeClass = Integer.class, paramType = "query"),
            @ApiImplicitParam(name = "name", value = "儿童姓名", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "idcard", value = "身份证件号码", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "code", value = "儿童编码", dataTypeClass = String.class, paramType = "query")
    })
    public MultipleRespBody<InforReplantMess> queryReplantMess(@ApiIgnore String params, HttpServletResponse response) {
        MultipleRespBody<InforReplantMess> result = new MultipleRespBody<>();
        result.setSuccess(true);
        Calendar start = Calendar.getInstance();
        try {
            JSONObject info = Sm4Util.getParameters(params);
            Integer level = info.getInteger("areaLevel");
            String areaCode = info.getString("areaCode");
            String schType = info.getString("schType");
            String schId = info.getString("schId");
            String year = info.getString("year");
            String rounds = info.getString("rounds");
            String grade = info.getString("grade");
            String clazz = info.getString("class");
            //String isjz = info.getString("isjz");
            //String status = info.getString("status");
            String name = info.getString("name");
            String idcard = info.getString("idcard");
            String code = info.getString("code");
            String rowkeys = info.getString("rowkey");
            List<String> rowkeyList = StrUtil.split(rowkeys, ",");
            PageInfo<InforReplantMess> page = checkInfoService.queryReplateMess(level, areaCode, schType, schId, year, rounds, grade, clazz, null, null, null, name, idcard, code, rowkeyList);
            result.setData(page);
        } catch (Exception e) {
            result.setPopMsg(Constants.FAILMESSAGE);
            result.setSuccess(false);
            result.setCode(Constants.ResultFailCode);
            logger.error(e.getMessage(), e);
        }
        Calendar end = Calendar.getInstance();
        double sec = (end.getTimeInMillis() - start.getTimeInMillis()) / 1000.0;
        result.setSec(sec);
        TextTemplateUtil.responseStatus(result, response);
        return result;
    }

    /**
     * 打印补证通知单
     */
    @Operation(
            summary = "打印补证通知单",
            description = "打印补证通知单"
    )
    @RequestMapping(value = "queryReplantCard", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rowkey", value = "主键", dataTypeClass = String.class, paramType = "query", allowMultiple = true),
            @ApiImplicitParam(name = "areaLevel", value = "区域层级", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "areaCode", value = "区域编码", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "schType", value = "学校类型", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "schId", value = "学校主键", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "year", value = "学年", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "rounds", value = "轮次", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "grade", value = "年级", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "class", value = "班级", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "name", value = "儿童姓名", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "idcard", value = "身份证件号码", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "code", value = "儿童编码", dataTypeClass = String.class, paramType = "query")
    })
    public MultipleRespBody<InforReplantMess> queryReplantCard(@ApiIgnore String params, HttpServletResponse response) {
        MultipleRespBody<InforReplantMess> result = new MultipleRespBody<>();
        result.setSuccess(true);
        Calendar start = Calendar.getInstance();
        try {
            JSONObject info = Sm4Util.getParameters(params);
            Integer level = info.getInteger("areaLevel");
            String areaCode = info.getString("areaCode");
            String schType = info.getString("schType");
            String schId = info.getString("schId");
            String year = info.getString("year");
            String rounds = info.getString("rounds");
            String grade = info.getString("grade");
            String clazz = info.getString("class");
            //String isjz = info.getString("isjz");
            //String status = info.getString("status");
            String name = info.getString("name");
            String idcard = info.getString("idcard");
            String code = info.getString("code");
            String rowkeys = info.getString("rowkey");
            List<String> rowkeyList = StrUtil.split(rowkeys, ",");
            PageInfo<InforReplantMess> page = checkInfoService.queryReplantCard(level, areaCode, schType, schId, year, rounds, grade, clazz, null, null, null, name, idcard, code, rowkeyList);
            result.setData(page);
        } catch (Exception e) {
            result.setPopMsg(Constants.FAILMESSAGE);
            result.setSuccess(false);
            result.setCode(Constants.ResultFailCode);
            logger.error(e.getMessage(), e);
        }
        Calendar end = Calendar.getInstance();
        double sec = (end.getTimeInMillis() - start.getTimeInMillis()) / 1000.0;
        result.setSec(sec);
        TextTemplateUtil.responseStatus(result, response);
        return result;
    }

    /**
     * 打印建档通知单
     */
    @Operation(
            summary = "打印建档通知单",
            description = "打印建档通知单"
    )
    @RequestMapping(value = "queryNoArchive", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rowkey", value = "主键", dataTypeClass = String.class, paramType = "query", allowMultiple = true),
            @ApiImplicitParam(name = "areaLevel", value = "区域层级", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "areaCode", value = "区域编码", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "schType", value = "学校类型", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "schId", value = "学校主键", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "year", value = "学年", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "rounds", value = "轮次", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "grade", value = "年级", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "class", value = "班级", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "name", value = "儿童姓名", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "idcard", value = "身份证件号码", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "code", value = "儿童编码", dataTypeClass = String.class, paramType = "query")
    })
    public MultipleRespBody<InforReplantMess> queryNoArchive(@ApiIgnore String params, HttpServletResponse response) {
        MultipleRespBody<InforReplantMess> result = new MultipleRespBody<>();
        result.setSuccess(true);
        Calendar start = Calendar.getInstance();
        try {
            JSONObject info = Sm4Util.getParameters(params);
            Integer level = info.getInteger("areaLevel");
            String areaCode = info.getString("areaCode");
            String schType = info.getString("schType");
            String schId = info.getString("schId");
            String year = info.getString("year");
            String rounds = info.getString("rounds");
            String grade = info.getString("grade");
            String clazz = info.getString("class");
            String name = info.getString("name");
            String idcard = info.getString("idcard");
            String code = info.getString("code");
            String rowkeys = info.getString("rowkey");
            List<String> rowkeyList = StrUtil.split(rowkeys, ",");
            PageInfo<InforReplantMess> page = checkInfoService.queryNoArchive(level, areaCode, schType, schId, year, rounds, grade, clazz, null, null, null, name, idcard, code, rowkeyList);
            result.setData(page);
        } catch (Exception e) {
            result.setPopMsg(Constants.FAILMESSAGE);
            result.setSuccess(false);
            result.setCode(Constants.ResultFailCode);
            logger.error(e.getMessage(), e);
        }
        Calendar end = Calendar.getInstance();
        double sec = (end.getTimeInMillis() - start.getTimeInMillis()) / 1000.0;
        result.setSec(sec);
        TextTemplateUtil.responseStatus(result, response);
        return result;
    }


    /**
     * 打印查验证明
     */
    @Operation(
            summary = "打印查验证明",
            description = "打印查验证明"
    )
    @RequestMapping(value = "queryCheckCert", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rowkey", value = "主键", dataTypeClass = String.class, paramType = "query", allowMultiple = true),
            @ApiImplicitParam(name = "areaLevel", value = "区域层级", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "areaCode", value = "区域编码", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "schType", value = "学校类型", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "schId", value = "学校主键", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "year", value = "学年", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "rounds", value = "轮次", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "grade", value = "年级", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "class", value = "班级", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "name", value = "儿童姓名", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "idcard", value = "身份证件号码", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "code", value = "儿童编码", dataTypeClass = String.class, paramType = "query")
    })
    public MultipleRespBody<InforReplantMess> queryCheckCert(@ApiIgnore String params, HttpServletResponse response) {
        MultipleRespBody<InforReplantMess> result = new MultipleRespBody<>();
        result.setSuccess(true);
        Calendar start = Calendar.getInstance();
        try {
            JSONObject info = Sm4Util.getParameters(params);
            Integer level = info.getInteger("areaLevel");
            String areaCode = info.getString("areaCode");
            String schType = info.getString("schType");
            String schId = info.getString("schId");
            String year = info.getString("year");
            String rounds = info.getString("rounds");
            String grade = info.getString("grade");
            String clazz = info.getString("class");
            String name = info.getString("name");
            String idcard = info.getString("idcard");
            String code = info.getString("code");
            String rowkeys = info.getString("rowkey");
            List<String> rowkeyList = StrUtil.split(rowkeys, ",");
            PageInfo<InforReplantMess> page = checkInfoService.queryCheckCert(level, areaCode, schType, schId, year, rounds, grade, clazz, null, null, null, name, idcard, code, rowkeyList);
            result.setData(page);
        } catch (Exception e) {
            result.setPopMsg(Constants.FAILMESSAGE);
            result.setSuccess(false);
            result.setCode(Constants.ResultFailCode);
            logger.error(e.getMessage(), e);
        }
        Calendar end = Calendar.getInstance();
        double sec = (end.getTimeInMillis() - start.getTimeInMillis()) / 1000.0;
        result.setSec(sec);
        TextTemplateUtil.responseStatus(result, response);
        return result;
    }

    //单个保存
   /* @Deprecated
    @Operation(
            summary = "单个学生信息保存",
            description = "单个保存"
    )
    @RequestMapping(value = "save", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public CommonRespBody save(@ApiIgnore @RequestParam String params,
                               @ApiParam(name="学生信息对象", value="传入json格式") StudentBase student,
                               HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            JSONObject info = Sm4Util.getParameters(params);
            StudentBase stuinfo = JSON.to(StudentBase.class, info);



            //object.put("data", rowkeyList);
        } catch (Exception e) {
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
        return com;
    }*/

}
