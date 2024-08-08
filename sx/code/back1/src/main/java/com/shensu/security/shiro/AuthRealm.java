package com.shensu.security.shiro;

import com.shensu.mybatis.entity.systemmanage.SchUser;
import com.shensu.mybatis.mapper.systemmanage.SysUserMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Component
public class AuthRealm extends AuthorizingRealm {
    @Autowired
    private SysUserMapper sysUserMapper;

    //认证.登录
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String inLoginName = (String) token.getPrincipal();
        SchUser user = sysUserMapper.findByLoginName(inLoginName);
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
        return new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Long userId = (Long) SecurityUtils.getSubject().getPrincipal();
        SchUser user = null;
        List<String> permissions = new ArrayList<String>();
        permissions.add("add");
        info.addStringPermissions(permissions);//将权限放入shiro中.
        return info;
    }
}
