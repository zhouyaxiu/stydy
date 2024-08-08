package com.shensu.jsjm.config;

import com.shensu.jsjm.constant.Constants;
import com.shensu.jsjm.controller.CommonController;
import com.shensu.jsjm.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

@Configuration
@RestControllerAdvice(basePackages = "com.shensu.jsjm")
public class GlobalExceptionHandlerConfig {
	private final static Logger log = LoggerFactory.getLogger(CommonController.class);

	@ExceptionHandler(value = BusinessException.class)
	public Object businessException(HttpServletResponse response, BusinessException e) {
//		log.info("错误码：{},错误信息：{}", e.getStatus(), e.getData().toString(),e);
//		e.printStackTrace();
		response.setStatus(e.getStatus());
		return CommonUtil.respMsg(e.getStatus(), e.getData().toString(), ConfigValue.getEncrypt());

	}

	@ExceptionHandler(value = { BindException.class })
	public Object noParameterException(HttpServletResponse response, BindException ex) {
//		log.error("绑定异常:",ex);
		BindingResult result = ex.getBindingResult();
		response.setStatus(Constants.STATUS_521);
		return CommonUtil.respMsg(Constants.STATUS_521, result.getFieldError().getDefaultMessage(), ConfigValue.getEncrypt());
	}

	@ExceptionHandler(value = { ConstraintViolationException.class })
	public Object constraintViolationException(HttpServletResponse response, ConstraintViolationException ex) {
//		log.error("新增数据异常",ex);
		response.setStatus(Constants.STATUS_521);
		return CommonUtil.respMsg(Constants.STATUS_521, Constants.DATA_521, ConfigValue.getEncrypt());
	}

	@ExceptionHandler(value = { IllegalArgumentException.class })
	public Object IllegalArgumentException(HttpServletResponse response, IllegalArgumentException ex) {
//		log.error("参数异常",ex);
		response.setStatus(Constants.STATUS_521);
		return CommonUtil.respMsg(Constants.STATUS_521, Constants.DATA_521, ConfigValue.getEncrypt());

	}

	@ExceptionHandler(value = { NoHandlerFoundException.class })
	public Object noHandlerFoundException(HttpServletResponse response, Exception ex) {
//		log.error("路径不匹配异常",ex);
		response.setStatus(Constants.STATUS_404);
		return CommonUtil.respMsg(Constants.STATUS_404, Constants.DATA_404, ConfigValue.getEncrypt());
	}

	@ExceptionHandler(value = { HttpMessageNotReadableException.class })
	public Object httpMessageNotReadableException(HttpServletResponse response, Exception ex) {
//		log.error("HttpMessageNotReadableException",ex);
		response.setStatus(Constants.STATUS_521);
		return CommonUtil.respMsg(Constants.STATUS_521, Constants.DATA_521, ConfigValue.getEncrypt());

	}

	@ExceptionHandler(value = { Exception.class })
	public Object unknownException(HttpServletResponse response, Exception ex) {
//		log.error("系统异常",ex);
		response.setStatus(Constants.STATUS_521);
		return CommonUtil.respMsg(Constants.STATUS_521, Constants.DATA_521, ConfigValue.getEncrypt());

	}

}