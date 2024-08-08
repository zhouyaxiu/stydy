package com.shensu.jsjm.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 自定义JSON工具类
 * 
 * @author puzm
 *
 */
public class JSONUtil {

	/**
	 * JSON转实体类
	 * 
	 * @param <T>
	 * @param jsonStr
	 * @param obj
	 * @return
	 */
	public static <T> Object JSONToObj(String jsonStr, Class<T> obj) {
		T t = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			t = objectMapper.readValue(jsonStr, obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
}
