//package com.shensu.jsjm.util;
//
//import com.shensu.jsjm.vo.HttpResponseBean;
//import org.apache.http.Header;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpException;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.methods.*;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicHeader;
//import org.apache.http.util.EntityUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class HttpClientCall
//{
//	private static final Logger log = LoggerFactory.getLogger("shensu");
//
//	public static CloseableHttpClient getHttpClientInstance()
//	{
//		return HttpClients.createDefault();
//	}
//
//	private static <T extends Header> void addHeader(List<T> headers, T header)
//	{
//		if(null == headers || 0 == headers.size())
//		{
//			headers = new ArrayList<T>();
//		}
//		headers.add(header);
//	}
//
//	private static  void initCommonHeader(List<BasicHeader> headers)
//	{
//		if(null == headers || 0 == headers.size())
//		{
//			headers = new ArrayList<BasicHeader>();
//		}
//		addHeader(headers,new BasicHeader("Accept", "application/json"));
//		addHeader(headers,new BasicHeader("Accept-Encoding", "gzip, deflate"));
//		addHeader(headers,new BasicHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"));
//		addHeader(headers,new BasicHeader("Cache-Control", "no-cache"));
//		addHeader(headers,new BasicHeader("Pragma", "no-cache"));
//		addHeader(headers,new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:24.0) Gecko/20100101 Firefox/24.0"));
//	}
//
//	private static HttpGet getGetMethod(String uri, List<BasicHeader> headers)
//	{
//		initCommonHeader(headers);
//		HttpGet httpget = new HttpGet(uri);
//		if(headers != null){
//		for (final BasicHeader header : headers) {
//			httpget.addHeader(header);
//		}
//    }
//		return httpget;
//	}
//
//
//	/**
//	 * 获取 HttpPost
//	 * @param uri
//	 * @param headers
//	 * @param httpEntity
//	 * @return
//	 */
//	public static HttpPost getPostMethod(String uri, List<BasicHeader> headers, HttpEntity httpEntity)
//	{
//		initCommonHeader(headers);
//		HttpPost httppost = new HttpPost(uri);
//        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(10 * 1000).setConnectTimeout(10 * 1000).setSocketTimeout(10 * 1000)
//                .build();
//		httppost.setConfig(requestConfig);
//		if(headers != null) {
//			for (final Header header : headers) {
//				httppost.addHeader(header);
//			}
//		}
//		httppost.setEntity(httpEntity);
//		return httppost;
//	}
//
//	/**
//	 * 获取 HttpPut
//	 * @param uri
//	 * @param headers
//	 * @param httpEntity
//	 * @return
//	 */
//	private static HttpPut getPutMethod(String uri, List<BasicHeader> headers, HttpEntity httpEntity)
//	{
//		initCommonHeader(headers);
//		HttpPut httpput = new HttpPut(uri);
//		if(headers != null) {
//			for (final Header header : headers) {
//				httpput.addHeader(header);
//			}
//		}
//		httpput.setEntity(httpEntity);
//		return httpput;
//	}
//
//
//	/**
//	 * 获取 HttpDelete
//	 * @param uri
//	 * @param headers
//	 * @return
//	 */
//	private static HttpDelete getDeleteMethod(String uri, List<BasicHeader> headers)
//	{
//		initCommonHeader(headers);
//		HttpDelete httpDelete = new HttpDelete(uri);
//		if(headers != null) {
//			for (final Header header : headers) {
//				httpDelete.addHeader(header);
//			}
//		}
//		return httpDelete;
//	}
//
//	public static HttpResponseBean callHttpClientPOST(String uri,List<BasicHeader> headers,HttpEntity httpEntity){
//		HttpUriRequest request = getPostMethod(uri,headers,httpEntity);
//		return callHttpClient(request);
//
//	}
//
//	public static HttpResponseBean callHttpClientPOST(String uri,HttpEntity httpEntity){
//		HttpUriRequest request = getPostMethod(uri,null,httpEntity);
//		return callHttpClient(request);
//
//	}
//
//	/**
//	 *上传文件
//	 * @author gaojun
//	 * @return Object
//	 */
//	public static String callHttpClientZip(CloseableHttpClient httpClient,HttpUriRequest request) throws IOException, HttpException {
//		CloseableHttpResponse response = null;
//		String result = "";
//		try
//		{
//			response = httpClient.execute(request);
//			log.info("httpclientCode=========="+response.getStatusLine());
//			result = EntityUtils.toString(response.getEntity());
//			if(response.getStatusLine().getStatusCode() != 200) {
//				log.info("请求失败返回的结果：==========" + result);
//				throw new HttpException(result);
//			}
//		}
//		catch (ClientProtocolException e)
//		{
//			e.printStackTrace();
//			throw e;
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//			throw e;
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//			throw e;
//		}
//		finally
//		{
//			try
//			{
//				if(null!=response){
//					response.close();
//				}
//			}
//			catch (IOException e)
//			{
//				e.printStackTrace();
//			}
//		}
//		return result;
//	}
//
//}
