package com.example.demo.config;

import com.example.demo.shiro.LoginAuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Bean
    public Realm realm(){
        return new LoginAuthorizingRealm();
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //用LinkedHashMap添加拦截的uri,其中authc指定需要认证的uri，anon指定排除认证的uri

        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/admin/login","anon");
        filterChainDefinitionMap.put("/admin/register","anon");
        filterChainDefinitionMap.put("/admin/unAuth","anon");
        filterChainDefinitionMap.put("/admin/401","anon");
        filterChainDefinitionMap.put("/**","authc");
        shiroFilterFactoryBean.setLoginUrl("/admin/401");
        shiroFilterFactoryBean.setSuccessUrl("/admin/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/admin/unAuth");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(realm());
        return  defaultWebSecurityManager;
    }
}
