package com.shensu.controller;

import com.shensu.redis.RedisOperates;
import com.shensu.service.systemmanage.SysUserService;
import com.shensu.util.CommonRespBody;
import com.shensu.util.Sm4Util;
import com.shensu.util.TextTemplateUtil;
import com.shensu.util.VerifyCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zcy
 * @ClassName LoginController
 * @Description 疾控端登录接口
 * @createTime 2023/4/3
 */
@RestController
@RequestMapping("user")
public class LoginController {
    /**
     * LOGGER : 日志
     */
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
        return TextTemplateUtil.setResponse(comm, response);
    }
}
