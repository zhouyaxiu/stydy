package com.shensu.jsjm.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class Util {

	public static byte[] getBytesFromInputStream(InputStream inputStream) throws Exception {
		int len = 0;
		byte[] bBuffer = new byte[2048];
		byte[] bIn = null;
		ByteArrayOutputStream baout = new ByteArrayOutputStream();
		while ((len = inputStream.read(bBuffer, 0, bBuffer.length)) != -1) {
			baout.write(bBuffer, 0, len);
		}
		inputStream.close();
		bIn = baout.toByteArray();
		return bIn;
	}

	public static String formatPhoneNo(String phoneNo) {
		if (StringUtils.isBlank(phoneNo)) {
			return "";
		}
		String phoneNoTrim = phoneNo.trim();
		if (!"".equals(phoneNoTrim)) {
			if (phoneNoTrim.length() == 11) {
				// 移动电话，显示前3位和后4位，中间星号
				return phoneNoTrim.substring(0, 3) + "****" + phoneNoTrim.substring(7, 11);
			} else if (phoneNoTrim.length() > 4) {
				// 其他电话，显示后4位，其他星号
				StringBuffer phoneNoFormat = new StringBuffer();
				for (int i = 0; i < phoneNoTrim.length() - 4; i++) {
					phoneNoFormat.append("*");
				}
				phoneNoFormat.append(phoneNoTrim.substring(phoneNoTrim.length() - 4, phoneNoTrim.length()));
				return phoneNoFormat.toString();
			} else {
				// 小于4位
				return "*" + phoneNoTrim.substring(1, phoneNoTrim.length());
			}
		} else {
			return "";
		}
	}

	public static String getPoint2(Object price) {
		if (price != null) {
			if (Double.valueOf(String.valueOf(price)) > 0) {
				return String.format("%.2f", Double.valueOf(String.valueOf(price)));
			} else {
				return "0";
			}
		}
		return "";
	}

	public static String getUserNowordMessage(int type) {
		switch (type) {
		case 1:
			return "用户不存在！";
		case 2:
			return "用户已被禁言！";
		case 3:
			return "用户状态异常！";
		default:
			return "";
		}
	}

	/**
	 * 大于1000的以K结尾
	 *
	 * @param num
	 * @return
	 */
	public static String numberFix(Integer num) {
		if (num == null) {
			return "0";
		} else if (num > 9999) {
			return String.format(Locale.getDefault(), "%.1fW", (Math.round(num / 1000.0) / 10.0));
		} else if (num > 999) {
			return String.format(Locale.getDefault(), "%.1fK", (Math.round(num / 100.0) / 10.0));
		} else {
			return num.toString();
		}
	}

	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static void getFileStream(String filePath, HttpServletResponse response) throws Exception {
		URL url = new URL(filePath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置超时间为10秒
		conn.setConnectTimeout(10 * 1000);
		// 防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		// 得到输入流
		InputStream fis = conn.getInputStream();
		byte[] buffer = new byte[fis.available()];
		fis.read(buffer);
		fis.close();
		response.reset();
		// 设置response的Header
		response.addHeader("Content-Length", "" + conn.getContentLength());
		response.setContentType("application/octet-stream");

		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
		toClient.write(buffer);
		toClient.flush();
		toClient.close();
	}

}
