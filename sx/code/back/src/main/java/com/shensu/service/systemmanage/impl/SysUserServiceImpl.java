package com.shensu.service.systemmanage.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.shensu.exception.MsgExcpetion;
import com.shensu.mybatis.entity.systemmanage.SysModule;
import com.shensu.mybatis.entity.systemmanage.SysUser;
import com.shensu.mybatis.mapper.systemmanage.LogMapper;
import com.shensu.mybatis.mapper.systemmanage.SysUserMapper;
import com.shensu.redis.RedisOperates;
import com.shensu.redis.model.LoginUser;
import com.shensu.security.shiro.NewUserNameToken;
import com.shensu.service.logService.LogService;
import com.shensu.service.systemmanage.SysUserService;
import com.shensu.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author zcy
 * @ClassName SysUserServiceImpl
 * @Description
 * @createTime 2023/4/3
 */
@Service
public class SysUserServiceImpl implements SysUserService {
    /**
     * LOGGER : 日志
     */
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private RedisOperates redisOperates;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private LogMapper logMapper;
    @Override
    public CommonRespBody loginIn(Map<String, Object> info, HttpServletRequest request) {
        CommonRespBody comm = new CommonRespBody();
        comm.setSuccess(false);
        comm.setCode(Constants.ResultFailCode);
        String loginName = info.get("userName").toString();
        String password = (String) info.get("password");
        String valid = (String) info.get("validCode");
        do {
            try {
                HttpSession session =request.getSession(true);
                if (StringUtils.isNotEmpty(loginName) && StringUtils.isNotEmpty(password)&& StringUtils.isNotEmpty(valid)) {
                    // session中获取到验证码
                    String sessionId  = info.get("jessionid").toString();
                    String verifyCode =  redisOperates.get(sessionId);
                    // 如果session中的验证码不为null
                    if (!StringUtil.isEmpty(verifyCode)) {
                        //比较登录携带的验证码和刚才点击生成的验证码是否相同
                        if (!verifyCode.equalsIgnoreCase(valid)) {
                            comm.setPopMsg(Constants.LOGGER_ERROR1);
                            break;
                        }
                    } else {
                        comm.setPopMsg(Constants.LOGGER_ERROR2);
                        break;
                    }
                    //通过用户名查找用户
                    SysUser user = sysUserMapper.findByLoginName(loginName);
                    if (user == null) {
                        comm.setPopMsg(Constants.LOGGER_ERROR7);
                        break;
                    }
                    int invalidStatus = user.getInvalidStatus();
                    if (1 == invalidStatus) {
                        comm.setPopMsg(Constants.LOGGER_ERROR8);
                        break;
                    }
                    // 密码是否一致
                    if (!password.equals(user.getUserPassword())) {
                        comm.setPopMsg(Constants.LOGGER_ERROR6);
                        break;
                    }


                    // shiro权限认证
                    Subject subject = SecurityUtils.getSubject();
                    System.out.println(subject);
                    NewUserNameToken token = new NewUserNameToken(loginName, password,false);
                    subject.login(token);
                    comm.setPopMsg("登录成功!");
                    session.setAttribute(Constants.SessionId, session.getId());
                    Date sendDate = new Date();
                    long loginTime = sendDate.getTime();
                    String uuid = UUID.randomUUID().toString();
                    String str = loginName + session.getId() + loginTime + uuid;
                    String usertoken = MD5Util.MD5Encode(str);

                    JSONObject rtnData = new JSONObject();
                    rtnData.put("token", usertoken);
                    rtnData.put("userinfo", JSONObject.toJSON(user));
                    rtnData.put("userarea", user.getAreaCode());
                    rtnData.put("supareaid", user.getSupAreaId());
                    rtnData.put("arealevel",  Util.getZoneLevelByCode(user.getAreaCode()));
                    comm.setData(rtnData);
                    LoginUser loginUser = this.addLoginUser(user.getAreaCode(), usertoken, session.getId(), String.valueOf(loginTime), loginName, null);
                    loginUser.setUserId(user.getId());
                    loginUser.setUserName(user.getUserName());
                    Map<String, Object> areamap = new HashMap<>();
                    areamap.put("areacode", user.getAreaCode());
                    areamap.put("areaname", user.getAreaName());
                    areamap.put("areaid", user.getAreaId());
                    areamap.put("arealevel",  Util.getZoneLevelByCode(user.getAreaCode()));
                    loginUser.setAreaInfo(areamap);
                    redisOperates.set(loginName, JSONObject.toJSONString(loginUser));
                    comm.setSuccess(true);
                    comm.setCode(Constants.ResultSuccessCode);
                    String modelObject  = "4".equals((String)areamap.get("arealevel"))?"1":"0";
                    //插入日志
                    logMapper.insertLog("0","登录",user.getLoginName(),user.getUserName(),  DateUtils.getDate("yyyy-MM-dd HH:mm:ss"),modelObject,user.getAreaCode(),(String)areamap.get("arealevel"));
                } else if (StringUtils.isEmpty(loginName)) {
                    comm.setPopMsg(Constants.LOGGER_ERROR3);
                } else if (StringUtils.isEmpty(password)) {
                    comm.setPopMsg(Constants.LOGGER_ERROR4);
                } else if (StringUtils.isEmpty(valid)) {
                    comm.setPopMsg(Constants.LOGGER_ERROR5);
                }
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

    @Override
    public PageInfo<SysUser> userList(int pageNum, String areaId, String loginName, String userName) {
        PageHelper.startPage(pageNum,Constants.PAGESIZE);
        List<SysUser> usersByArea = sysUserMapper.getUsersByArea(areaId, loginName, userName);
        return new PageInfo<>(usersByArea);
    }

    @Override
    public String addUser(SysUser user) {
        String loginName = user.getLoginName();
        SysUser dbUser = sysUserMapper.findByLoginName(loginName);
        if (null!= dbUser) return "该用户已存在，无法新增！";
        // 获取主键
        String userMaxId = sysUserMapper.getUserMaxId();
        if (userMaxId == null || "".equals(userMaxId)) {
            userMaxId = "0";
        }
        userMaxId = String.valueOf(Long.parseLong(userMaxId) + 1);
        user.setId(userMaxId);
        sysUserMapper.insertUser(user);
        return "新增成功！";
    }

    @Override
    public String updateUser(String id, String userName, String loginName, String userPassword,String updateUser,String userType) {
        // 验证账号唯一性
        SysUser dbUser = sysUserMapper.findByLoginName(loginName);
        if (null!=dbUser && !dbUser.getId().equals(id)) {
            return "登录账号已存在，无法修改！";
        }
        sysUserMapper.updateUser(id,userName,loginName,userPassword,updateUser,userType);
        return "修改成功！";
    }

    @Override
    public String delUser(String id) {
        sysUserMapper.delUser(id);
        return "删除成功！";
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
