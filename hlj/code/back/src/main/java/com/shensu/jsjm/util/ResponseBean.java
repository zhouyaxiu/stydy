package com.shensu.jsjm.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shensu.jsjm.constant.Constants;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseBean {

	private int code;
	private String msg;
	private String message;

	private boolean success;
	private Object data;
	private long time;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public ResponseBean() {
		
	}
	
	public ResponseBean(int code, String message, Object data) {
		this.code = code;
		this.msg = message;
		this.data = data;
		this.time = new Date().getTime();
	}

	public ResponseBean(String message) {
		this.code = -1;
		this.msg = message;
		this.data = null;
		this.time = new Date().getTime();
	}

	public String getMessage() {
		return message = message == null? msg:message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Override
	public String toString() {
		return "ResponseBean{" +
				"code=" + code +
				", message='" + message + '\'' +
				", data=" + data +
				'}';
	}
}
