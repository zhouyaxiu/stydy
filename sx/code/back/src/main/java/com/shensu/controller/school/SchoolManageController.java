package com.shensu.controller.school;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.school.SchUserBean;
import com.shensu.mybatis.entity.school.SchoolManageBean;
import com.shensu.service.school.SchoolManageService;
import com.shensu.service.taskService.TaskService;
import com.shensu.util.*;
import io.swagger.annotations.SwaggerDefinition;
import org.apache.commons.beanutils.BeanUtils;
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
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * @author zcy
 * @ClassName SchoolManageController
 * @Description
 * @createTime 2023/4/4
 */
/**
 *  学校管理
 * */
@RestController
@RequestMapping("school")
public class SchoolManageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchoolManageController.class);

    @Autowired
    private SchoolManageService schoolManageService;

    @Autowired
    private TaskService taskService;

    /**
     * 学校列表
     * 根据页面查询查询所有学校  逻辑删除的学校不在此列
     */
    @RequestMapping(value = "/schoolList", method = RequestMethod.POST)
    public Object schoolList(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            info.put("pageSize",Constants.PAGESIZE);
            PageInfo<SchoolManageBean> beans = schoolManageService.schoolList(info);
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
     * 学校新增
     * @param
     */
    @RequestMapping(value = "/addSchool", method = RequestMethod.POST)
    public Object addSchool(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            SchoolManageBean schoolManageBean = new SchoolManageBean();
            BeanUtils.populate(schoolManageBean,info);
            schoolManageBean.setSchStationCode(info.get("areaCode").toString());
            schoolManageBean.setSchStationId(info.get("areaId").toString());
            schoolManageBean.setSchStationName(info.get("areaName").toString());

            String supCode = info.get("supCode").toString();
            String msg = schoolManageService.addSchool(schoolManageBean,supCode);
            object.put("msg", msg);
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
     * 学校详情
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public Object detail(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            String schId = info.get("schId").toString();
            SchoolManageBean bean = schoolManageService.detail(schId);
            object.put("data", bean);
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
     * 修改学校
     */
    @RequestMapping(value = "/modifySchool", method = RequestMethod.POST)
    public Object modifySchool(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            String msg = schoolManageService.modifySchool(info);
            object.put("msg", msg);
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
     * 删除学校
     */
    @RequestMapping(value = "/deleteSchool", method = RequestMethod.POST)
    public Object deleteSchool(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            String[] schIds = info.get("schIds").toString().split(",");
            String schTypes = info.get("schTypes").toString();
            String msg = null;
            if ("0_1".contains(schTypes)) {
                msg = "选中学校包含幼托机构或小学，无法删除！";
            } else {
                msg = schoolManageService.delSchool(Arrays.asList(schIds));
            }
            object.put("msg", msg);
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
     * 学校用户查询
     */
    @RequestMapping(value = "/schUserList", method = RequestMethod.POST)
    public Object schUserList(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            String schId = info.get("schId").toString();
            List<SchUserBean> beans = schoolManageService.schUserList(schId);
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
     * 学校用户新增
     */
    @RequestMapping(value = "/addSchUser", method = RequestMethod.POST)
    public Object addSchUser(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            String schId = info.get("schId").toString();
            String fullCode = info.get("fullCode").toString();
            String schType = info.get("schType").toString();
            String msg = null;
            if (!"234".contains(schType)) {
                msg = "无法在该学校类型下新增用户！";
            } else {
                msg = schoolManageService.addSchUser(schId,fullCode);
            }
            object.put("msg", msg);
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
     * 学校用户密码重置
     */
    @RequestMapping(value = "/resetSchUser", method = RequestMethod.POST)
    public Object resetSchUser(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            String id = info.get("id").toString();
            String schType = info.get("schType").toString();
            String msg = null;
            if ("234".contains(schType)) {
                msg = schoolManageService.resetSchUser(id);
            } else {
                msg = "无法重置该学校密码！";
            }
            object.put("msg", msg);
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
        Resource resource = new ClassPathResource("/excel/school");
        try {
            String excelName = "SchoolManage_export";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String fileName = "学校信息表" + format.format(new Date());

            excelName = new String(excelName.getBytes(), StandardCharsets.ISO_8859_1);
            if (!excelUtil.readFromExcel(resource.getFilename(), excelName)) {
                throw new RuntimeException("找不到模板文件");
            }
            info.put("pageSize",Constants.EXPORTPAGESIZE);
            PageInfo<SchoolManageBean> pageInfo = schoolManageService.schoolList(info);
            List<SchoolManageBean> list = pageInfo.getList();
            schoolManageService.outPut(excelUtil, info, list);

            if (!excelUtil.writeExcelToResponse(response, new String(fileName.getBytes("GBK"), StandardCharsets.ISO_8859_1))) {
                throw new RuntimeException(new Exception("导出出错"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 手动同步宁波学校数据
     */
    @RequestMapping(value = "/syncSchAndSchUser")
    public void syncSchAndSchUser() {
        try {
            String areaCode = "330600000000";
            LocalDate localDate = LocalDate.now();
            String date = localDate.toString();
            taskService.SchInfoTask(areaCode,date);
            // 同步学校用户
            taskService.SchUserInfoTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
