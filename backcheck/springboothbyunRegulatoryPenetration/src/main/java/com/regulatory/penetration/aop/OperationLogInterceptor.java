package com.regulatory.penetration.aop;

import com.regulatory.penetration.util.LegalDealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.beans.OperationLog;
import com.hbfk.sdk.log.context.OperationLogContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class OperationLogInterceptor implements HandlerInterceptor {
	private static final String TOKEN = "token";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String token = request.getHeader(TOKEN);
		if (StringUtils.hasText(token)) {
			TblStaffUtil staffUtil = LegalDealUserToken.parseUserToken(token);
			if (staffUtil == null) {
				return true;
			}
			// 补充用户信息
			OperationLogContext.setOperationLog(OperationLog.builder().userId(staffUtil.getStaffid().toString()).userName(staffUtil.getRealname())
					.userAccount(staffUtil.getUsername()).build());
		}
		return true;
	}
}
