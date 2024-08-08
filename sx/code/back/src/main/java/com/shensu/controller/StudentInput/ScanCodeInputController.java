package com.shensu.controller.StudentInput;


import com.alibaba.fastjson2.JSONObject;
import com.shensu.service.scanCodeService.ScanCodeService;
import com.shensu.util.CommonRespBody;
import com.shensu.util.Constants;
import com.shensu.util.Sm4Util;
import com.shensu.util.TextTemplateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Map;

@RestController
@RequestMapping("/scanCode")
@Api(tags="扫码录入")
public class ScanCodeInputController {
    private static final Logger logger = LoggerFactory.getLogger(ScanCodeInputController.class);

    @Resource
    private ScanCodeService scanCodeService;


    @Operation(
            summary = "通用扫码查询",
            description = "通用扫码查询"
    )
    @RequestMapping(value = "queryByConditions", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationCode", value = "所属接种单位编码", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "stationName", value = "所属接种单位名称", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "schid", value = "学校id", dataTypeClass = Integer.class, paramType = "query"),
            @ApiImplicitParam(name = "type", value = "选择扫码类型(1:儿童编码, 2:儿童条码, 3:身份证件号码, 4:儿童姓名、性别、出生日期)", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "childno", value = "儿童编码", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "imuno", value = "儿童条码", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "cid", value = "身份证件号码", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "name", value = "儿童姓名", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "gender", value = "性别", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "birth", value = "出生日期", dataTypeClass = String.class, paramType = "query")
    })
    public Object queryByConditions(@ApiIgnore String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        com.setCode(Constants.ResultSuccessCode);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            JSONObject info = Sm4Util.getParameters(params);
            //String schid = info.getString("schid"); //学校id
            Integer type = info.getInteger("type"); //1:儿童编码, 2:儿童条码, 3:身份证件号码, 4:儿童姓名、性别、出生日期
            String childno = info.getString("childno"); //儿童编码
            String imuno = info.getString("imuno"); //儿童条码
            String cid = info.getString("cid"); //身份证件号码
            String name = info.getString("name"); //儿童姓名
            String gender = info.getString("gender"); //性别
            String birth = info.getString("birth"); //出生日期
            String stationCode = info.getString("stationCode"); //门诊编码

            if (type == null) {
                com.setPopMsg("请选择查询方式！");
                com.setSuccess(false);
                com.setCode(Constants.ResultFailCode);
                return com;
            }
            if (type == 1 && StringUtils.isBlank(childno)) {
                com.setPopMsg("请填写儿童编码！");
                com.setSuccess(false);
                com.setCode(Constants.ResultFailCode);
                return com;
            }
            if (type == 2 && StringUtils.isBlank(imuno)) {
                com.setPopMsg("请填写儿童条码！");
                com.setSuccess(false);
                com.setCode(Constants.ResultFailCode);
                return com;
            }
            if (type == 3 && StringUtils.isBlank(cid)) {
                com.setPopMsg("请填写身份证件号码！");
                com.setSuccess(false);
                com.setCode(Constants.ResultFailCode);
                return com;
            }
            if (type == 4 && (StringUtils.isBlank(name) || StringUtils.isBlank(gender) || StringUtils.isBlank(birth))) {
                com.setPopMsg("请填写儿童姓名、性别、出生日期！");
                com.setSuccess(false);
                com.setCode(Constants.ResultFailCode);
                return com;
            }
            if (StringUtils.isBlank(stationCode)) {
                com.setPopMsg("请选择所属接种单位！");
                com.setSuccess(false);
                com.setCode(Constants.ResultFailCode);
                return com;
            }
            object = scanCodeService.queryByConditions(info);
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
        return com;
    }


    @Operation(
        summary = "保存扫码结果",
        description = "保存扫码结果"
    )
    @RequestMapping(value = "saveByFchildno", method = RequestMethod.POST)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "schid", value = "学校id", dataTypeClass = Integer.class, paramType = "query", required = true),
        @ApiImplicitParam(name = "childno", value = "儿童编码", dataTypeClass = String.class, paramType = "query", required = true),
        @ApiImplicitParam(name = "year", value = "学年", dataTypeClass = String.class, paramType = "query", required = true),
        @ApiImplicitParam(name = "grade", value = "年级", dataTypeClass = String.class, paramType = "query", required = true),
        @ApiImplicitParam(name = "className", value = "班级名称", dataTypeClass = String.class, paramType = "query", required = true),
        @ApiImplicitParam(name = "classId", value = "班级id", dataTypeClass = String.class, paramType = "query", required = true),
        @ApiImplicitParam(name = "stationCode", value = "所属接种单位编码", dataTypeClass = String.class, paramType = "query", required = true)
    })
    public Object saveByFchildno(@ApiIgnore String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        com.setCode(Constants.ResultSuccessCode);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            JSONObject info = Sm4Util.getParameters(params);
            object = scanCodeService.saveByFchildno(info);
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
        return com;
    }



    /**
     * 其他学校存在或者
     */
    @Operation(
            summary = "保存转校或转班",
            description = "保存转校或转班"
    )
    @RequestMapping(value = "/saveAndChange", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schid", value = "学校id", dataTypeClass = Integer.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "childno", value = "儿童编码", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "classId", value = "班级id", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "stationCode", value = "所属接种单位编码", dataTypeClass = String.class, paramType = "query", required = true),
            @ApiImplicitParam(name = "stateCode", value = "202:学生在当前学校存在但是需要转班转年级, 203:学生已经存在, 204:用户存在于其他学校)", dataTypeClass = String.class, paramType = "query", required = true)
    })
    public Object saveAndChange(@ApiIgnore String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        com.setCode(Constants.ResultSuccessCode);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            JSONObject info = Sm4Util.getParameters(params);
            object = scanCodeService.saveAndChange(info);
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
