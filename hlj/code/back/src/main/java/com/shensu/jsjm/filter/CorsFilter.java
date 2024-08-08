package com.shensu.jsjm.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.shensu.jsjm.config.ConfigValue;
import com.shensu.jsjm.constant.Constants;

@Component
@WebFilter(urlPatterns = "/*", filterName = "corsFilter")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
		if (!ConfigValue.getSamesite()) {
			String origin = httpRequest.getHeader("Origin");
			if (origin == null) {
				origin = httpRequest.getHeader("Referer");
			}
			httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, PATCH, DELETE");
			httpResponse.setHeader("Access-Control-Allow-Headers",
					"Origin, X-Requested-With, Content-Type, Accept, Authorization");
			httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
			httpResponse.setHeader("Access-Control-Allow-Origin", origin);
		}
		if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
			httpResponse.setStatus(Constants.STATUS_200);
			return;
		}
		filterChain.doFilter(servletRequest, httpResponse);
	}

	@Override
	public void destroy() {
	}
}
