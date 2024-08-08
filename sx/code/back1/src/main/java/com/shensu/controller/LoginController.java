package com.shensu.controller;

import com.alibaba.fastjson.JSONObject;
import com.shensu.redis.RedisOperates;
import com.shensu.service.systemmanage.SysUserService;
import com.shensu.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zcy
 * @ClassName LoginController
 * @Description
 * @createTime 2023/4/7
 */
@RestController
@RequestMapping("user")
public class LoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private RedisOperates redisOperates;
    @Autowired
    private SysUserService sysUserService;


    /**
     * 获取验证码图片
     */
    @RequestMapping(value = "/getVerifyImg", method = RequestMethod.GET)
    public void getVerifyImg(HttpServletRequest request, HttpServletResponse response, String params) throws IOException {
        Map<String, Object> info = Sm4Util.getParams(params);
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "No-cache");
        response.setContentType("image/jpeg");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        String verifyCode = VerifyCodeUtil.generateVerifyCode(4);
        HttpSession session=request.getSession(true);
        //将验证码放入session
        String sessionId  = info.get("jessionid").toString();
        redisOperates.set(sessionId, verifyCode.toLowerCase(), 60, TimeUnit.SECONDS);
        OutputStream out = response.getOutputStream();
        VerifyCodeUtil.outputImage(100, 33, out, verifyCode);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    /**
     * 获取getSessionId
     */
    @RequestMapping(value = "/getSessionId", method = RequestMethod.GET)
    @ResponseBody
    public Object getSessionId(HttpServletRequest request) {

        CommonRespBody msgBean = new CommonRespBody();
        HttpSession session = request.getSession(true);
        try {
            msgBean.setData(session.getId());
            redisOperates.append(session.getId(),"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msgBean;
    }

    /**
     * 登录接口
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @CrossOrigin
    @Transactional
    public Object ajaxLogin(HttpServletRequest request, HttpServletResponse response , String params) {
        //解密的内容等前端加密过后在放开
        Map<String, Object> info = Sm4Util.getParams(params);
        CommonRespBody comm = sysUserService.loginIn(info,request);
        return TextTemplateUtil.setResponse(comm, response);
    }

    /**
     * 登出
     *
     * @param params loginname 用户名
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    @ResponseBody
    public Object logout(String params, HttpServletResponse response) {
        Map<String, Object> info = Sm4Util.getParams(params);
        String loginName = info.get("loginname").toString();

        if (redisOperates.exists(loginName)) {
            redisOperates.del(loginName);
        }
        CommonRespBody comm = new CommonRespBody();
        comm.setSuccess(true);
        LOGGER.error("用户" + loginName + "在时间" + DateUtils.formatDateTime(new Date()) + "退出登录了");
        return TextTemplateUtil.setResponse(comm, response);
    }

    @RequestMapping("updateUserPwd")
    public Object updateUserPwd(String params, HttpServletResponse response) {
        CommonRespBody com = new CommonRespBody();
        com.setSuccess(true);
        JSONObject object = new JSONObject();
        Calendar start = Calendar.getInstance();
        try {
            Map<String, Object> info = Sm4Util.getParams(params);
            String loginName = info.get("loginName").toString();
            String oldPassword = info.get("oldPassword").toString();
            String newPassword = info.get("newPassword").toString();
            String isForce = StringUtils.nullToString(info.get("isForce").toString());
            String msg = sysUserService.updateUserPwd(loginName,oldPassword,newPassword,isForce);
            object.put("msg",msg);
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
