package com.financial.sharing.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financial.sharing.service.ExportPermissionService;
import com.hbfk.util.JsonBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 导出权限拦截器
 */
@Slf4j
@Component
public class ExportPermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private ExportPermissionService exportPermissionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查是否为导出请求
        if (!isExportRequest(request)) {
            return true;
        }

        // 获取用户ID
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            writeErrorResponse(response, "用户未登录");
            return false;
        }

        // 获取导出类型
        String exportType = getExportTypeFromRequest(request);
        if (exportType == null) {
            exportType = "unknown";
        }

        // 检查导出权限
        if (!exportPermissionService.checkExportPermission(userId, exportType)) {
            writeErrorResponse(response, "没有导出权限或超过限制");
            return false;
        }

        // 记录导出开始
        log.info("用户{}开始导出，类型：{}，URI：{}", userId, exportType, request.getRequestURI());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (isExportRequest(request)) {
            Long userId = getUserIdFromRequest(request);
            String exportType = getExportTypeFromRequest(request);

            if (ex != null) {
                log.error("用户{}导出{}失败，错误：{}", userId, exportType, ex.getMessage());
            } else {
                log.info("用户{}导出{}成功", userId, exportType);
            }
        }
    }

    private boolean isExportRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.contains("/export/") || uri.endsWith("/export") ||
               "export".equals(request.getParameter("action"));
    }

    private Long getUserIdFromRequest(HttpServletRequest request) {
        // 从请求中获取用户ID
        // 这里需要根据实际的认证方式实现
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader != null) {
            try {
                return Long.parseLong(userIdHeader);
            } catch (NumberFormatException e) {
                log.warn("无效的用户ID格式：{}", userIdHeader);
            }
        }

        // 也可以从session或其他地方获取
        Object userIdAttribute = request.getAttribute("userId");
        if (userIdAttribute instanceof Long) {
            return (Long) userIdAttribute;
        }

        return null;
    }

    private String getExportTypeFromRequest(HttpServletRequest request) {
        // 从请求中获取导出类型
        String exportType = request.getParameter("exportType");
        if (exportType == null) {
            exportType = request.getHeader("X-Export-Type");
        }
        if (exportType == null) {
            // 从URI中解析
            String uri = request.getRequestURI();
            if (uri.contains("excel")) {
                exportType = "excel";
            } else if (uri.contains("pdf")) {
                exportType = "pdf";
            } else if (uri.contains("csv")) {
                exportType = "csv";
            } else if (uri.contains("json")) {
                exportType = "json";
            }
        }
        return exportType;
    }

    private void writeErrorResponse(HttpServletResponse response, String message) throws Exception {
        response.setStatus(403);
        response.setContentType("application/json;charset=UTF-8");

        JsonBean errorResponse = new JsonBean();
        errorResponse.setCode(0);
        errorResponse.setMsg(message);
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}