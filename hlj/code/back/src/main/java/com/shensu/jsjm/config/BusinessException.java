package com.shensu.jsjm.config;

public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private int status;
	private Object data;

	public int getStatus() {
		return status;
	}

	public Object getData() {
		return data;
	}

	public BusinessException(int status, Object data) {
		super();
		this.status = status;
		this.data = data;
	}

}