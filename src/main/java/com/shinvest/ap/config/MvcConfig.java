package com.shinvest.ap.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.rathontech.sso.sp.agent.controller.ServiceProviderDispatcher;
import com.rathontech.sso.sp.config.SPConfigurationListener;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.common.interceptor.MenuCheckInterceptor;

/**
 * MVC 셋팅
 * Async 서비스 enable
 */
@Configuration
@EnableAsync
public class MvcConfig implements WebMvcConfigurer {

    /**
     * 서브메뉴 처리를 위한 인터셉터 추가
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(menuCheckInterceptor()).excludePathPatterns("/css/**", "/js/**", "/images/**", "/font/**", "/webjars/**","/favicon.ico","/","/login","/error/**","/test/**","/dispatch/**");
    }

    @Bean
    public MenuCheckInterceptor menuCheckInterceptor() {
        return new MenuCheckInterceptor();
    }
    
    
    
    
    
    /**
     * Rathon SSO context param 개발 및 로컬
     * spring boot 내장 톰캣으로 구동시 설정 load 안됨
     */
    @Bean("servletContextInitializer")
    @Profile({Constant.Profile.DEV,Constant.Profile.LOCAL})
    public ServletContextInitializer servletContextInitializerDEV() {
    	return new ServletContextInitializer() {
    		@Override
			public void onStartup(ServletContext servletContext) throws ServletException {
    			servletContext.setInitParameter("RTSSO_SP_CONFIG","/WEB-INF/sso/rathon_sso_sp_dev.properties");
			}
    	};
    }
    
    /**
     * Rathon SSO context param 운영
     * spring boot 내장 톰캣으로 구동시 설정 load 안됨
     */
    @Bean("servletContextInitializer")
    @Profile({Constant.Profile.PROD})
    public ServletContextInitializer servletContextInitializer() {
    	return new ServletContextInitializer() {
    		@Override
			public void onStartup(ServletContext servletContext) throws ServletException {
    			servletContext.setInitParameter("RTSSO_SP_CONFIG","/WEB-INF/sso/rathon_sso_sp_prod.properties");
			}
    	};
    }
    
    /**
     * Rathon SSO servlet mapping and listener
     * spring boot 내장 톰캣으로 구동시 설정 load 안됨
     */
    @Bean
    public ServletRegistrationBean<HttpServlet> ssoServletRegistrationBean() {
    	ServletRegistrationBean<HttpServlet> bean = new ServletRegistrationBean<HttpServlet>();
    	bean.setServlet(serviceProviderDispatcher());
    	bean.addUrlMappings("/dispatch/*");
    	//bean.getServlet().getServletContext().addListener(sPConfigurationListener());
    	return bean;
    }
    
    /**
     * Rathon SSO servlet
     * spring boot 내장 톰캣으로 구동시 설정 load 안됨
     * @return
     */
    @Bean
    public HttpServlet serviceProviderDispatcher() {
    	HttpServlet bean = new ServiceProviderDispatcher();
    	return bean;
    }
    
    /**
     * Rathon SSO listener
     * spring boot 내장 톰캣으로 구동시 설정 load 안됨
     */
    @Bean
    public ServletListenerRegistrationBean<ServletContextListener> ssoServletRegistrationListenerBean() {
    	ServletListenerRegistrationBean<ServletContextListener> bean = new ServletListenerRegistrationBean<ServletContextListener>();
    	bean.setListener(sPConfigurationListener());
    	return bean;
    }
    
    /**
     * Rathon SSO listener
     * spring boot 내장 톰캣으로 구동시 설정 load 안됨
     * @return
     */
    @Bean
    public ServletContextListener sPConfigurationListener() {
    	ServletContextListener bean = new SPConfigurationListener();
    	return bean;
    }
    
}
