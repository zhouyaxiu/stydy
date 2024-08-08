package com.shensu.service.systemmanage.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.shensu.exception.MsgExcpetion;
import com.shensu.mybatis.entity.systemmanage.SchUser;
import com.shensu.mybatis.entity.systemmanage.SysModule;
import com.shensu.mybatis.mapper.systemmanage.LogMapper;
import com.shensu.mybatis.mapper.systemmanage.SysUserMapper;
import com.shensu.redis.RedisOperates;
import com.shensu.redis.model.LoginUser;
import com.shensu.service.systemmanage.SysUserService;
import com.shensu.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author zcy
 * @ClassName SysUserServiceImpl
 * @Description
 * @createTime 2023/4/7
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
                    SchUser user = sysUserMapper.findByLoginName(loginName);
                    if (user == null) {
                        comm.setPopMsg(Constants.LOGGER_ERROR7);
                        break;
                    }
                    // 查验学校是否被删除
                    if (user.getSchInvalidStatus().equals("1")) {
                        comm.setPopMsg(Constants.LOGGER_ERROR13);
                        break;
                    }
                    // 账号是否被锁定
                    int isaccess = user.getIsaccess();
                    if (isaccess==0) {
                        comm.setPopMsg(Constants.LOGGER_ERROR12);
                        break;
                    }
                    // 密码是否一致
                    if (!password.equals(user.getPassword())) {
                        int invalidCount = user.getInvalidCount();
                        int newCount = invalidCount + 1 > 5 ? 5 : invalidCount + 1;
                        comm.setPopMsg(Constants.LOGGER_ERROR6+"剩余"+ String.valueOf(5-newCount) +"次机会");
                        if(loginName!=null && !"".equals(loginName)) {
                            sysUserMapper.updateUserStatus(loginName, newCount);
                        }
                        break;
                    }


                    // shiro权限认证
                    Subject subject = SecurityUtils.getSubject();
                    UsernamePasswordToken token = new UsernamePasswordToken(loginName, password);
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
                    rtnData.put("userarea", user.getSchStationCode());

                    // 是否需要强制修改密码
                    // String updateDateStr = DateUtils.formatDate(user.getUpdateTime());
                    // String nowDate = DateUtils.getDate();
                    // long monthCompare = DateUtils.MonthCompare(updateDateStr, nowDate);
                        int loginCount = user.getLoginCount();
                    // if (monthCompare>=3 || user.getLoginCount()==0) {
                    //     // 首次登录，修改密码超过3个月都需要修改密码
                    //     user.setForcePwd("1");
                    //
                    // } else {
                    //     // 是否需要提示修改密码
                    //     Date updateDate = DateUtils.toDate(updateDateStr);
                    //     long l = DateUtils.pastDays(updateDate);
                    //     String date = DateUtils.stepMonth(updateDateStr, 3);
                    //     long dayCompare = DateUtils.dayCompare(updateDateStr, date);
                    //     long day = dayCompare - l;
                    //     if (day==1 || day== 2 || day== 3) {
                    //
                    //         String msg = "为了保障您的账户安全，您还有"+day+"天将修改密码。";
                    //         user.setForceMsg(msg);
                    //     }
                    // }
                    rtnData.put("userinfo", JSONObject.toJSON(user));
                    comm.setData(rtnData);
                    LoginUser loginUser = this.addLoginUser(user.getSchStationCode(), usertoken, session.getId(), String.valueOf(loginTime), loginName, null);
                    loginUser.setUserId(user.getId());
                    loginUser.setUserName(user.getLoginName());
                    Map<String, Object> areamap = new HashMap<>();
                    areamap.put("areacode", user.getSchStationCode());
                    areamap.put("areaname", user.getSchStationName());
                    areamap.put("areaid", user.getSchStationId());
                    areamap.put("arealevel", "4");
                    loginUser.setAreaInfo(areamap);
                    redisOperates.set(loginName, JSONObject.toJSONString(loginUser));
                    comm.setSuccess(true);
                    comm.setCode(Constants.ResultSuccessCode);
                    // 登录成功，清除失败次数
                    if(loginName!=null && !"".equals(loginName)) {
                        sysUserMapper.updateUserStatus(loginName, 0);
                    }
                    // 登录次数+1
                    sysUserMapper.updateUserLoginCount(loginName,loginCount+1);
                    logMapper.insertLog("0","登录",user.getLoginName(),user.getSchName(),  DateUtils.getDate("yyyy-MM-dd HH:mm:ss"),"2",user.getSchStationCode(),"4");

                } else if (StringUtils.isEmpty(loginName)) {
                    comm.setPopMsg(Constants.LOGGER_ERROR3);
                } else if (StringUtils.isEmpty(password)) {
                    comm.setPopMsg(Constants.LOGGER_ERROR4);
                } else if (StringUtils.isEmpty(valid)) {
                    comm.setPopMsg(Constants.LOGGER_ERROR5);
                }
            }   catch (MsgExcpetion e) {
                comm.setSuccess(false);
                comm.setPopMsg(e.getMessage());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                comm.setPopMsg(Constants.FAILMESSAGE);
            }
        } while (false);


        return comm;
    }

    @Override
    public void cleanUserStatus() {
        // 查询出需要重置的用户id
        List<String> ids = sysUserMapper.getIdsForClean();
        if(ids!=null && ids.size()>0) {
            sysUserMapper.cleanUserStatus(ids);
        }
    }

    @Override
    @Transactional
    public String updateUserPwd(String loginName, String oldPassword, String newPassword,String isForce) {
        String msg = "修改成功！";
        SchUser user = sysUserMapper.findByLoginName(loginName);
        String password = user.getPassword();
        if (password.equals(oldPassword)) {
            sysUserMapper.updateUserPwd(loginName,newPassword);
            if ("1".equals(isForce)) {
                sysUserMapper.updateUserForcePwd(loginName);
            }
        } else {
            msg = "输入的旧密码错误，请重新输入！";
        }
        return msg;
    }
    public LoginUser addLoginUser(String empCode, String token, String sessionId, String loginTime, String loginName, List<SysModule> path) {
        LoginUser user = new LoginUser();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(loginName)) {
            user.setLoginName(loginName);
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(sessionId)) {
            user.setSessionId(sessionId);
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(token)) {
            user.setToken(token);
        }
        if (loginTime != null) {
            user.setLoginTime(loginTime);
        }
        if (path != null && !path.isEmpty()) {
            List<String> list = new ArrayList<String>();
            for (SysModule s : path) {
                if (org.apache.commons.lang3.StringUtils.isNotBlank(s.getModuleUrl())) {
                    list.add(s.getModuleUrl());
                }
            }
            user.setPath(path);
            user.setCheckPath(list);
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(empCode)) {
            user.setEmpCode(empCode);
        }
        return user;
    }
}
