package com.shensu.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * HttpUtils
 * @author ztt
 */
public class HttpUtils {
    /**
     * @param url 请求url
     * @return
     * @description 通用get请求
     */
    public static JSONObject sendGet(String url) {
        RestTemplate restTemplate = new RestTemplate();
        JSONObject body = restTemplate.getForEntity(url, JSONObject.class).getBody();
        return body;
    }

    public static String sendPost(String url, Map<String, Object> formMap) {
        HttpResponse response = HttpRequest.post(url)
                .form(formMap)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .execute();
        return response.body();
    }


    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

}
