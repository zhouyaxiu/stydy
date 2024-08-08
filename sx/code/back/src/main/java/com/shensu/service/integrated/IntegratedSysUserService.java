package com.shensu.service.integrated;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.util.StringUtil;
import com.shensu.exception.MsgExcpetion;
import com.shensu.mybatis.entity.systemmanage.SysModule;
import com.shensu.mybatis.entity.systemmanage.SysUser;
import com.shensu.mybatis.mapper.systemmanage.SysUserMapper;
import com.shensu.redis.RedisOperates;
import com.shensu.redis.model.LoginUser;
import com.shensu.security.shiro.NewUserNameToken;
import com.shensu.service.systemmanage.SysUserService;
import com.shensu.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 集成平台用户管理业务
 *
 * */
@Service
public class IntegratedSysUserService {

    private static final Logger log = LoggerFactory.getLogger(SysUserService.class);
    @Resource
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisOperates redisOperates;

    @Value("${jiChengPath}")
    private String path;
    @Value("${ECBKEY}")
    private String ecbkey;
    /**
     * 验证用户登录的用户名和密码
     * */
    //f3788378868f0631b20a35d97f79b618

    @DS("master")
    public CommonRespBody loginIn(String saasToken, HttpServletRequest request) {
        CommonRespBody comm = new CommonRespBody();
        comm.setSuccess(false);
        comm.setCode(Constants.ResultFailCode);
        do {
            try {
                // 调用集成平台接口 验证token
                RestTemplate restTemplate = new RestTemplate();
                // 设置请求头
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                // 设置参数
                LinkedMultiValueMap body=new LinkedMultiValueMap();
                body.add("func","queryUserInfo");
                body.add("form","StatPlatform.view.Main.InspectionSysMgr");
                body.add("token",saasToken);
               // body.add("token",saasToken);
                // 请求体，包括请求数据 body 和 请求头 headers
                HttpEntity httpEntity = new HttpEntity(body,httpHeaders);
                ResponseEntity<String> responseEntity = restTemplate.exchange(path, HttpMethod.POST, httpEntity, String.class);
                //解析返回的数据
                JSONObject jsTemp = JSONObject.parseObject(responseEntity.getBody());
                Boolean success = jsTemp.getBoolean("success");
                String errCode = jsTemp.getString("errCode");
                String errMsg = jsTemp.getString("errMsg");
                // 登录失败提前返回
                JSONObject rtnData = new JSONObject();
                log.info("集成平台token验证接口返回值：  success："+success+"  errCode:"+errCode+"  errMsg:"+errMsg);
                if (!success){
                    comm.setPopMsg(errMsg);
                    break;
                }

                // 获取返回的用户信息
                String data = jsTemp.getString("data");
                log.info("加密前数据data："+data);
                Map<String, Object> map = Sm4Util.decryptECB(data, ecbkey);
                String loginName = map.get("userCode").toString();
                String userName = map.get("userName").toString();
                String areaCode = map.get("loginStationCode").toString() + "00";


                HttpSession session =request.getSession(true);
                // shiro权限认证
                Subject subject = SecurityUtils.getSubject();
                System.out.println(subject);
                NewUserNameToken token = new NewUserNameToken(loginName);
                subject.login(token);
                comm.setPopMsg("登录成功!");
                session.setAttribute(Constants.SessionId, session.getId());
                Date sendDate = new Date();
                long loginTime = sendDate.getTime();
                String uuid = UUID.randomUUID().toString();
                String str = loginName + session.getId() + loginTime + uuid;
                String usertoken = MD5Util.MD5Encode(str);

                SysUser user = new SysUser();
                Map areaMap = sysUserMapper.findUserAreaName(areaCode);
                String areaId = areaMap.get("areaId").toString();
                String areaName = areaMap.get("areaName").toString();
                user.setLoginName(loginName);
                user.setUserName(userName);
                user.setAreaCode(areaCode);
                user.setAreaId(areaId);
                user.setAreaName(areaName);

                rtnData.put("token", usertoken);
                rtnData.put("userinfo", JSONObject.toJSON(user));
                rtnData.put("userarea", user.getAreaCode());
                rtnData.put("supareaid", user.getSupAreaId());
                rtnData.put("arealevel",  Util.getZoneLevelByCode(user.getAreaCode()));
                rtnData.put("errorCode",  errCode);
                comm.setData(rtnData);
                LoginUser loginUser = this.addLoginUser(user.getAreaCode(), usertoken, session.getId(), String.valueOf(loginTime), loginName, null);
                redisOperates.set(loginName, JSONObject.toJSONString(loginUser));
                comm.setSuccess(true);
                comm.setCode(Constants.ResultSuccessCode);
            }   catch (MsgExcpetion e) {
                e.printStackTrace();
                comm.setSuccess(false);
                comm.setPopMsg(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage(), e);
                comm.setPopMsg(Constants.FAILMESSAGE);
            }
        } while (false);

        return comm;
    }


    public LoginUser addLoginUser(String empCode, String token, String sessionId, String loginTime, String loginName, List<SysModule> path) {
        LoginUser user = new LoginUser();
        if (StringUtils.isNotBlank(loginName)) {
            user.setLoginName(loginName);
        }
        if (StringUtils.isNotBlank(sessionId)) {
            user.setSessionId(sessionId);
        }
        if (StringUtils.isNotBlank(token)) {
            user.setToken(token);
        }
        if (loginTime != null) {
            user.setLoginTime(loginTime);
        }
        if (path != null && !path.isEmpty()) {
            List<String> list = new ArrayList<String>();
            for (SysModule s : path) {
                if (StringUtils.isNotBlank(s.getModuleUrl())) {
                    list.add(s.getModuleUrl());
                }
            }
            user.setPath(path);
            user.setCheckPath(list);
        }
        if (StringUtils.isNotBlank(empCode)) {
            user.setEmpCode(empCode);
        }
        return user;
    }
}
