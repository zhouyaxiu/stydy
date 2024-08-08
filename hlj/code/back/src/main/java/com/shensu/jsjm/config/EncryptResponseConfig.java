package com.shensu.jsjm.config;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.shensu.jsjm.constant.Constants;
import com.shensu.jsjm.util.CommonUtil;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

@Configuration
@ControllerAdvice(basePackages = "com.shensu.jsjm.controller")
public class EncryptResponseConfig implements ResponseBodyAdvice<Object> {
	private final static Logger log = LoggerFactory.getLogger(EncryptResponseConfig.class);

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object obj, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest serverHttpRequest,
			ServerHttpResponse serverHttpResponse) {
		if (serverHttpRequest.getURI().getPath().contains("/system/singleLogin2")
		) {
			return obj;
		}
		ServletServerHttpRequest sshreq = (ServletServerHttpRequest) serverHttpRequest;
		HttpServletRequest request = sshreq.getServletRequest();
		try {
			log.info("【" + request.getMethod() + "】【" + request.getRequestURI() + "】【返回参数】："
					+ JSONObject.fromObject(obj));
			return CommonUtil.respMsg(Constants.STATUS_200, JSONObject.fromObject(obj), ConfigValue.getEncrypt());
		} catch (JSONException e) {
			log.info("【" + request.getMethod() + "】【" + request.getRequestURI() + "】【返回参数】：" + (String) obj);
			return CommonUtil.respMsg(Constants.STATUS_200, (String) obj, ConfigValue.getEncrypt());
		}
	}
}