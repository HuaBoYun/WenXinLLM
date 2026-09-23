package com.financial.sharing.config;

import com.financial.sharing.aop.OperationLogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

	@Resource
	private OperationLogInterceptor operationLogInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(operationLogInterceptor).addPathPatterns("/**");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		// 添加LocalDate转换器
		registry.addConverter(new Converter<String, LocalDate>() {
			@Override
			public LocalDate convert(String source) {
				if (source == null || source.isEmpty()) {
					return null;
				}
				try {
					return LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				} catch (Exception e) {
					return null;
				}
			}
		});

		// 添加LocalDateTime转换器
		registry.addConverter(new Converter<String, LocalDateTime>() {
			@Override
			public LocalDateTime convert(String source) {
				if (source == null || source.isEmpty()) {
					return null;
				}
				try {
					return LocalDateTime.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				} catch (Exception e) {
					return null;
				}
			}
		});

		// 添加Integer转换器（处理空字符串）
		registry.addConverter(new Converter<String, Integer>() {
			@Override
			public Integer convert(String source) {
				if (source == null || source.isEmpty()) {
					return null;
				}
				try {
					return Integer.valueOf(source);
				} catch (NumberFormatException e) {
					return null;
				}
			}
		});

		// 添加Long转换器（处理空字符串）
		registry.addConverter(new Converter<String, Long>() {
			@Override
			public Long convert(String source) {
				if (source == null || source.isEmpty()) {
					return null;
				}
				try {
					return Long.valueOf(source);
				} catch (NumberFormatException e) {
					return null;
				}
			}
		});

		// 添加Double转换器（处理空字符串）
		registry.addConverter(new Converter<String, Double>() {
			@Override
			public Double convert(String source) {
				if (source == null || source.isEmpty()) {
					return null;
				}
				try {
					return Double.valueOf(source);
				} catch (NumberFormatException e) {
					return null;
				}
			}
		});
	}

	// CORS配置已移除,由Gateway统一处理CORS
	// 避免重复的Access-Control-Allow-Origin响应头导致浏览器CORS错误
}
