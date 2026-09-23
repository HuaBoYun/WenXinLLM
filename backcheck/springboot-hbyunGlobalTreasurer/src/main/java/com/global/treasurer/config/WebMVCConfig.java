package com.global.treasurer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.global.treasurer.resolver.FlexibleRequestBodyArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;
import java.util.List;

/**
 * Web MVC 配置类
 * 注册自定义参数解析器,支持同时处理 JSON 和 form-data 格式
 *
 * @author 华博云开发团队
 * @since 2026-02-12
 */
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
	// 操作日志拦截器已禁用，避免密标依赖冲突

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		// 注册灵活的请求体参数解析器
		// 将其放在解析器列表的第一位,优先使用
		resolvers.add(0, new FlexibleRequestBodyArgumentResolver(objectMapper));
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	/**
	 * 配置 MultipartResolver 以支持文件上传和 multipart/form-data
	 */
	@Bean
	public MultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}

	/**
	 * 配置 MultipartConfigFactory
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		return new MultipartConfigElement("", 10485760, 10485760, 0);
	}
}
