package com.shensu.security.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shensu.util.TextTemplateUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;

/**
 * 版权：(C) 版权所有 2019-2022  苏州沈苏自动化有限公司
 * <简述>注册拦截器
 * <详细描述>
 *
 * @author gelongyu
 */
@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    @Bean
    public InterceptorConfig InterceptorConfig() {
        return new InterceptorConfig();
    }

    @Bean
    public LogIntercepter logIntercepter() {
        return new LogIntercepter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //导入页面及后台方法，不能加入权限
        registry.addInterceptor(InterceptorConfig()).addPathPatterns("/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/user/login", "/user/getVerifyImg", "/user/logout", "/user/getSessionId", "/user/messageCount",
                "/common/checkLogout","/sms/code","/user/checkPhone","/integratedPlatform/loginNingBoRxrt","/integratedPlatform/JiChengloginIn","/round/*","/highStu/*");
        registry.addInterceptor(logIntercepter()).addPathPatterns("/**");
    }

    /**
     *  允许跨域访问
     */

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 可限制哪个请求可以通过跨域
                .allowedHeaders("*")  // 可限制固定请求头可以通过跨域
                .allowedOriginPatterns("*")  // 可限制访问ip可以通过跨域
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS") // 可限制固定methods可以通过跨域
                .allowCredentials(true) // 是否允许发送cookie
                .exposedHeaders(HttpHeaders.SET_COOKIE).allowCredentials(true);
    }


    @Bean
    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        converter.setObjectMapper(mapper);
        return converter;
    }

    @Bean public CookieSerializer httpSessionIdResolver(){
        DefaultCookieSerializer   cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setCookieName("token");
        cookieSerializer.setUseHttpOnlyCookie(false);
        return cookieSerializer;
    }



}
