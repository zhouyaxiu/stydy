package com.shensu.jsjm.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;


public class HttpUtil2 {
	
	private final static Logger log = Logger.getLogger("shensu");
	
	public final static String ENCODING = "UTF-8";


	
	public static byte[] getBytesFromInputStream(InputStream inputStream) throws Exception {
		ByteArrayOutputStream baout = null;
		try {
				int len = 0;
				byte[] bBuffer = new byte[2048];
				byte[] bIn = null;
				baout = new ByteArrayOutputStream();
				while ((len = inputStream.read(bBuffer, 0, bBuffer.length)) != -1) {
					baout.write(bBuffer, 0, len);
				}
				bIn = baout.toByteArray();
				return bIn;
	       }finally {
			if(inputStream != null) {
				inputStream.close();
			}
			if(baout != null) {
				baout.close();
			}
		}
	}
	
	public static String postHttpXml(String postStr, String urlstr) {
		String result = "";
		BufferedOutputStream out = null;
		try {
			URL url=new URL(urlstr);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("content-type","application/json; charset=utf-8");
            out=new BufferedOutputStream(connection.getOutputStream());
            out.write(postStr.getBytes("UTF-8"));
            out.flush();
            result = new String(Util.getBytesFromInputStream(connection.getInputStream()),"UTF-8");

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	

}
