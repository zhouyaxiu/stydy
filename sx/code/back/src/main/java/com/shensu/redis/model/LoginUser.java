package com.shensu.redis.model;



import com.shensu.mybatis.entity.systemmanage.SysModule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 版权：(C) 版权所有 2019-2022  苏州沈苏自动化有限公司
 * <简述>redis保存的 用户基本信息
 * <详细描述>
 * @author   gelongyu
 * @version  $Id$
 * @since
 * @see
 */
public class LoginUser implements Serializable{
    
    /**
     * @Fields serialVersionUID : TODO(目的和意义)
     */
    private static final long serialVersionUID = -7465252041132313235L;

    /**
     * @Fields token : token  用于验证
     */
    private String token;
    
    /**
     * @Fields loginName : 登录姓名
     */
    private String loginName;

    private String userName;
    
    /**
     * @Fields userId : 登录人的ID
     */
    private String userId;
    
    /**
     * @Fields sessionId : 服务器的session
     */
    private String sessionId;
    
    
    /**
     * @Fields loginTime : 登录时间
     */
    private String loginTime;
    
    
    /**
     * @Fields uuid : 随机数
     */
    private String uuid;
    
    /**
     * @Fields empCode : 人员编码
     */
    private String empCode;


    private Map<String, Object> areaInfo = new HashMap<>();
    
    /**
     * @Fields path : 用户的目录结构
     */
    private List<SysModule> path = new ArrayList<SysModule>();
    
    /**
     * @Fields pathCheck : 直接用来验证前端传过来的地址是否存在
     */
    private List<String>  checkPath = new  ArrayList<String>();
    
    /**
     * @Fields userArea : 用户所属的所有区划
     */
    private List<Map<String, Object>> userArea = new ArrayList<Map<String, Object>>();
    
    private Integer messageCount;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }


    public List<SysModule> getPath() {
        return path;
    }

    public void setPath(List<SysModule> path) {
        this.path = path;
    }

    public List<String> getCheckPath() {
        return checkPath;
    }

    public void setCheckPath(List<String> checkPath) {
        this.checkPath = checkPath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
	public List<Map<String, Object>> getUserArea() {
		return userArea;
	}

	public void setUserArea(List<Map<String, Object>> userArea) {
		this.userArea = userArea;
	}

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }

    public Map<String, Object> getAreaInfo() {
        return areaInfo;
    }

    public void setAreaInfo(Map<String, Object> areaInfo) {
        this.areaInfo = areaInfo;
    }
}
