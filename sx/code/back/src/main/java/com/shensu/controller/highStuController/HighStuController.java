package com.shensu.controller.highStuController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.shensu.controller.NoticeController.NoticeMessController;
import com.shensu.mybatis.entity.highStudent.HighStuInfoAndCheck;
import com.shensu.mybatis.entity.highStudent.HighStudCheck;
import com.shensu.mybatis.entity.highStudent.NoticeContent;
import com.shensu.mybatis.entity.inforInspection.InforReplantMess;
import com.shensu.mybatis.mapper.round.RoundMapper;
import com.shensu.service.highStuService.HighStuService;
import com.shensu.service.highStuService.impl.HighStuServiceImpl;
import com.shensu.service.roundService.RoundService;
import com.shensu.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author  ztt
 * 用于查验结果进行查询，导出 ，删除以及催种通知单的说明
 * */

@RestController
@RequestMapping("/highStu")
public class HighStuController {
    private static final Logger logger = LoggerFactory.getLogger(NoticeMessController.class);
    @Resource
    private HighStuService highStuService;


    /**
     *  查找查验结果信息
     * */
    @RequestMapping("/findCheckResult")
    public Object findCheckResult(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            //删选学生的条件
            //String areaCode = StringUtils.nullToString(info.get("areaCode"));
            //String areaLevel = StringUtils.nullToString(info.get("areaLevel"));
            String schId  =  StringUtils.nullToString(info.get("schId"));
            String nowYear  =StringUtils.nullToString(info.get("nowYear"));
            String vaccName  =StringUtils.nullToString(info.get("vaccName"));
            String roundName=StringUtils.nullToString(info.get("roundName"));
            String claYear = StringUtils.nullToString(info.get("claYear"));
            String claLevel = StringUtils.nullToString(info.get("claLevel"));
            String claId = StringUtils.nullToString(info.get("claId"));
            String cid =StringUtils.nullToString(info.get("cid"));
            //构建的轮次信息
            String stuName = StringUtils.nullToString(info.get("stuName"));
            int pageNum = Integer.parseInt(StringUtils.nullToString(info.get("pageNum")));
           PageInfo<HighStuInfoAndCheck> list  = highStuService.findCheckResult(cid ,schId,nowYear,vaccName,roundName,claYear,claLevel,claId,stuName,pageNum,Constants.PAGESIZE);
            object.put("data", list);
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
  *  导出
  * */
    @RequestMapping(value = "/outPutCheckResult", method = {RequestMethod.GET})
    public void outPutCheckResult(@ApiIgnore @RequestParam String params, HttpServletResponse response) {
        Map<String, Object> info = Sm4Util.getParams(params);

        String areaName =StringUtils.nullToString(info.get("areaName"));
        String schId  =  StringUtils.nullToString(info.get("schId"));
        String schType =  StringUtils.nullToString(info.get("schType"));
        String schName=StringUtils.nullToString(info.get("schName"));
        String nowYear  =StringUtils.nullToString(info.get("nowYear"));
        String vaccName  =StringUtils.nullToString(info.get("vaccName"));
        String roundName=StringUtils.nullToString(info.get("roundName"));
        String claYear = StringUtils.nullToString(info.get("claYear"));
        String claLevel = StringUtils.nullToString(info.get("claLevel"));
        String claLevelName = StringUtils.nullToString(info.get("claLevelName"));
        String claId = StringUtils.nullToString(info.get("claId"));
        String claName =StringUtils.nullToString(info.get("claName"));
        String cid =StringUtils.nullToString(info.get("cid"));
        //构建的轮次信息
        String stuName = StringUtils.nullToString(info.get("stuName"));
        int pageNum = Integer.parseInt(StringUtils.nullToString(info.get("pageNum")));
        StringBuilder  builder  = new StringBuilder();
        builder.append("选择地区：").append(areaName).append(" ");
        if(StringUtils.isNotBlank(schType)){
            builder.append("学校类型：").append(Constants.SCH_TYPE.get( schType)).append(" ");
        }
        builder.append("学校名称：").append(schName).append(" ");
        builder.append("年份：").append(nowYear).append(" ").append("疫苗：").append(vaccName).append(" ").append("轮次：第").append(roundName).append("轮 ");
        builder.append("学年：").append(claYear);
        if(StringUtils.isNotBlank(claLevelName)){
            builder.append("年级：").append(claLevelName).append(" ");
        }
        if(StringUtils.isNotBlank(claName)){
            builder.append("班级：").append(claName).append(" ");
        }
        if(StringUtils.isNotBlank(stuName)){
            builder.append("儿童姓名：").append(stuName).append(" ");
        }
        if(StringUtils.isNotBlank(cid)){
            builder.append("身份证件号码：").append(cid);
        }
        ExcelUtil excelUtil = new ExcelUtil();
        org.springframework.core.io.Resource resource = new ClassPathResource("/excel/highCheck");
        String excelName = "maSaiFengCheck";
        if("麻腮风疫苗".equals(vaccName)) {
            excelName = "maSaiFengCheck";
        }else  if("水痘疫苗".equals(vaccName)) {
            excelName = "shuiDouCheck";
        }else  if("流感疫苗".equals(vaccName)) {
            excelName = "liuGanCheck";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fileName = "特定疫苗查验接种信息" + format.format(new Date());
        excelName = new String(excelName.getBytes(), StandardCharsets.ISO_8859_1);
        if (!excelUtil.readFromExcel(resource.getFilename(), excelName)) {
            throw new RuntimeException("找不到模板文件");
        }
        try {
            PageInfo<HighStuInfoAndCheck> beans  = highStuService.findCheckResult(cid ,schId,nowYear,vaccName,roundName,claYear,claLevel,claId,stuName,pageNum,Constants.EXPORTPAGESIZE);
            if("麻腮风疫苗".equals(vaccName)) {
                highStuService.outPutMaSaiFengCheckResult(excelUtil, builder, beans.getList());
            }else if("水痘疫苗".equals(vaccName)){
                highStuService.outPutCheckResult(excelUtil, builder, beans.getList());
            }else if("流感疫苗".equals(vaccName)){
                highStuService.outPutLiuGanCheckResult(excelUtil, builder, beans.getList());
            }
            if (!excelUtil.writeExcelToResponse(response, new String(fileName.getBytes("GBK"), StandardCharsets.ISO_8859_1))) {
                throw new RuntimeException(new Exception("导出出错"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除查验结果
     * */
    @RequestMapping("/deleteCheckResult")
    public Object deleteCheckResult(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);

            String roundId  =  StringUtils.nullToString(info.get("roundId"));
            String stuNos  =StringUtils.nullToString(info.get("stuNos"));
            int num  = highStuService.deleteCheckResult(roundId,stuNos);
            if(num>0) {
                object.put("data", "成功");
            }else{
                object.put("data", "删除失败！");
            }
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
     * 查询补种通知单
     * */
    @RequestMapping("/queryPrintBuZhong")
    public Object queryPrintBuZhong(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);

            String roundId  =  StringUtils.nullToString(info.get("roundId"));
            String stuNos  =StringUtils.nullToString(info.get("stuNos"));
            List<NoticeContent> list   = highStuService.queryBuZhongMess(roundId,stuNos);
            object.put("data", list);

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
