//package com.shensu.jsjm.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.session.web.http.CookieSerializer;
//import org.springframework.session.web.http.DefaultCookieSerializer;
//
//@Configuration
//public class SpringSessionConfig {
//
//    public SpringSessionConfig() {
//    }
//
//    @Bean
//    public CookieSerializer httpSessionIdResolver() {
//        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
//        // 取消仅限同一站点设置
//        if (!ConfigValue.getSamesite()) {
//        	cookieSerializer.setUseHttpOnlyCookie(false);
//    		cookieSerializer.setSameSite(null);
//        }
//        cookieSerializer.setCookieMaxAge(3600);
//        return cookieSerializer;
//    }
//}