package com.shensu.jsjm.filter;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Maps;
import com.shensu.jsjm.config.ConfigValue;
import com.shensu.jsjm.constant.Constants;
import com.shensu.jsjm.servlet.JmDoctorHttpServletRequest;
import com.shensu.jsjm.util.AESUtil;
import com.shensu.jsjm.util.CommonUtil;
import com.shensu.jsjm.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Component
@WebFilter(urlPatterns = { "/*" }, filterName = "requestFilter")
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class RequestFilter implements Filter {
	private final static Logger log = LoggerFactory.getLogger(RequestFilter.class);

	/** 无需拦截的,无需进行过滤的url地址 */
//	private static final Set<String> ALLOWED_PATHS = Collections
//			.unmodifiableSet(new HashSet<>(Arrays.asList("/common")));
	private static final Set<String> ALLOWED_DRUID = Collections
			.unmodifiableSet(new HashSet<>(Arrays.asList("/druid")));
	private static final Set<String> ALLOWED_SWAGGER2 = Collections
			.unmodifiableSet(new HashSet<>(Arrays.asList("/swagger", "/v2", "/webjars")));
	private static final Set<String> ALLOWED_STATIC_RESOURCES = Collections
			.unmodifiableSet(new HashSet<>(Arrays.asList(".css", ".js", ".html", ".htm", ".jsp", ".gif", ".jpg",
					".jpeg", ".png", "ico", ".bmp", ".tif", ".gif", ".pcx", ".tga", ".exif", ".fpx", ".svg", ".psd",
					".cdr", ".pcd", ".dxf", ".ufo", "eps", ".ai", ".raw", ".WMF", ".webp", ".map", ".woff", ".woff2")));
	/** 无需解密的url地址 */
	private static final Set<String> ALLOWED_DECRYPT = Collections
			.unmodifiableSet(new HashSet<>(Arrays.asList("/system/singleLogin2")));

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("init-----------filter");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
		// 最外层过滤
		// 默认url，静态资源过滤
		if (( allowedPathDefault(path))
				&& doChain(req, res, request, response, path, chain)) {
			return;
		}

//		// 登录过滤
//		HttpSession session = request.getSession(true);
//		User user = (User) session.getAttribute("user");
//		if (user == null) {
//			log.info("【" + request.getMethod() + "】【" + request.getRequestURI() + "】【拦截！】：{}", "用户没有登陆，不允许操作！");
//			CommonUtil.responseProcess(response, request, Constants.STATUS_524, "用户没有登陆，不允许操作",
//					ConfigValue.getEncrypt(), ConfigValue.getSamesite());
//			return;
//		}
//
//		// 所有自定义url过滤
//		JSONArray jsonArray = JSONArray.fromObject(session.getAttribute("moduleURLListAll"));
//		List<Module> listAll = JSONObject.parseArray(jsonArray.toString(), Module.class);
//		if (!allowedPathCustom(path, listAll) && doChain(req, res, request, response, path, chain)) {
//			return;
//		}
//
//		// 用户url匹配过滤
//		jsonArray = JSONArray.fromObject(session.getAttribute("moduleURLList"));
//		List<Module> list = JSONObject.parseArray(jsonArray.toString(), Module.class);
//		if (!allowedPathCustom(path, list)) {
//			log.info("【" + request.getMethod() + "】【" + request.getRequestURI() + "】【拦截！】：{}", "用户没有权限，不允许操作！");
//			CommonUtil.responseProcess(response, request, Constants.STATUS_521, "用户没有权限，不允许操作",
//					ConfigValue.getEncrypt(), ConfigValue.getSamesite());
//			return;
//		}

		if (doChain(req, res, request, response, path, chain)) {
			return;
		}
	}

	@Override
	public void destroy() {
		log.info("destroy----------filter");
	}

	/**
	 * 默认过滤url，静态资源
	 */
	public boolean allowedPathDecrpt(String path) {
		for (String str : ALLOWED_DECRYPT) {
			if (path.startsWith(str))
				return true;
		}
		for (String str : ALLOWED_DRUID) {
			if (path.startsWith(str))
				return true;
		}
		for (String str : ALLOWED_SWAGGER2) {
			if (path.startsWith(str))
				return true;
		}
		for (String str : ALLOWED_STATIC_RESOURCES) {
			if (path.endsWith(str))
				return true;
		}
		return false;
	}

	/**
	 * 默认过滤url，静态资源
	 */
	public boolean allowedPathDefault(String path) {
//		for (String str : ALLOWED_PATHS) {
//			if (path.startsWith(str))
//				return true;
//		}
		for (String str : ALLOWED_DRUID) {
			if (path.startsWith(str))
				return true;
		}
		for (String str : ALLOWED_SWAGGER2) {
			if (path.startsWith(str))
				return true;
		}
		for (String str : ALLOWED_STATIC_RESOURCES) {
			if (path.endsWith(str))
				return true;
		}
		return false;
	}

