package com.shensu.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.PageInfo;
import com.shensu.controller.LoginController;
import com.shensu.mybatis.entity.school.SchoolManageBean;
import com.shensu.mybatis.entity.systemmanage.SysUser;
import com.shensu.service.systemmanage.SysUserService;
import com.shensu.util.*;
import net.sf.cglib.beans.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Map;

/**
 * @author zcy
 * @ClassName UserController
 * @Description
 * @createTime 2023/7/27
 */
@RestController
@RequestMapping("userManage")
public class UserController {
    /**
     * LOGGER : 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private SysUserService sysUserService;


    /**
     * 用户列表
     * 根据页面查询查询所有用户  逻辑删除的用户不在此列
     */
    @RequestMapping(value = "/uerList", method = RequestMethod.POST)
    public Object uerList(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            int pageNum = Integer.parseInt(info.get("pageNum").toString());
            String areaId = info.get("areaId").toString();
            String loginName = StringUtils.nullToString(info.get("loginName"));
            String userName = StringUtils.nullToString(info.get("username"));
            PageInfo<SysUser> beans = sysUserService.userList(pageNum,areaId, loginName, userName);
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
     * 新增用户
     *
     */
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public Object addUser(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            // 将map转为对象
            SysUser user = new SysUser();
            BeanMap beanMap = BeanMap.create(user);
            beanMap.putAll(info);
            user.setUserName(info.get("username").toString());
            String msg = sysUserService.addUser(user);
            object.put("code","201");
            if (msg.contains("成功")) object.put("code","200");
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
     * 修改用户
     *
     */
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public Object updateUser(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            String id = info.get("id").toString();
            String userName = info.get("username").toString();
            String loginName = info.get("loginName").toString();
            String userPassword = info.get("userPassword").toString();
            String updateUser = info.get("updateUser").toString();
            String userType = info.get("userType").toString();
            String msg = sysUserService.updateUser(id, userName, loginName, userPassword, updateUser,userType);
            object.put("code","201");
            if (msg.contains("成功")) object.put("code","200");
            object.put("msg",msg);
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
     * 修改用户
     *
     */
    @RequestMapping(value = "/delUser", method = RequestMethod.POST)
    public Object delUser(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            String id = info.get("id").toString();
            String msg = sysUserService.delUser(id);
            object.put("code","200");
            object.put("msg",msg);
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
}
