package com.shensu.controller.StudentInput;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.check.StudentBase;
import com.shensu.mybatis.entity.studentInput.ExcelImportResult;
import com.shensu.service.check.CheckInfoService;
import com.shensu.service.studentInputService.ManualEntryService;
import com.shensu.util.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author zcy
 * @ClassName ManualEntryController
 * @Description 手动添加
 * @createTime 2023/4/14
 */
@RestController
@RequestMapping("manualEntry")
public class ManualEntryController {
    private Logger logger = LoggerFactory.getLogger(ManualEntryController.class);


    @Autowired
    private ManualEntryService manualEntryService;
    @Autowired
    private CheckInfoService checkInfoService;

    /**
     * 查询未建档学生
     */
    @RequestMapping(value = "/queryList", method = RequestMethod.POST)
    public Object queryList(@RequestParam(name = "params") String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            String schId = info.get("schId").toString();
            String nameYear = info.get("year").toString();
            String grade = info.get("grade").toString();
            String stuSeq = info.get("round").toString();
            String className = info.get("className").toString();
            String pageNum = info.get("pageNum").toString();
            PageInfo<StudentBase> results = manualEntryService.queryList(schId, nameYear, grade, stuSeq, className,Integer.parseInt(pageNum), Constants.PAGESIZE);

            object.put("data", results);
        } catch (Exception e) {
            e.printStackTrace();
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
     * 未建档学生录入
     */
    @RequestMapping(value = "/pageEntry", method = RequestMethod.POST)
    public Object pageEntry(@RequestParam(name = "params") String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            Map<String, Object> map = manualEntryService.pageEntry(info);
            object.put("message", map.get("message"));
            if (map.containsKey("insertStuNo")) {
                Object insertStuNo = map.get("insertStuNo");
                List<String> castList = Util.castList(insertStuNo, String.class);
                checkInfoService.checkByChildno(castList);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
     * 查询学生在sass库中是否存在（不存在才能打印补证通知单）
     */
    @RequestMapping(value = "/ifNotHasCard", method = RequestMethod.POST)
    public Object ifHasCard(@RequestParam(name = "params") String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            String cid = info.get("cid").toString();
            boolean  results = manualEntryService.ifNotHasCard(cid);
            object.put("data", results);
        } catch (Exception e) {
            e.printStackTrace();
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