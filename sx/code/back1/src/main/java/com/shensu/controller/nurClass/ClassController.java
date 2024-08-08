package com.shensu.controller.nurClass;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.nurClass.ClassInfo;
import com.shensu.service.nurClass.ClassService;
import com.shensu.util.*;
import net.sf.cglib.beans.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zcy
 * @ClassName ClassController
 * @Description
 * @createTime 2023/9/5
 */
@RestController
@RequestMapping("class")
public class ClassController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassController.class);

    @Autowired
    private ClassService classService;

    /**
     * 班级列表
     */
    @RequestMapping(value = "/classList", method = RequestMethod.POST)
    public Object classList(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            info.put("pageSize", Constants.PAGESIZE);
            PageInfo<ClassInfo> beans = classService.classList(info);
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
     * 班级新增
     */
    @RequestMapping(value = "/insertClass", method = RequestMethod.POST)
    public Object insertClass(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            ClassInfo classInfo = ClassInfo.class.newInstance();
            BeanMap beanMap = BeanMap.create(classInfo);
            beanMap.putAll(info);
            String msg = classService.insertClass(classInfo);
            object.put("msg", msg);
        } catch (Exception e) {
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


    @RequestMapping(value = "/classDetail", method = RequestMethod.POST)
    public Object getClass(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            String classId = info.get("classId").toString();
            ClassInfo aClass = classService.getClass(classId);
            object.put("data", aClass);
        } catch (Exception e) {
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
     * 班级修改
     */
    @RequestMapping(value = "/updateClass", method = RequestMethod.POST)
    public Object updateClass(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        object.put("code", Constants.ResultErrorCode);
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            String classId = info.get("classId").toString();
            String claTotal = info.get("claTotal").toString();
            String claLinkman = StringUtils.nullToString(info.get("claLinkman").toString());
            String claPhone = StringUtils.nullToString(info.get("claPhone").toString());
            String msg = classService.updateClass(classId,claTotal,claLinkman,claPhone);
            if (msg.contains("成功")) object.put("code", Constants.ResultSuccessCode);
            object.put("msg", msg);
        } catch (Exception e) {
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
     * 删除班级
     */
    @RequestMapping(value = "/deleteClass", method = RequestMethod.POST)
    public Object deleteClass(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        object.put("code", Constants.ResultErrorCode);
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            String classIds = info.get("classIds").toString();
            String msg;
            if (StringUtils.isEmpty(classIds)) {
                msg = "未选择要删除的班级！";
            } else {
                msg = classService.deleteClass(classIds);
            }
            if (msg.contains("成功")) object.put("code", Constants.ResultSuccessCode);
            object.put("msg", msg);
        } catch (Exception e) {
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


    @RequestMapping(value = "/output")
    public void output(String params, HttpServletResponse response) {
        Map<String, Object> info = Sm4Util.getParams(params);
        ExcelUtil excelUtil = new ExcelUtil();
        Resource resource = new ClassPathResource("/excel/classInfo");
        try {
            String excelName = "Class_Module";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String fileName = "班级信息表" + format.format(new Date());

            excelName = new String(excelName.getBytes(), StandardCharsets.ISO_8859_1);
            if (!excelUtil.readFromExcel(resource.getFilename(), excelName)) {
                throw new RuntimeException("找不到模板文件");
            }
            info.put("pageSize",Constants.EXPORTPAGESIZE);
            PageInfo<ClassInfo> pageInfo = classService.classList(info);
            List<ClassInfo> list = pageInfo.getList();
            classService.outPut(excelUtil, info, list);

            if (!excelUtil.writeExcelToResponse(response, new String(fileName.getBytes("GBK"), StandardCharsets.ISO_8859_1))) {
                throw new RuntimeException(new Exception("导出出错"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
