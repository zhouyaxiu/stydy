package com.shensu.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.nurClass.ClassInfo;
import com.shensu.mybatis.entity.round.Round;
import com.shensu.mybatis.entity.school.SchoolManageBean;
import com.shensu.redis.RedisOperates;
import com.shensu.service.roundService.RoundService;
import com.shensu.service.scanCodeService.ScanCodeService;
import com.shensu.service.school.SchoolManageService;
import com.shensu.service.systemmanage.SysAreaService;
import com.shensu.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @autho  zhangtt
 *  公共接口：提供地区控件 班级 学校等公共信息查询
 * */
//@Api(tags="公共接口")
@RestController
@RequestMapping("/common")
public class CommonController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonController.class);
    @Autowired
    private RedisOperates redisOperates;
    @Resource
    private SchoolManageService schoolManageService;
    @Autowired
    private SysAreaService sysAreaService;

    @Resource
    private ScanCodeService scanCodeService;

    @Autowired
    private RoundService roundService;
    /**
     * 查询地区
     *
     * @param response
     * @return
     */
   /* @Operation(
            summary = "地区控件",
            description = "地区控件查询"
    )*/
    @RequestMapping(value = "queryArea", method = {RequestMethod.POST})
    /*@ApiImplicitParams({
            @ApiImplicitParam(name = "areaCode", value = "地区编码", dataTypeClass = String.class, paramType = "query",required = true)
    })*/
    public Object queryArea(String params, HttpServletResponse response) {

        CommonRespBody comm = new CommonRespBody();
        comm.setSuccess(true);
        Map<String, Object> info = Sm4Util.getParams(params);
        try {
            String areacode = info.get("areaCode").toString();
            String key = "AREA:" + areacode;
            redisOperates.del(key);
            JSONArray sysAreaData = null;
            if (redisOperates.exists(key)) {
                sysAreaData = JSONArray.parseArray(redisOperates.get(key));
            } else {
                sysAreaData = sysAreaService.listChildWithStationByAreaCode(areacode);
                //缓存4小时
                redisOperates.set(key, sysAreaData.toJSONString(), 12, TimeUnit.HOURS);
            }

            comm.setData(sysAreaData);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            comm.setPopMsg(Constants.FAILMESSAGE);
            comm.setSuccess(false);
        }

        TextTemplateUtil.responseStutas(comm, response);
        return comm.isSuccess() ? comm.getData() : comm.getPopMsg();
    }


   /* @Operation(
            summary = "地区控件",
            description = "地区控件查询"
    )*/
    @RequestMapping(value = "findSchoolByAreaAndSchType", method = {RequestMethod.POST})
    public Object schoolList(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);

            List<SchoolManageBean> beans =schoolManageService.findAllSchool(info);
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
  //根基学校查询班级信息
  @RequestMapping(value = "findClassMess", method = {RequestMethod.POST})
  public Object classMess(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            List<String> listClass  = scanCodeService.queryClassMess(info);
            object.put("data", listClass);
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

    @RequestMapping(value = "findHighClassMess",method = {RequestMethod.POST})
    public Object highClassMess(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            List<ClassInfo> listClass  = scanCodeService.queryHighClassMess(info);
            object.put("data", listClass);
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

    @RequestMapping(value = "findHighClassYear",method = {RequestMethod.POST})
    public Object findHighClassYear(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            List<ClassInfo> listClass  = scanCodeService.findHighClassYear(info);
            object.put("data", listClass);
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

    @RequestMapping(value = "findHighClassYears",method = {RequestMethod.POST})
    public Object findHighClassYears(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            List<ClassInfo> listClass  = scanCodeService.findHighClassYears(info);
            object.put("data", listClass);
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


    @RequestMapping("/findRounds")
    public Object findRounds(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            //构建的轮次信息
            String nowYear  =StringUtils.nullToString(info.get("nowYear"));
            String vaccName  =StringUtils.nullToString(info.get("vaccName"));
            String roundName=StringUtils.nullToString(info.get("roundName"));
            String areaCode=StringUtils.nullToString(info.get("areaCode"));
            String areaLevel=StringUtils.nullToString(info.get("areaLevel"));
            //添加轮次信息
            PageInfo<Round> pageInfo   = roundService.findRounds(areaCode,areaLevel,nowYear,vaccName,roundName,"0",-1,-1);
            object.put("data", pageInfo);
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
}
