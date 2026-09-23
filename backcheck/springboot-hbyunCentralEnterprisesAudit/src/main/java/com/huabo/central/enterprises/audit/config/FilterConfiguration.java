package com.huabo.central.enterprises.audit.config;

import com.huabo.central.enterprises.audit.aop.OperatorFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
		registration.setOrder(1);
		return registration;
	}
}
