package com.management.accountant.aop;

import com.management.accountant.util.LegalDealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.beans.OperationLog;
import com.hbfk.sdk.log.context.OperationLogContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class OperationLogInterceptor implements HandlerInterceptor {
	private static final String TOKEN = "token";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		try {
			String token = request.getHeader(TOKEN);
			if (StringUtils.hasText(token)) {
				TblStaffUtil staffUtil = LegalDealUserToken.parseUserToken(token);
				if (staffUtil != null) {
					// 补充用户信息
					OperationLogContext.setOperationLog(OperationLog.builder().userId(staffUtil.getStaffid().toString()).userName(staffUtil.getRealname())
							.userAccount(staffUtil.getUsername()).build());
				}
			}
		} catch (Exception e) {
			// Token解析或Redis异常不影响业务请求，仅打印简要信息
			log.debug("[OperationLogInterceptor] 用户信息解析失败，跳过日志上下文设置: {}", e.getMessage());
		}
		return true;
	}
}
