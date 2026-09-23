package com.management.accountant.config;

import com.management.accountant.aop.BodyWrapperFilter;
import com.management.accountant.aop.OperatorFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.FormContentFilter;

@Configuration
public class FilterConfiguration {

	@Bean
	public OperatorFilter operatorFilter() {
		return new OperatorFilter();
	}

	@Bean
	public FilterRegistrationBean<OperatorFilter> operatorFilterRegistration() {
		FilterRegistrationBean<OperatorFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(operatorFilter());
		registration.addUrlPatterns("/*");
		registration.setName("operatorFilter");
		registration.setOrder(10);
		return registration;
	}

	/**
	 * 禁用 Spring 内置的 FormContentFilter。
	 * FormContentFilter 会在 BodyWrapperFilter 之前消费掉 form-urlencoded 的请求体，
	 * 导致 BodyWrapperFilter 读不到 body，无法完成 form→JSON 的转换。
	 */
	@Bean
	public FilterRegistrationBean<FormContentFilter> formContentFilterRegistration() {
		FilterRegistrationBean<FormContentFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new FormContentFilter());
		registration.setEnabled(false);
		return registration;
	}

	/**
	 * 确保 BodyWrapperFilter 在 OperatorFilter 之前执行（order=5）。
	 * BodyWrapperFilter 本身用 @Order(1) + @Component 注解，
	 * 这里显式注册以保证顺序。
	 */
	@Bean
	public FilterRegistrationBean<BodyWrapperFilter> bodyWrapperFilterRegistration() {
		FilterRegistrationBean<BodyWrapperFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new BodyWrapperFilter());
		registration.addUrlPatterns("/*");
		registration.setName("bodyWrapperFilter");
		registration.setOrder(5);
		return registration;
	}
}
