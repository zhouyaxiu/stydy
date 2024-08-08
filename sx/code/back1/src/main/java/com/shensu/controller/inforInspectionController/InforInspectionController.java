package com.shensu.controller.inforInspectionController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.inforInspection.InforReplantMess;
import com.shensu.service.InforInspection.InforInspectionService;
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
 * @author ztt
 * @ClassName InforInspectionController
 * @Description 查验情况汇总
 * @createTime 2023/4/4
 */

@RestController
@RequestMapping("/inforInspect")
public class InforInspectionController {
    private static final Logger log = LoggerFactory.getLogger(InforInspectionController.class);

    @Resource
    private InforInspectionService  inforInspectionService;

    //查询补种信息
    @RequestMapping(value = "/queryReplantMess", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public Object queryReplantMess(@ApiIgnore @RequestParam String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        Map<String, Object> info = Sm4Util.getParams(params);
        String schId  = StringUtils.nullToString(info.get("schId"));
        String classYear  = StringUtils.nullToString(info.get("classYear"));
        String grade  = StringUtils.nullToString(info.get("grade"));
        String lunci  = StringUtils.nullToString(info.get("lunci"));
        String className  = StringUtils.nullToString(info.get("className"));
        int  pageIndex  =Integer.parseInt(StringUtils.nullToString(info.get("pageIndex"))) ;
        int  pageSize=Constants.PAGESIZE;
        try {
            PageInfo<InforReplantMess> beans =inforInspectionService.findInforReplantMess(schId,classYear,lunci,grade,className,pageIndex,pageSize);
            object.put("data", beans);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        Calendar end = Calendar.getInstance();
        double sec = (end.getTimeInMillis() - start.getTimeInMillis()) / 1000.0;
        object.put("sec", sec);
        com.setData(object);
        TextTemplateUtil.responseStutas(com, response);
        return com.isSuccess() ? com.getData() : com.getPopMsg();
    }

    @RequestMapping(value = "/outPutReplantMess", method = {RequestMethod.GET})
    public void outPutInforReplantMess(@ApiIgnore @RequestParam String params, HttpServletResponse response) {
        Map<String, Object> info = Sm4Util.getParams(params);
        ExcelUtil excelUtil = new ExcelUtil();
        org.springframework.core.io.Resource resource = new ClassPathResource("/excel/inforInsoection");
        String excelName = "replantMess";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fileName = "补种情况反馈表_" + format.format(new Date());
        excelName = new String(excelName.getBytes(), StandardCharsets.ISO_8859_1);
        if (!excelUtil.readFromExcel(resource.getFilename(), excelName)) {
            throw new RuntimeException("找不到模板文件");
        }
        String schId  = StringUtils.nullToString(info.get("schId"));
        String classYear  = StringUtils.nullToString(info.get("classYear"));
        String grade  = StringUtils.nullToString(info.get("grade"));
        String lunci  = StringUtils.nullToString(info.get("lunci"));
        String className  = StringUtils.nullToString(info.get("className"));
        int  pageIndex  =Integer.parseInt(StringUtils.nullToString(info.get("pageIndex"))) ;
        int  pageSize=Constants.EXPORTPAGESIZE;
        try {
            PageInfo<InforReplantMess> beans =inforInspectionService.findInforReplantMess(schId,classYear,lunci,grade,className,pageIndex,pageSize);
            inforInspectionService.outPutReplantMess(excelUtil, info, beans.getList());
            if (!excelUtil.writeExcelToResponse(response, new String(fileName.getBytes("GBK"), StandardCharsets.ISO_8859_1))) {
                throw new RuntimeException(new Exception("导出出错"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
