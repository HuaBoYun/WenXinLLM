package com.management.accountant.aop;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * wuqian
 * 注意：不使用 @Component，由 FilterConfiguration 统一注册并控制 order
 */
public class BodyWrapperFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		BodyHttpServletRequestWrapper wrapper = new BodyHttpServletRequestWrapper(request);
		filterChain.doFilter(wrapper, response);
	}

}
