package com.shensu.service.systemmanage;

import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.systemmanage.SysUser;
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

    PageInfo<SysUser>  userList(int pageNum,String areaId,String loginName,String userName);

    String addUser(SysUser user);

    String updateUser(String id,String userName,String loginName,String userPassword,String updateUser,String userType);

    String delUser(String id);
}
