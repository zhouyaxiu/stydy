package com.shensu.controller.integrated;

import com.alibaba.fastjson.JSONObject;
import com.shensu.service.integrated.IntegratedSysUserService;
import com.shensu.util.CommonRespBody;
import com.shensu.util.Constants;
import com.shensu.util.Sm4Util;
import com.shensu.util.TextTemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static org.apache.ws.security.WSConstants.UT;

@Controller
@RequestMapping("/integratedPlatform")
public class IntegratedUserManagerController {
    private static final Logger log = LoggerFactory.getLogger(IntegratedUserManagerController.class);
    @Autowired
    private IntegratedSysUserService integratedSysUserService;

    @Value("${loginPath}")
    private String loginPath;

    @RequestMapping(value = "/loginNingBoRxrt", method = RequestMethod.GET)
    public String loginNingBoRxrt(@RequestParam("token") String token) {
        log.info("集成平台登录开始. ");
        log.info("redirect: " + loginPath+"?token="+token);
        return "redirect:" + loginPath + "?token=" + token;
    }


    //http://192.168.1.43:8080/rxrt/systemsetting/jcptUserLogin.action
    @RequestMapping(value = "/JiChengloginIn", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Object JiChengloginIn(HttpServletRequest request, HttpServletResponse response, String params) {
        Map<String, Object> info = Sm4Util.getParams(params);
        String token = info.get("token").toString();
        CommonRespBody comm = integratedSysUserService.loginIn(token,request);
        log.info("集成平台登录结束. ");
        String flag = comm.isSuccess() ? "true" : "false";
        log.info("集成平台登录结果. "+flag);
        return TextTemplateUtil.setResponse(comm, response);
    }


    public static void main(String[] args) {
        Map<String, Object> info = Sm4Util.getParams("2bab75f206d7d036f120ba09dd0e9a3cc8c284c4ba7af7695fffcd1ce058b93d2499de6c407e6d2c97d0620fa933b56f84d2e53880e8bed9981e02abb9d84ea806332b72ebfd642d36d0aa25dd6e9bda8ef8c16fa5bf3100b7a6c4dd232192a6948199be079ef6913f4acb9504f44196633f258ef49e563257f9808835859ff6");
        String token = info.get("token").toString();
        System.out.println(token);
    }
}

