package org.dataart.qdump.service.security;

import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.dataart.qdump.persistence.security.QdumpRealm;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * Created by artemvlasov on 29/01/15.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public QdumpRealm qdumpRealm() {
        return new QdumpRealm();
    }

    @Bean
    public WebSecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(qdumpRealm());
        manager.setCacheManager(cacheManager());
        return manager;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
        MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
        factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        factoryBean.setArguments(new Object[]{securityManager()});
        return factoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager());
        return advisor;
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());
//        shiroFilterFactoryBean.setLoginUrl("/app/Account/authentication.html");
//        Map<String, String> chainDefinitionMap = new HashMap<>();
//        chainDefinitionMap.put("/answers", "authc, roles[ADMIN]");
//        chainDefinitionMap.put("/questions", "authc, roles[ADMIN]");
//        chainDefinitionMap.put("/persons/answers", "authc, roles[ADMIN]");
//        chainDefinitionMap.put("/persons/questions", "authc, roles[ADMIN]");
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(chainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public MemoryConstrainedCacheManager cacheManager() {
        return new MemoryConstrainedCacheManager();
    }

}