//	/**
//	 * 自定义url
//	 */
//	public boolean allowedPathCustom(String path, List<Module> list) {
//		for (int i = 0; i < list.size(); i++) {
//			if (path.startsWith(list.get(i).getModuleUrl())) {
//				return true;
//			}
//		}
//		return false;
//	}

	/**
	 * 取form-data参数
	 * 
	 * @param request
	 */
	public Map<Object, Object> doGetFormData(HttpServletRequest request) {
		String contentType = request.getContentType();
		if (contentType != null && contentType.contains("multipart/form-data")) {
			MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
			// 将转化后的 request 放入过滤链中
			request = resolver.resolveMultipart(request);
		}
		Map<Object, Object> map = Maps.newHashMap();
		Enumeration<String> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String parameter = enumeration.nextElement();
			map.put(parameter, request.getParameter(parameter));
		}
		return map;
	}

	/**
	 * 解密
	 * @throws IOException
	 * @throws ServletException
	 */
	@SuppressWarnings("unchecked")
	public boolean doChain(ServletRequest req, ServletResponse res, HttpServletRequest request,
			HttpServletResponse response, String path, FilterChain chain) throws IOException, ServletException {
		DateTime startTime = DateUtil.date();

		// 不用解密
		if (!ConfigValue.getDecrypt() || allowedPathDecrpt(path)) {
			chain.doFilter(req, res);
			log.info("【" + request.getMethod() + "】【" + CommonUtil.getIpAddress(request) + "】【" + request.getRequestURI() + "】【请求耗时】：{}s",  DateUtil.between(startTime,DateUtil.date(), DateUnit.SECOND));
			return true;
		}

		// form-data取参数
		Map<Object, Object> decryptParameterMap = doGetFormData(request);
		if (decryptParameterMap.size() != 0) {
			log.info("【" + request.getMethod() + "】【" + CommonUtil.getIpAddress(request) + "】【" + request.getRequestURI() + "】【请求入参】：{}", decryptParameterMap);

			String parameters = decryptParameterMap.get("parameters").toString().replace(" ", "+");

			decryptParameterMap = (Map<Object, Object>) net.sf.json.JSONObject.fromObject(AESUtil.decrypt(parameters));
			req = new JmDoctorHttpServletRequest(request, decryptParameterMap);
			log.info("【" + request.getMethod() + "】【"+ CommonUtil.getIpAddress(request) + "】【" + request.getRequestURI() + "】【解密入参】：{}", decryptParameterMap);

		}

		chain.doFilter(req, res);
		log.info("【" + request.getMethod() + "】【" + CommonUtil.getIpAddress(request) + "】【" + request.getRequestURI() + "】【请求耗时】：{}s",  DateUtil.between(startTime,DateUtil.date(), DateUnit.SECOND));
		return true;
	}
}
