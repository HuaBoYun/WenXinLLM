package com.financial.sharing.aop;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.beans.OperationLog;
import com.hbfk.sdk.log.context.OperationLogContext;
import com.financial.sharing.util.LegalDealUserToken;
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
		String token = request.getHeader(TOKEN);
		String requestUri = request.getRequestURI();
		String method = request.getMethod();

		// 记录请求基本信息
		log.debug("处理请求: {} {}, Token存在: {}", method, requestUri, StringUtils.hasText(token));

		// TEST: 跳过权限验证，直接使用固定测试用户
		if (requestUri.startsWith("/financial/")) {
			log.info("TEST: 财务接口跳过权限验证，使用固定测试用户，请求: {} {}", method, requestUri);

			// 创建测试用户的操作日志
			TblStaffUtil testUser = new TblStaffUtil();
			testUser.setStaffid(new java.math.BigDecimal(5555));
			testUser.setUsername("星光");
			testUser.setRealname("星光");

			OperationLog operationLog = OperationLog.builder()
					.userId("5555")
					.userName("星光")
					.userAccount("星光")
					.build();

			// 设置操作日志上下文
			OperationLogContext.setOperationLog(operationLog);

			return true;
		}

		if (StringUtils.hasText(token)) {
			try {
				// 记录token信息（脱敏处理）
				String maskedToken = token.length() > 10 ? token.substring(0, 10) + "***" : token;
				log.debug("开始解析用户token，请求: {} {}, token: {}", method, requestUri, maskedToken);

				TblStaffUtil staffUtil = LegalDealUserToken.parseUserToken(token);
				if (staffUtil == null) {
					log.warn("用户token解析失败，请求: {} {}, token: {}", method, requestUri, maskedToken);
					return true;
				}

				// 验证用户信息完整性
				if (staffUtil.getStaffid() == null) {
					log.error("用户信息缺少staffid，请求: {} {}, token: {}", method, requestUri, maskedToken);
					return true;
				}

				// 构建操作日志
				String userId = staffUtil.getStaffid().toString();
				String userName = staffUtil.getRealname() != null ? staffUtil.getRealname() : "未知用户";
				String userAccount = staffUtil.getUsername() != null ? staffUtil.getUsername() : "未知账号";

				OperationLog operationLog = OperationLog.builder()
						.userId(userId)
						.userName(userName)
						.userAccount(userAccount)
						.build();

				// 设置操作日志上下文
				OperationLogContext.setOperationLog(operationLog);

				log.info("设置操作日志上下文成功，请求: {} {}, 用户ID: {}, 用户名: {}, 账号: {}",
						method, requestUri, userId, userName, userAccount);

			} catch (Exception e) {
				// 记录异常但不阻断请求流程
				log.error("设置操作日志上下文时发生异常，请求: {} {}, token: {}",
						method, requestUri,
						token.length() > 10 ? token.substring(0, 10) + "***" : token, e);
				// 异常情况下仍然允许请求继续执行
				return true;
			}
		} else {
			log.debug("请求中未包含token，请求: {} {}", method, requestUri);
		}

		return true;
	}
}
