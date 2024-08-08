package com.shensu.mybatis.mapper.systemmanage;

import com.shensu.mybatis.entity.systemmanage.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author zcy
 * @ClassName SysUserMapper
 * @Description
 * @createTime 2023/4/3
 */
@Mapper
public interface SysUserMapper {
    SysUser findByLoginName(@Param("loginName")String inLoginName);

    Map findUserAreaName(@Param("areaCode") String areaCode);

    List<SysUser> getUsersByArea(@Param("areaId") String areaId,@Param("loginName") String loginName,@Param("userName") String userName);

    void insertUser(SysUser user);

    String getUserMaxId();

    void updateUser(@Param("id") String id,@Param("userName") String userName,@Param("loginName") String loginName,
                    @Param("password") String password,@Param("updateUser") String updateUser,@Param("userType") String userType);

    void delUser(String id);
}
