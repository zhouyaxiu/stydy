package com.shensu.security.filter;

import com.alibaba.fastjson.JSONObject;
import com.shensu.security.interceptor.ParameterRequestWrapper;
import com.shensu.util.Sm4Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class HttpServletFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(HttpServletFilter.class);

    @Value("${spring.profiles.active}")
    private String runningMode;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = ((HttpServletRequest) request);
        //仅开发模式用于测试接口
        if ("dev".equals(runningMode)) { //开发模式
            String params = httpServletRequest.getParameter("params");
            //处理测试请求
            if (StringUtils.isBlank(params) && !CollectionUtils.isEmpty(httpServletRequest.getParameterMap())) {
                Map<String, String[]> paramMap = httpServletRequest.getParameterMap();
                JSONObject devData = new JSONObject();
                for (String key : paramMap.keySet()) {
                    devData.put(key, paramMap.get(key)[0]);
                }
                try {
                    String devParams = Sm4Util.encryptParams(devData.toJSONString());
                    Map<String, String[]> requestParamMap = new HashMap<>();
                    requestParamMap.put("params", new String[]{devParams});
                    requestParamMap.put("permit", new String[]{"true"});
                    ParameterRequestWrapper parameterRequestWrapper = new ParameterRequestWrapper(httpServletRequest, requestParamMap);
                    chain.doFilter(parameterRequestWrapper, response);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    chain.doFilter(request, response);
                }
            } else {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

}
