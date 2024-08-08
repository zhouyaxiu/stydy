package com.shensu.mybatis.mapper.systemmanage;

import com.shensu.mybatis.entity.systemmanage.SchUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zcy
 * @ClassName SysUserMapper
 * @Description
 * @createTime 2023/4/7
 */
@Mapper
public interface SysUserMapper {
    SchUser findByLoginName(String loginName);
    void updateUserStatus(@Param("loginName") String loginName, @Param("invalidCount") int invalidCount);
    void updateUserLoginCount(@Param("loginName") String loginName,@Param("loginCount") int loginCount);

    List<String> getIdsForClean();

    void cleanUserStatus(List<String> ids);

    void updateUserPwd(@Param("loginName") String loginName,@Param("userPassword") String userPassword);

    void updateUserForcePwd(@Param("loginName") String loginName);
}
