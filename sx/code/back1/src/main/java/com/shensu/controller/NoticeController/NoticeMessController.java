package com.shensu.controller.NoticeController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.notice.NoticeMess;
import com.shensu.service.noticeMessService.NoticeMessService;
import com.shensu.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
/**
 * @author zhangtt
 * 用于查询变更通知单内容
 * */
@Api(tags="变更通知单内容")
@RestController
@RequestMapping("/notice")
public class NoticeMessController {
    private static final Logger logger = LoggerFactory.getLogger(NoticeMessController.class);

    @Autowired
    private NoticeMessService noticeMessService;
    /**
     * 通知列表
     */
 /*   @Operation(
            summary = "查询通知单内容",
            description = "通过学校和通知单类型获取通知单固定部分的信息"
    )*/
    @RequestMapping(value = "/queryNoticeMess", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
   /* @ApiImplicitParams({
            @ApiImplicitParam(name = "schId", value = "学校id", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "noticeType", value = "通知类型", dataTypeClass = String.class, paramType = "query"),
    })*/
    public Object notifyList(@ApiIgnore @RequestParam String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            String schId  = StringUtils.nullToString(info.get("schId"));
            String noticeType = StringUtils.nullToString(info.get("noticeType"));
            List<NoticeMess> beans =noticeMessService.findNoticeMess(schId,noticeType);
            object.put("data", beans);
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
     * 修改通知单内容
     */
    @Operation(
            summary = "更新通知单内容",
            description = "通过学校和通知单类型更新通知单信息"
    )
    @RequestMapping(value = "upNoticeMess", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schId", value = "学校id", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "noticeType", value = "通知类型", dataTypeClass = String.class, paramType = "query",required = true),
            @ApiImplicitParam(name = "noticeContent", value = "通知内容", dataTypeClass = String.class, paramType = "query")
    })
    public Object addNotify(@ApiIgnore @RequestParam String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            String schId =StringUtils.nullToString(info.get("schId"));
            String noticeType =StringUtils.nullToString(info.get("noticeType"));
            String noticeContent=StringUtils.nullToString(info.get("noticeContent"));
            NoticeMess mess  = new NoticeMess(schId,noticeType,noticeContent);
            String msg = noticeMessService.updateNotify(mess);
            object.put("data", msg);
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
