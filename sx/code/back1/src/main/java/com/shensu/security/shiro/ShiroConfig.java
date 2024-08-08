package com.shensu.security.shiro;

import com.shensu.security.filter.XssAndSqlFilter;
import com.shensu.security.interceptor.SecureIgnoredProperties;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * shiro的配置类
 */
@Configuration
public class ShiroConfig {

    @Resource
    private SecureIgnoredProperties secureIgnoredProperties;

    /**
     *〈简述〉注册过滤器XssAndSqlFilter
     *〈详细描述〉
     * @author gelongyu
     * @return
     */
    @Bean
    public FilterRegistrationBean xssFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new XssAndSqlFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        Map<String, String> initParameters =new HashMap<String, String>();
        initParameters.put("excludes", "/favicon.ico,/img/*,/js/*,/css/*"); //不需要参数过滤的请求url
        initParameters.put("isIncludeRichText", "true");    //主要用于设置富文本内容是否需要过滤
        filterRegistrationBean.setInitParameters(initParameters);
        return filterRegistrationBean;
    }

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager manager){
        ShiroFilterFactoryBean bean=new ShiroFilterFactoryBean();
        bean.setSecurityManager(manager);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        //swagger3, knife4j
        if (!CollectionUtils.isEmpty(secureIgnoredProperties.getUrls())) {
            List<String> ignoredUrls = secureIgnoredProperties.getUrls();
            for (String ignoredUrl : ignoredUrls) {
                filterChainDefinitionMap.put(ignoredUrl, "anon");
            }
        }

        //注意过滤器配置顺序 不能颠倒
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了，登出后跳转配置的loginUrl
        filterChainDefinitionMap.put("/user/*", "anon");
        filterChainDefinitionMap.put("/roster/*", "anon");
        filterChainDefinitionMap.put("/common/*", "anon");
        filterChainDefinitionMap.put("/scanCode/*", "anon");
        filterChainDefinitionMap.put("/log/*", "anon");
        filterChainDefinitionMap.put("/notice/*", "anon");
        filterChainDefinitionMap.put("/inforInspect/*", "anon");
        filterChainDefinitionMap.put("/manualEntry/*", "anon");
        filterChainDefinitionMap.put("/batchExcel/*", "anon");
        filterChainDefinitionMap.put("/class/*", "anon");
        filterChainDefinitionMap.put("/highStu/*", "anon");
        filterChainDefinitionMap.put("/round/*", "anon");
        filterChainDefinitionMap.put("/reseedSummar/*", "anon");
        filterChainDefinitionMap.put("/**", "authc");

        //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        bean.setLoginUrl("/unauth");
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }

    //配置核心安全事务管理器
    @Bean
    public SecurityManager securityManager(@Qualifier("authRealm") AuthRealm authRealm) {
        DefaultWebSecurityManager manager=new DefaultWebSecurityManager();
        manager.setRealm(authRealm);
        return manager;
    }


    //配置自定义的权限登录器
    @Bean(name="authRealm")
    public AuthRealm authRealm(@Qualifier("credentialsMatcher") CredentialsMatcher matcher) {
        AuthRealm authRealm=new AuthRealm();
        authRealm.setCredentialsMatcher(matcher);
        return authRealm;
    }
    //配置自定义的密码比较器
    @Bean(name="credentialsMatcher")
    public CredentialsMatcher credentialsMatcher() {
        return new CredentialsMatcher();
    }
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator=new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager manager) {
        AuthorizationAttributeSourceAdvisor advisor=new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);
        return advisor;
    }

    public static void main(String[] args) {
        System.out.println(Charset.defaultCharset());//查看当前jdk编码
        System.getProperties().put("file.encoding", "UTF-8");//修改当前编码为UTF-8
        System.getProperties().list(System.out);
        System.out.println(Charset.defaultCharset());//查看当前jdk编码
    }

}
