package com.shensu.controller.roundController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.shensu.controller.NoticeController.NoticeMessController;
import com.shensu.mybatis.entity.notice.NoticeMess;
import com.shensu.mybatis.entity.round.Round;
import com.shensu.service.roundService.RoundService;
import com.shensu.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author  ztt
 * 用于对轮次进行管理
 * */

@RestController
@RequestMapping("/round")
public class RoundController {
    private static final Logger logger = LoggerFactory.getLogger(NoticeMessController.class);

    @Resource
    private RoundService  roundService ;
    /**
     * 获取最新轮次名称（添加新轮次使用）
     * */
    @RequestMapping("/getRoundName")
    public Object addRoundName(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            //构建的轮次信息
            String nowYear  = StringUtils.nullToString(info.get("nowYear"));
            String vaccName  =StringUtils.nullToString(info.get("vaccName"));
            String roundName =roundService.findRoundName(nowYear,vaccName);
            object.put("data", roundName);
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
        return com.isSuccess() ? com.getData() : com.getPopMsg();
    }

     /**
      * 添加轮次信息
      * */
    @RequestMapping("/addRound")
    public Object addRound(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            //删选学生的条件
            String areaCode = StringUtils.nullToString(info.get("areaCode"));
            String startBirth = StringUtils.nullToString(info.get("startBirth"));
            String endBirth = StringUtils.nullToString(info.get("endBirth"));
            String schId  =  StringUtils.nullToString(info.get("schIds"));
            String claId =StringUtils.nullToString(info.get("claId"));//班级主键
            String claLevels = StringUtils.nullToString(info.get("claLevels"));//年级（方便展示）
            //构建的轮次信息
            String nowYear  =StringUtils.nullToString(info.get("nowYear"));
            String vaccName  =StringUtils.nullToString(info.get("vaccName"));
            String roundName=StringUtils.nullToString(info.get("roundName"));
            String startTime =StringUtils.nullToString(info.get("startTime"));
            String endTime =StringUtils.nullToString(info.get("endTime"));
            //添加轮次信息
            String mess = roundService.addRound(nowYear,vaccName,roundName,startTime,endTime,areaCode,schId,claId,claLevels,startBirth,endBirth);
            object.put("data", mess);
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
        return com.isSuccess() ? com.getData() : com.getPopMsg();
    }


    /**
     * 添加轮次信息
     * */
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
            int pageNum=Integer.parseInt(StringUtils.nullToString(info.get("pageNum")));
            //添加轮次信息
            PageInfo<Round> pageInfo   = roundService.findRounds(areaCode,areaLevel,nowYear,vaccName,roundName,"1",pageNum,Constants.PAGESIZE);
            object.put("data", pageInfo);
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
        return com.isSuccess() ? com.getData() : com.getPopMsg();
    }



    /**
     * 删除轮次信息
     * */
    @RequestMapping("/deleteRounds")
    public Object deleteRounds(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            //构建的轮次信息
            String roundId  =StringUtils.nullToString(info.get("roundId"));

            //添加轮次信息
           roundService.deleteRound(roundId);
           object.put("data", "删除成功！");
        } catch (Exception e) {
            com.setPopMsg(e.getMessage());
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
     * 根据条件查询轮次名称（下拉框）
     * */
    @RequestMapping("/findRoundYear")
    public Object findRoundYear(String params, HttpServletResponse response) {
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
            Set<String> set  = new HashSet<>();
            List<Round> allList  = pageInfo.getList();
            if(allList!=null){
                allList.forEach(round->set.add(round.getRoundYear()));
            }
            object.put("data", set);
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
        return com.isSuccess() ? com.getData() : com.getPopMsg();
    }
    /**
     * 根据条件查询轮次名称（下拉框）
     * */
    @RequestMapping("/findRoundName")
    public Object findRoundName(String params, HttpServletResponse response) {
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
            Set<String> set  = new TreeSet<String>((o1,o2)->{

                 return  Integer.parseInt(o1.substring(1,o1.length()-1))- Integer.parseInt(o2.substring(1,o2.length()-1));
            });
            List<Round> allList  = pageInfo.getList();
            if(allList!=null){
                allList.forEach(round->set.add(round.getRoundName()));
            }
            object.put("data", set);
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
        return com.isSuccess() ? com.getData() : com.getPopMsg();
    }


}
