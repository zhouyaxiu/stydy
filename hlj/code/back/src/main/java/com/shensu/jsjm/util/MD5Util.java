package com.shensu.jsjm.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

	public static final String MD5_KEY = "lvifj$%Gsdf2#BS@";

	/**
	 * @param text明文
	 * @param key密钥
	 * @return 密文
	 */
	// 带秘钥加密
	public static String md5(String text) {
		// 加密后的字符串
		try {
			return DigestUtils.md5Hex(text + MD5_KEY);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * @param text明文
	 * @param key密钥
	 * @return 密文
	 */
	// 带秘钥加密
	public static String md5(String text, String key) {
		// 加密后的字符串
		try {
			return DigestUtils.md5Hex(text + key);
		} catch (Exception e) {
			return "";
		}
	}

	// 不带秘钥加密
	public static String md52(String text) throws Exception {
		// 加密后的字符串
		String md5str = DigestUtils.md5Hex(text);
		return md5str;
	}

	/**
	 * MD5验证方法
	 * 
	 * @param text明文
	 * @param key密钥
	 * @param md5密文
	 */
	// 根据传入的密钥进行验证
	public static boolean verify(String text, String key, String md5) {
		try {
			String md5str = md5(text, key);
			if (md5str.equalsIgnoreCase(md5)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * MD5验证方法
	 * 
	 * @param text明文
	 * @param key密钥
	 * @param md5密文
	 */
	// 根据传入的密钥进行验证
	public static boolean verify(String text, String md5) {
		try {
			String md5str = md5(text, MD5_KEY);
			if (md5str.equalsIgnoreCase(md5)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 测试
	public static void main(String[] args) throws Exception {
		// String str =
		// "181115.041650.10.88.168.148.2665.2419425653_1";181115.040908.10.88.181.118.3013.1655327821_1
		String str = "hAodmpvYMYPrf6c3lPp48XchnhEtCwiXqAjN0GJcOYODIrp5i3fNxSD/g/wQ2b9g";
		System.out.println("加密的字符串：" + str + "\t长度：" + str.length());
		md5(str);
	}
}