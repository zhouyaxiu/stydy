
package com.shensu.jsjm.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

/**
 * 自定义HttpServletRequest，统一处理请求数据解密问题
 * 
 */
public class JmDoctorHttpServletRequest implements HttpServletRequest {

	private HttpServletRequest request;

	private Map<Object, Object> decrypt;

	public JmDoctorHttpServletRequest(HttpServletRequest request, Map<Object, Object> decrypt) {
		this.request = request;
		this.decrypt = decrypt;
	}

	@Override
	public Object getAttribute(String s) {
		return request.getAttribute(s);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return request.getAttributeNames();
	}

	@Override
	public String getCharacterEncoding() {
		return request.getCharacterEncoding();
	}

	@Override
	public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
		request.setCharacterEncoding(s);
	}

	@Override
	public int getContentLength() {
		return request.getContentLength();
	}

	@Override
	public long getContentLengthLong() {
		return request.getContentLengthLong();
	}

	@Override
	public String getContentType() {
		return request.getContentType();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return request.getInputStream();
	}

	@Override
	public String getParameter(String s) {
		// 返回解密后的参数
		return String.valueOf(decrypt.get(s));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Enumeration getParameterNames() {
		// 这里是通过实体类注入参数
		return Collections.enumeration(decrypt.keySet());
	}

	@Override
	public String[] getParameterValues(String s) {
		// 这里是注入参数
		Object o = decrypt.get(s);
		if (o == null) {
			return null;
		} else {
			return new String[] { String.valueOf(o) };
		}
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return request.getParameterMap();
	}

	@Override
	public String getProtocol() {
		return request.getProtocol();
	}

	@Override
	public String getScheme() {
		return request.getScheme();
	}

	@Override
	public String getServerName() {
		return request.getServerName();
	}

	@Override
	public int getServerPort() {
		return request.getServerPort();
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return request.getReader();
	}

	@Override
	public String getRemoteAddr() {
		return request.getRemoteAddr();
	}

	@Override
	public String getRemoteHost() {
		return request.getRemoteHost();
	}

	@Override
	public void setAttribute(String s, Object obj) {
		request.setAttribute(s, obj);
	}

	@Override
	public void removeAttribute(String s) {
		request.removeAttribute(s);
	}

	@Override
	public Locale getLocale() {
		return request.getLocale();
	}

	@Override
	public Enumeration<Locale> getLocales() {
		return request.getLocales();
	}

	@Override
	public boolean isSecure() {
		return request.isSecure();
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String s) {
		return request.getRequestDispatcher(s);
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getRealPath(String s) {
		return request.getRealPath(s);
	}

	@Override
	public int getRemotePort() {
		return request.getRemotePort();

	}

	@Override
	public String getLocalName() {
		return request.getLocalName();

	}

	@Override
	public String getLocalAddr() {
		return request.getLocalAddr();

	}

	@Override
	public int getLocalPort() {
		return request.getLocalPort();

	}

	@Override
	public ServletContext getServletContext() {
		return request.getServletContext();

	}

	@Override
	public AsyncContext startAsync() throws IllegalStateException {
		return request.startAsync();

	}

	@Override
	public AsyncContext startAsync(ServletRequest servletrequest, ServletResponse servletresponse)
			throws IllegalStateException {
		return request.startAsync(servletrequest, servletresponse);
	}

	@Override
	public boolean isAsyncStarted() {
		return request.isAsyncStarted();
	}

	@Override
	public boolean isAsyncSupported() {
		return request.isAsyncSupported();
	}

	@Override
	public AsyncContext getAsyncContext() {
		return request.getAsyncContext();

	}

	@Override
	public DispatcherType getDispatcherType() {
		return request.getDispatcherType();

	}

	@Override
	public boolean authenticate(HttpServletResponse httpservletresponse) throws IOException, ServletException {
		return request.authenticate(httpservletresponse);

	}

	@Override
	public String changeSessionId() {
		return request.changeSessionId();

	}

	@Override
	public String getAuthType() {
		return request.getAuthType();

	}

	@Override
	public String getContextPath() {
		return request.getContextPath();

	}

	@Override
	public Cookie[] getCookies() {
		return request.getCookies();

	}

	@Override
	public long getDateHeader(String s) {
		return request.getDateHeader(s);

	}

	@Override
	public String getHeader(String s) {
		return request.getHeader(s);

	}

	@Override
	public Enumeration<String> getHeaderNames() {
		return request.getHeaderNames();

	}

	@Override
	public Enumeration<String> getHeaders(String s) {
		return request.getHeaders(s);

	}

	@Override
	public int getIntHeader(String s) {
		return request.getIntHeader(s);

	}

	@Override
	public String getMethod() {
		return request.getMethod();
	}

	@Override
	public Part getPart(String s) throws IOException, ServletException {
		return request.getPart(s);

	}

	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		return request.getParts();

	}

	@Override
	public String getPathInfo() {
		return request.getPathInfo();

	}

	@Override
	public String getPathTranslated() {
		return request.getPathTranslated();

	}

	@Override
	public String getQueryString() {
		return request.getQueryString();

	}

	@Override
	public String getRemoteUser() {
		return request.getRemoteUser();

	}

	@Override
	public String getRequestURI() {
		return request.getRequestURI();

	}

	@Override
	public StringBuffer getRequestURL() {
		return request.getRequestURL();

	}

	@Override
	public String getRequestedSessionId() {
		return request.getRequestedSessionId();

	}

	@Override
	public String getServletPath() {
		return request.getServletPath();

	}

	@Override
	public HttpSession getSession() {
		return request.getSession();

	}

	@Override
	public HttpSession getSession(boolean flag) {
		return request.getSession(flag);
	}

	@Override
	public Principal getUserPrincipal() {
		return request.getUserPrincipal();
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return request.isRequestedSessionIdFromCookie();
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		return request.isRequestedSessionIdFromURL();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isRequestedSessionIdFromUrl() {
		return request.isRequestedSessionIdFromUrl();
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return request.isRequestedSessionIdValid();
	}

	@Override
	public boolean isUserInRole(String s) {
		return request.isUserInRole(s);
	}

	@Override
	public void login(String s, String s1) throws ServletException {
		request.login(s, s1);
	}

	@Override
	public void logout() throws ServletException {
		request.logout();
	}

	@Override
	public <T extends HttpUpgradeHandler> T upgrade(Class<T> class1) throws IOException, ServletException {
		return request.upgrade(class1);
	}
}