package com.shensu.service.systemmanage;

import com.shensu.util.CommonRespBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author zcy
 * @ClassName SysUserService
 * @Description
 * @createTime 2023/4/3
 */
public interface SysUserService {
    CommonRespBody loginIn(Map<String, Object> info, HttpServletRequest request);

    void cleanUserStatus();

    String updateUserPwd(String loginName,String oldPassword,String newPassword,String isForce);
}
