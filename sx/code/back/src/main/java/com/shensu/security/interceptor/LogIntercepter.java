package com.shensu.security.interceptor;

import com.shensu.controller.CommonController;
import com.shensu.service.logService.LogService;
import com.shensu.util.Constants;
import com.shensu.util.Sm4Util;
import com.shensu.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author hogans
 * @Date 2021/10/27 10:37
 **/
@Component
public class LogIntercepter implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonController.class);

    @Resource
     private LogService logService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String params = request.getParameter("params");
        Map<String, Object> info = Sm4Util.getParams(params);
        String operateType = StringUtils.nullToString(info.get("operateType"));//操作类型  0-6
        String loginname = StringUtils.nullToString(info.get("loginname"));//登录名
        String accountName  = StringUtils.nullToString(info.get("accountName"));//账户名
        String modelName =StringUtils.nullToString(info.get("modelName"));//模块名称
        String modelObject =StringUtils.nullToString(info.get("modelObject"));//疾控端，门诊端 ，学校端
        String areaCode =StringUtils.nullToString(info.get("areaCodeSelect"));//选择登录的账户所属地区
        String areaLevel =StringUtils.nullToString(info.get("areaLevelSelect"));//选择登录的账户所属地区
        if (Constants.query.equals(operateType) || Constants.output.equals(operateType) ||Constants.insert.equals(operateType)||Constants.delete.equals(operateType)||Constants.update.equals(operateType)||Constants.print.equals(operateType)) {
            // 导出
            logService.insertSysLog(loginname, modelName, operateType, accountName,modelObject,areaCode,areaLevel);
        }
        return true;
    }
}
