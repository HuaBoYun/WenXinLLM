package com.global.treasurer.aop;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * wuqian
 */
@Order(1)
@Component
@WebFilter(value = "bodyWrapperFilter", urlPatterns = "/*")
public class BodyWrapperFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		BodyHttpServletRequestWrapper wrapper = new BodyHttpServletRequestWrapper(request);
		filterChain.doFilter(wrapper, response);
	}

}
