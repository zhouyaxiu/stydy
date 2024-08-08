package com.shensu.security.filter;

import com.shensu.security.interceptor.InterceptorConfig;
import com.shensu.util.TextTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;


/**
* 拦截防止sql注入xss，跨域问题解决
 *
 */
public class XssAndSqlFilter implements Filter {

    private String frontWebUrl;

    FilterConfig filterConfig = null;


    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        httpResponse.setHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
        String  path = ((HttpServletRequest) request).getServletPath();
        if ("/error".equals(path)) {
            String code = request.getAttribute("javax.servlet.error.status_code").toString();
            String message = request.getAttribute("javax.servlet.error.message").toString();

            httpResponse.setStatus(Integer.parseInt(code));
            httpResponse.setContentType("application/json; charset=utf-8");
            httpResponse.setCharacterEncoding("UTF-8");

            OutputStream out = httpResponse.getOutputStream();
            out.write(message.getBytes("UTF-8"));
            out.flush();
            out.close();
            return;
        }


        if (path.equals("/user/login") || path.equals("/integratedPlatform/JiChengloginIn")) {
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.addHeader("Access-Control-Allow-Origin", frontWebUrl);
            httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, PATCH, DELETE");
        } else {
            httpResponse.addHeader("Access-Control-Allow-Origin", "*");
            httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, PATCH, DELETE");
        }

        chain.doFilter(new XssAndSqlHttpServletRequestWrapper(
            (HttpServletRequest) request), response);

    }


    public void destroy() {
        this.filterConfig = null;

    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        // 读取配置文件值
        ServletContext servletContext = filterConfig.getServletContext();
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        // front.server.url为yml中的键
        frontWebUrl= ctx.getEnvironment().getProperty("front.server.url");
    }

}
