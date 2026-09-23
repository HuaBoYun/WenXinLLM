package com.huabo.audit.config;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.beans.OperationLog;
import com.hbfk.sdk.log.context.OperationLogContext;
import com.hbfk.util.user.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 操作日志拦截器
 * 用于在请求处理前设置操作日志上下文信息
 *
 * @author Augment Code AI
 * @date 2025-01-21
 */
@Slf4j
@Component
public class OperationLogInterceptor implements HandlerInterceptor {
    private static final String TOKEN = "token";

    @Resource
    private UserProvider userProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(TOKEN);
        if (StringUtils.hasText(token)) {
            TblStaffUtil staffUtil;
            try {
                staffUtil = userProvider.get();
            } catch (Exception e) {
                log.warn("获取用户信息失败，跳过操作日志上下文设置", e);
                return true;
            }
            if (staffUtil == null) {
                log.debug("用户信息为空，跳过操作日志上下文设置");
                return true;
            }

            // 设置操作日志上下文
            OperationLog operationLog = OperationLog.builder()
                    .userId(staffUtil.getStaffid().toString())
                    .userName(staffUtil.getRealname())
                    .userAccount(staffUtil.getUsername())
                    .build();
            OperationLogContext.setOperationLog(operationLog);
            log.debug("操作日志上下文已设置 - 用户ID: {}, 用户名: {}", staffUtil.getStaffid(), staffUtil.getRealname());
        }
        return true;
    }
}


