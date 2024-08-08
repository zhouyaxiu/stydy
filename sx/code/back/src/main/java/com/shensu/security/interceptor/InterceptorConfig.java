package com.shensu.security.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.shensu.controller.NoticeController.NoticeMessController;
import com.shensu.redis.RedisOperates;
import com.shensu.redis.model.LoginUser;
import com.shensu.util.Constants;
import com.shensu.util.Sm4Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 版权：(C) 版权所有 2019-2022  苏州沈苏自动化有限公司
 * <简述>拦截器
 * <详细描述>
 *
 * @author dzjun
 * @version 1.0.0
 * @see
 * @since
 */
@Component
public class InterceptorConfig implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(InterceptorConfig.class);

    /**
     * userId名
     */
    private static final String USER_ID_NAME = "loginname";

    /**
     * Token名
     */
    private static final String TOKEN_NAME = "token";

    @Value("${spring.profiles.active}")
    private String runningMode;

    @Resource
    private SecureIgnoredProperties secureIgnoredProperties;

    /**
     * @Fields redisOperates : redis操作
     */
    @Resource
    private RedisOperates redisOperates;

    /**
     * @Fields LOGGER : 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InterceptorConfig.class);

    private static final PathMatcher MATCHER = new AntPathMatcher();

    @Override
    public void afterCompletion(HttpServletRequest arg0,
                                HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        logger.debug("afterCompletion");
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
                           Object arg2, ModelAndView arg3) throws Exception {
        logger.debug("postHandle");
    }

    /**
     * 1、先判断请求是否有token
     * 2、根据 loginname 从redis里面找到对应的sessionId
     * 3、当前url 用户是否有权限进去    对比 redisl里面的数据
     * 4、更新sessionId的过期时间
     *
     * @author gelongyu
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object arg2) throws Exception {
        String requestUrl = request.getServletPath(); //获取请求的地址
        LOGGER.info("调用接口:" + requestUrl);
        String params = request.getParameter("params");
        String permit = request.getParameter("permit");

        //仅开发模式用于测试接口
        if (!CollectionUtils.isEmpty(secureIgnoredProperties.getUrls())) {
            for (String urlPattern : secureIgnoredProperties.getUrls()) {
                if (MATCHER.match(urlPattern, requestUrl)) {
                    return true;
                }
            }
        }

        Map<String, Object> info = null;
        if (StringUtils.isNotBlank(params)) {
            info = Sm4Util.getParams(params);
            System.out.println(info + "*****");
        }

        if (info == null) {
            response.setStatus(Constants.ResultFailCode);
            OutputStream out = response.getOutputStream();
            out.write("没有权限，请重新登录！".getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
            return false;
        }

        //swagger开发调试模式
        if ("dev".equals(runningMode) && StringUtils.isNotBlank(permit) && Boolean.parseBoolean(permit)) {
            return true;
        }

        if (info.containsKey("token") && info.containsKey("loginname")) {
            String token = (String) info.get("token");
            String loginName = (String) info.get("loginname");
            if (StringUtils.isNotBlank(token)) {
                String json = redisOperates.get(loginName);
                LoginUser user = JSONObject.parseObject(json, LoginUser.class);
                if (user != null && StringUtils.isNotBlank(user.getSessionId()) && token.equals(user.getToken())) {
                    List<String> checkPath = user.getCheckPath();
                    checkPath.add("/user");
                    // boolean isUrlHas = false;
                    //TODO  暂时设定为不校验地址
                    boolean isUrlHas = true;
                    for (String s : checkPath) {
                        if (requestUrl.startsWith(s)) {
                            isUrlHas = true;
                            redisOperates.set(loginName, json);
                            break;
                        }
                    }
                    if (isUrlHas) {
                        redisOperates.set(loginName, json);
                        return true;
                    } else {
                        response.setStatus(Constants.ResultFailCode);
                        OutputStream out = response.getOutputStream();
                        out.write("没有权限，请重新登录！".getBytes(StandardCharsets.UTF_8));
                        out.flush();
                        out.close();
                        return false;
                    }
                } else {
                    String str = user != null ? user.getSessionId() : "user为空";
                    String s1 = user != null ? user.getToken() : "user为空";
                    String ss = "redis保存的sessionid=" + str + "和 redis的token=" + s1 + " and  页面的token=" + token + " 和 页面传过来的 loginName=" + loginName;
                    LOGGER.error(ss);
                    response.setStatus(Constants.LOGINOUT);
                    OutputStream out = response.getOutputStream();
                    out.write("登录失效，请重新登录！".getBytes(StandardCharsets.UTF_8));
                    out.flush();
                    out.close();
                    redisOperates.set(loginName, json);
                    return false;
                }
            } else {
                LOGGER.error("redis暂无当前用户信息，请重新登录！");
                response.setStatus(Constants.LOGINOUT);
                OutputStream out = response.getOutputStream();
                out.write("登录失效，请重新登录！".getBytes(StandardCharsets.UTF_8));
                out.flush();
                out.close();
                return false;
            }
        } else {
            String str = "token=" + info.containsKey("token") + " and  loginname=" + info.containsKey("loginname") + "redis暂无当前用户信息，请重新登录！";
            LOGGER.error(str);
            response.setStatus(Constants.LOGINOUT);
            OutputStream out = response.getOutputStream();
            out.write("登录失效，请重新登录！".getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
            return false;
        }

    }

}
