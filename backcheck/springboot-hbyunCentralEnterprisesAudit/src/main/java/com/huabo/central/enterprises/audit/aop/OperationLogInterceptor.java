package com.huabo.central.enterprises.audit.aop;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.beans.OperationLog;
import com.hbfk.sdk.log.context.OperationLogContext;
import com.hbfk.util.user.UserProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class OperationLogInterceptor implements HandlerInterceptor {
	private static final String TOKEN = "token";
	
	@Resource
	private UserProvider userProvider;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		try {
			String token = request.getHeader(TOKEN);
			if (StringUtils.hasText(token)) {
				TblStaffUtil staffUtil = userProvider.get();
				if (staffUtil == null) {
					return true;
				}
				// 补充用户信息
				OperationLogContext.setOperationLog(OperationLog.builder().userId(staffUtil.getStaffid().toString()).userName(staffUtil.getRealname())
						.userAccount(staffUtil.getUsername()).build());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
