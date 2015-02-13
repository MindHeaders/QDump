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
        shiroFilterFactoryBean.setLoginUrl("/login/auth.html");
////		shiroFilterFactoryBean.setFilterChainDefinitions("" +
////				"/answers = authc, roles[ADMIN]" +
////				"/persons/** = authc, roles[ADMIN, USER]" +
////				"/questionnaires = authc, roles[ADMIN]" +
////				"/question = authc, roles[ADMIN]");
//        Map<String, String> chainDefinitionMap = new HashMap<>();
//        chainDefinitionMap.put("/rest/persons/get/min", "authc, roles[ADMIN]");
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(chainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public MemoryConstrainedCacheManager cacheManager() {
        MemoryConstrainedCacheManager cacheManager = new MemoryConstrainedCacheManager();
        return cacheManager;
    }

}
