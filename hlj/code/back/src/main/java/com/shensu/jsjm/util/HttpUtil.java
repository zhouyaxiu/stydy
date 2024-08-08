package com.shensu.jsjm.util;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class HttpUtil {

    public final static String ENCODING = "UTF-8";

    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

    public static String postHttp(String postStr, String urlstr) throws Exception {
        log.info("httpURl:{},method:POST,param:{}", urlstr, postStr);
        String result = null;
        URL url = new URL(urlstr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
//            connection.setRequestProperty("content-type", "text/xml");
        BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream());
        out.write(postStr.getBytes(ENCODING));
        out.flush();
        result = new String(getBytesFromInputStream(connection.getInputStream()), ENCODING);
        return result;
    }

    public static void main(String[] args) {
        String urlstr = "http://192.168.2.149/data-exchange/sk9fa5bmr7awgq3l/v1.0/exchange-magic/sd/loadStroe";
        Map<String, Object> map = new HashMap<>();
        map.put("GBCODE","3704000000");
        String json = JSON.toJSONString(map);
        ResponseBean response = postHttpWithAuth(json,urlstr, "Bearer 767ce810-4b46-43e2-802b-7727bfedc991");
    }

    private static final OkHttpClient client = new OkHttpClient().newBuilder().retryOnConnectionFailure(true)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS).build();

    public static String postHttpToken(String postStr, String urlstr, String headerKey, String headerValue) {
        log.info("postHttpToken, url:{};param:{}",urlstr,postStr);
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = okhttp3.RequestBody.create(mediaType, postStr);
        Request request = new Request.Builder().url(urlstr)
                .method("POST", body).addHeader(headerKey, headerValue)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    String bodyStr = response.body().string();
                    Map<String,Object> map = JSON.parseObject(bodyStr, Map.class);
                    log.info("postHttpToken return:{}",map);
                    response.close();
                    return (String) map.get("access_token");
                } else {
                    response.close();
                    log.error("postHttpToken body is null");
                    return null;
                }
            } else {
                response.close();
                log.error("postHttpToken error");
                return null;
            }
        } catch (IOException e) {
            if (response != null){
                response.close();
            }
            log.error(e.getMessage());
            return null;
        }
    }

    public static ResponseBean postHttpWithAuth(String postStr,String urlstr,String auth) {
        log.info("getHttpWithAuth, url:{};param:{} auth:{}",urlstr,postStr ,auth);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = okhttp3.RequestBody.create(mediaType, postStr);;
        Request request = new Request.Builder().url(urlstr)
                .method("POST", body)
                .addHeader("Authorization", auth)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    ResponseBean responseBean = JSON.parseObject(response.body().string(), ResponseBean.class);
//                    log.info("getHttpWithAuth return:{}",responseBean.toString());
                    response.close();
                    return responseBean;
                } else {
                    log.error("getHttpWithAuth body is null");
                    response.close();
                    return new ResponseBean("");
                }
            } else {
                log.error("getHttpWithAuth error");
                response.close();
                return new ResponseBean("");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            if (response != null){
                response.close();
            }
            return new ResponseBean("");
        }
    }

    public static ResponseBean getHttpWithAuth(String urlstr,String auth) {
        log.info("getHttpWithAuth, url:{}; auth:{}",urlstr,auth);
        Request request = new Request.Builder().url(urlstr)
                .addHeader("Authorization", auth)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    ResponseBean responseBean = JSON.parseObject(response.body().string(), ResponseBean.class);
                    log.info("getHttpWithAuth return:{}",responseBean.toString());
                    response.close();
                    return responseBean;
                } else {
                    log.error("getHttpWithAuth body is null");
                    response.close();
                    return new ResponseBean("");
                }
            } else {
                log.error("getHttpWithAuth error");
                response.close();
                return new ResponseBean("");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            if (response != null){
                response.close();
            }
            return new ResponseBean("");
        }
    }

    public static byte[] getBytesFromInputStream(InputStream inputStream)
            throws Exception {
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

    /**
     * @param postStr
     * @param urlstr
     * @param authorization token
     * @return
     * @throws Exception
     */
    public static String postHttpXml(String postStr, String urlstr, String authorization) throws Exception {
        log.info("postHttpXml, url:{};param:{} authorization:{}",urlstr,postStr ,authorization);
        String result = "";
        URL url = new URL(urlstr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
/*            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000); */
        //connection.setRequestProperty("content-type","application/xml; charset=utf-8");
        connection.setRequestProperty("content-type", "application/json; charset=utf-8");
        connection.setRequestProperty("Accept", "application/json; charset=utf-8");
        if (authorization != null) {
            //非oauth接口需要带入
            connection.setRequestProperty("Authorization", authorization);
        }
        BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream());
        out.write(postStr.getBytes(StandardCharsets.UTF_8));
        out.flush();
        result = new String(Util.getBytesFromInputStream(connection.getInputStream()), StandardCharsets.UTF_8);
//        System.out.println(result);
        // String ddd = AESDateUtil.decrypt(result);
        //System.out.println(new String(ddd));
        //System.out.println(ddd);
        //System.out.println(AESDateUtil.decrypt(result));
        return result;
    }

    public static String getIp(HttpServletRequest request) {
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

    /**
     * http 下载文件流到前端 jsp
     *
     * @return
     * @throws Throwable
     */
    public static InputStream downloadFile(String filePath) {
        InputStream fis = null;
        try {
            URL httpurl = new URL(filePath);
            HttpURLConnection httpConn = (HttpURLConnection) httpurl.openConnection();
            httpConn.setDoOutput(true);// 使用 URL 连接进行输出
            httpConn.setDoInput(true);// 使用 URL 连接进行输入
            httpConn.setUseCaches(false);// 忽略缓存
            httpConn.setRequestMethod("POST");// 设置URL请求方法
            //可设置请求头
            httpConn.setRequestProperty("Content-Type", "application/octet-stream");
            httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            httpConn.setRequestProperty("Charset", "GBK");
            //可设置请求头
            fis = httpConn.getInputStream();
        } catch (Exception e) {
            //log.info("--------文件下载失败--------->>>要下载的文件地址是："+filePath);
            throw new RuntimeException(e);
        }
        return fis;
    }

    /**
     * 从输入流中获取字节数组
     *
     * @return
     * @throws IOException
     */
    public static final byte[] input2byte(InputStream inStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    public static String getPort(HttpServletRequest request) {
        String port = request.getRemotePort() + "";//返回发出请求的客户机的端口号。
        return port;
    }
    /*public static void main(String[] args)
	{
		try {		
			/*postHttpXml("EIuDkz638/bis42+WiOJosq8KDGerFX/LiFzMaKXiGPkVWZwvC2M5nVhebFAnVs6e1Bkts12fH44KDNZBBdq4r1VqmjyAJfRKDliRAxODCnLDJLb61zZwcZzBNXJX3XdAIlTXeQc9v7I1OKigP3zu+cLvr696LGz0wky++Ej/Tk=", 
					//"http://192.168.1.254:9080/aefi2019_liaoning/downloadAEFI");
		"http://192.168.1.254:9080/aefi2019_liaoning/downloadAEFI");*/
    //postHttpXml("IjhYtQnIsKKXBLOSfumomr7lfNDXT5JlRsY7Bzqy1ZeQpwyvfwoWqlzCsZuwVRAARM85Gt+pefnZiQ7OphBGqP/Ta4hOY/vgLeL5Bz0r2jD4PE6jxsarOhASjhV4tCASBnJ0xMIXjWu03PMBVkysgl6TFmNZF60ho4rr89miN18=",
			/*postHttpXml("N4dcuw3uxeULZTgIlssDg8mJ+T2oYZEMa/Qh+GEmtoHLPI1+mLNqQYJ0NJIx07dBet9RaYrFPZARcm9lj4DjUmxA06T3+oPPSAS5IcYhX4bVGVFk1TnwaNuxIIas+3Lq1liVqZTPBrfa71P/MuEvKnhFVUgiUBAzdt2P4oRLZLQ=",
			"http://221.224.159.210:9081/aefi2019_liaoning/downloadAEFI");*/
//			postHttpXml("{\"ustationcode\":\"2101020101\",\"downloadflag\":\"increment\",\"startday\":\"2020-05-01\",\"endday\":\"2020-07-10\",\"pageindex\":1}",
			/*postHttpXml("RVqVn45Z6mi5T869hX/emKhKowxTzvtDvqiYc7M9toYVrarXvxdZzXc7rOAdPe7TC0th7AlOlASv5sE2foRPyUp5Qc7VtIGDjo7lbqKp8zfCb92G0OtZSRCbJ+h/7M//H4AUnxayEE0R7C+k+tTx1PF6tSOjUe/09OX/dt7qgzA=", 
					//"http://221.224.159.210:9081/aefi2019_liaoning/downloadAEFI");
					"http://192.168.1.254:9080/aefi2019_liaoning/downloadAEFI");*/
			
			/*postHttpXml("{clientId:\"05013DDF837BCFDB9044F678B93532D1\",}", 
					"http://221.224.159.211:9931/authClient?"
					+ "clientId=05013DDF837BCFDB9044F678B93532D1&"
					+ "clientSecret=AEE5EAA7BE257E83475F32450AE5D010&"
					
					+ "accessToken=89707A4ABFA0665B79BBDD45131487ED&"
					+ "supervisionCode=81001240025640358522");///
			//System.out.println(HttpUtil.doGet("http://221.224.159.211:9931/getSupervisionCode?clientId=05013DDF837BCFDB9044F678B93532D1&clientSecret=AEE5EAA7BE257E83475F32450AE5D010&accessToken=F2021B3D9068C91DBC8E31D97AE98617&supervisionCode=81001240025640358522"));
			System.out.println(HttpUtil.doGet("http://192.168.1.254:8889/JMCOVID_APP_INTERFACE/Encryption/Account/Login?parameters=WoiaI+nFq3aPeeK7qO0Dm04cxolF4Q0maolE1dASd3OJsixN2l0xvlwO1k2Y+c2MbNCK9Ks5Fm0hVfRxV8JBMciYg4B8U8UrIsK96Ct4u6cicQGPWk4hZEiOzvechpQwDjkTN8BtFXbWcuR+cDXtTO29kV7WbVON/mDhViluvLHZYtkIkLawsfL3u3EaDRCYMWKX3TxGE27g1ncu6cgHJYrNUhj8iVOryqvh1BZWVoTWIAZnFvVqalwB8aLWr/2d0FcXecoMSwuPEjLJf0eQKoex7DrFJLXPrEOMPPfwxCKv5evj3PvNCySmSt7UymCXWf/m2QXfaZlTrpqtQuGp0MvpADSnaHQPDn0FDUCu+tq27rTo4azqUH8NnuxwPuI3ZwkoF8o5GIngj6KWExJ4Bq62oD1mnPaRu1e9rT6eujtbowYH+cbXtkIcse1CMyFKXGw3MJdn8LG3qSHRbiUT6w==&sign=525985de8487eb62389be51330ce7733"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
