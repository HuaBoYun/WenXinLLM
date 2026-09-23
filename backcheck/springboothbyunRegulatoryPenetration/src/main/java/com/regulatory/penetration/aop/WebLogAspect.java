package com.regulatory.penetration.aop;

import com.alibaba.fastjson.JSON;
import com.regulatory.penetration.util.SnowflakeIdWorker;
import com.google.common.collect.Maps;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.Map;

/**
 * 系统日志记录拦截器
 * wuqian
 */
@Slf4j
@Aspect
@Component
public class WebLogAspect {

	private final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(5, 5);
	private final ThreadLocal<StopWatch> stopWatchLocal = new ThreadLocal<>();
	private final ThreadLocal<LogVO> logVoLocal = new ThreadLocal<>();

	@Pointcut("within(com.huabo..*) && (@within(org.springframework.web.bind.annotation.RestController) "
			+ "||@within(org.springframework.stereotype.Controller))")
	public void webLog() {
	}


	@Around("webLog()")
	public Object interceptor(ProceedingJoinPoint joinPoint) throws Throwable {
		try {
			String id = snowflakeIdWorker.nextId() + "";
			StopWatch clock = new StopWatch(id);
			clock.start();
			stopWatchLocal.set(clock);

			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			BodyHttpServletRequestWrapper request = (BodyHttpServletRequestWrapper) attributes.getRequest();

			LogVO logVO = new LogVO();
			logVO.setUrl(request.getRequestURL().toString());
			logVO.setMethod(request.getMethod());

			StringBuilder sb = new StringBuilder();
			request.getParameterMap().forEach((k, v) -> sb.append("&").append(k).append("=").append(v != null && v.length > 0 ? v[0] : ""));
			logVO.setRequestParam(sb.length() > 1 ? sb.substring(1) : sb.toString());

			Enumeration<String> s = request.getHeaderNames();
			Map<String, String> map = Maps.newHashMap();
			while (s.hasMoreElements()) {
				String header = s.nextElement();
				map.put(header, request.getHeader(header));
			}
			logVO.setRequestHeader(JSON.toJSONString(map));
			logVO.setRequestBody(request.getJsonbody());
			logVoLocal.set(logVO);
			log.info("\n日志id: {} \n接口信息: {} ", id, logVoLocal.get());
		} catch (Exception e) {
			log.warn("日志打印不影响主线程", e);
		}
		return joinPoint.proceed();

	}


	@AfterReturning(returning = "ret", pointcut = "webLog()")
	public void doAfterReturning(Object ret) {
		try {
			StopWatch clock = stopWatchLocal.get();
			clock.stop();
			// 处理完请求，返回内容
			String returnStr = StringUtils.EMPTY;
			if (ret != null) {
				returnStr = JsonMapper.defaultMapper().toJson(ret);
				if (StringUtils.isNotBlank(returnStr) && returnStr.length() > 200) {
					returnStr = returnStr.substring(0, 200).concat("...");
				}
			}

			log.info("\n日志id: {} \n返回: {} \n耗时: {} ms ", clock.getId(), returnStr, clock.getTotalTimeMillis());

			logVoLocal.remove();
			stopWatchLocal.remove();
		} catch (Exception e) {
			log.warn("日志打印不影响主线程", e);
		}
	}

	@Data
	private static class LogVO {
		private String url;
		private String method;

		private String requestHeader;
		private String requestParam;
		private String requestBody;

		@Override
		public String toString() {
			return "url=" + url + ", method=" + method + ",\n requestHeader: " + requestHeader + ",\n requestParam: " + requestParam
					+ ",\n requestBody=" + requestBody;
		}
	}

}
