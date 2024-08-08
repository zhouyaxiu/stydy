package com.shensu.controller.logController;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.notice.NoticeMess;
import com.shensu.mybatis.entity.systemmanage.SysLog;
import com.shensu.service.logService.LogService;
import com.shensu.util.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/log")
public class LogController {

    @Resource
    private LogService  logService;

    @RequestMapping(value = "/queryLog",method= RequestMethod.POST)
    public Object findLogs( String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            String opertimeStrat =StringUtils.nullToString(info.get("opertimeStrat"));
            String opertimeEnd =StringUtils.nullToString(info.get("opertimeEnd"));
            String operaType=StringUtils.nullToString(info.get("operaType"));
            String operaObject=StringUtils.nullToString(info.get("operaObject"));
            String areaCode=StringUtils.nullToString(info.get("areaCode"));
            String areaLevel=StringUtils.nullToString(info.get("areaLevel"));
            int pageIndex=Integer.parseInt(StringUtils.nullToString(info.get("pageIndex")));
            int pageSize = Constants.PAGESIZE;
            PageInfo<SysLog> list  = logService.findLogs(opertimeStrat,opertimeEnd,operaType,operaObject,areaCode,areaLevel,pageIndex,pageSize);
            object.put("data", list);
        } catch (Exception e) {
            com.setPopMsg(Constants.FAILMESSAGE);
            com.setSuccess(false);
            com.setCode(Constants.ResultFailCode);
            e.printStackTrace();
        }
        Calendar end = Calendar.getInstance();
        double sec = (end.getTimeInMillis() - start.getTimeInMillis()) / 1000.0;
        object.put("sec", sec);
        com.setData(object);
        TextTemplateUtil.responseStutas(com, response);
        return com.isSuccess() ? com.getData() : com.getPopMsg();
    }
}
