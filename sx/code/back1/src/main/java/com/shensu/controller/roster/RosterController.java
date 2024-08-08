package com.shensu.controller.roster;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.check.Student;
import com.shensu.mybatis.entity.check.StudentBase;
import com.shensu.mybatis.entity.inforInspection.InforReplantMess;
import com.shensu.service.check.CheckInfoService;
import com.shensu.task.StaticScheduleTask;
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
import java.util.stream.Collectors;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName RosterController.java
 * @Description 花名册
 * @createTime 2023年04月04日 15:08
 */
@Api(tags="班级花名册")
@RestController
@RequestMapping("roster")
public class RosterController {

    private static final Logger logger = LoggerFactory.getLogger(RosterController.class);

    @Autowired
    private CheckInfoService checkInfoService;

    @Autowired
    private StaticScheduleTask scheduleTask;

    //班级花名册查询
    //选择地区、学校类型、学校名称、学年、轮次、年级、班级、是否种全、查验状态、儿童姓名、身份证件号码、编码
    @Operation(
            summary = "班级花名册查询",
            description = "支持对已添加或导入成功的学生进行查询查验情况"
    )
    @RequestMapping(value = "queryList", method = {RequestMethod.GET})
    @ApiImplicitParams({
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
            PageInfo<StudentBase> page = checkInfoService.getList(schId, year, rounds, grade, clazz, isjz, status, name, idcard, code, pageNum, pageSize);
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
     * 班级花名册导出
     */
    @Operation(
            summary = "班级花名册导出",
            description = "支持对已添加或导入成功的学生进行查询查验情况"
    )
    @ApiImplicitParams({
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
        Resource resource = new ClassPathResource("/excel/roster");
        try {
            String excelName = "checkinfo_export";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String fileName = "班级花名册查询" + format.format(new Date());

            excelName = new String(excelName.getBytes(), StandardCharsets.ISO_8859_1);
            if (!excelUtil.readFromExcel(resource.getFilename(), excelName)) {
                throw new RuntimeException("找不到模板文件");
            }
            JSONObject info = Sm4Util.getParameters(params);
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
            PageInfo<StudentBase> pageInfo = checkInfoService.getList(schId, year, rounds, grade, clazz, isjz, status, name, idcard, code, pageNum, pageSize);
            List<StudentBase> list = pageInfo.getList();
            checkInfoService.outPut(excelUtil, info, list);
            if (!excelUtil.writeExcelToResponse(response, new String(fileName.getBytes("GBK"), StandardCharsets.ISO_8859_1))) {
                throw new RuntimeException(new Exception("导出出错"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    //单个班级花名册查询
    @Operation(
            summary = "单个班级花名册查询",
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
            @ApiImplicitParam(name = "schId", value = "学校主键", dataTypeClass = String.class, paramType = "query", required = true),
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
            String schId = info.getString("schId");
            String year = info.getString("year");
            String rounds = info.getString("rounds");
            String grade = info.getString("grade");
            //班级
            String className = info.getString("className");

            Integer pageNum = info.getInteger("pageNum");
            Integer pageSize = info.getInteger("pageSize");
            PageInfo<Student> page = checkInfoService.getReplantRosterPage(schId, year, rounds, grade,className, pageNum, pageSize);
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
        Resource resource = new ClassPathResource("/excel/roster");
        try {
            String excelName = "replant_roster_export";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String fileName = "需补种名单列表" + format.format(new Date());

            excelName = new String(excelName.getBytes(), StandardCharsets.ISO_8859_1);
            if (!excelUtil.readFromExcel(resource.getFilename(), excelName)) {
                throw new RuntimeException("找不到模板文件");
            }
            JSONObject info = Sm4Util.getParameters(params);
            String schId = info.getString("schId");
            String year = info.getString("year");
            String rounds = info.getString("rounds");
            String grade = info.getString("grade");
            String className = info.getString("className");
            Integer pageNum = info.getInteger("pageNum");
            Integer pageSize = Constants.EXPORTPAGESIZE;
            PageInfo<Student> page = checkInfoService.getReplantRosterPage(schId, year, rounds, grade,className, pageNum, pageSize);
            List<Student> list = page.getList();
            checkInfoService.outPutReplantRoster(excelUtil, info, list);
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
            PageInfo<InforReplantMess> page = checkInfoService.queryReplateMess(schId, year, rounds, grade, clazz, null, null, null, name, idcard, code, rowkeyList);
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
            PageInfo<InforReplantMess> page = checkInfoService.queryReplantCard(schId, year, rounds, grade, clazz, null, null, null, name, idcard, code, rowkeyList);
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
            PageInfo<InforReplantMess> page = checkInfoService.queryNoArchive(schId, year, rounds, grade, clazz, null, null, null, name, idcard, code, rowkeyList);
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
     * 定时查验任务
     */
    @Operation(
            summary = "定时查验任务",
            description = "每日查验, 年度查验"
    )
    @RequestMapping(value = "runCheckTask", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "eachYear", value = "是否年度任务", dataTypeClass = String.class, paramType = "query")
    })
    public CommonRespBody runCheckTask(@ApiIgnore @RequestParam String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            JSONObject info = Sm4Util.getParameters(params);
            Boolean eachYear = info.getBoolean("eachYear");
            int count = scheduleTask.check(eachYear);
            object.put("data", count);
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

}
