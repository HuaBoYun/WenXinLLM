package com.huabo.fxgl.util;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.huabo.fxgl.config.OperationLogInterceptor;

@Configuration
public class WebMVCConfig extends WebMvcConfigurerAdapter {
	 @Resource
	    private OperationLogInterceptor operationLogInterceptor;
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	
    	
    	
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(operationLogInterceptor).addPathPatterns("/**");
    }
}
