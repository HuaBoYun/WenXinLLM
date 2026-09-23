package com.financial.sharing.controller;

import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * Controller基类 - 提供通用的权限验证功能
 *
 * @author system
 * @since 2024-12-07
 */
@Slf4j
public abstract class BaseController {

    @Resource
    protected UserProvider userProvider;

    /**
     * 权限验证方法
     * @return TblStaffUtil 用户信息，验证失败返回null
     */
    protected TblStaffUtil validateUser() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                log.warn("用户信息为空，用户已失效");
                return null;
            }

            if (loginStaff.getLinkDetp() == null) {
                log.warn("用户部门信息为空，用户已失效，staffId: {}", loginStaff.getStaffid());
                return null;
            }

            if (loginStaff.getCurrentOrg() == null) {
                log.warn("用户组织信息为空，用户已失效，staffId: {}", loginStaff.getStaffid());
                return null;
            }

            log.debug("用户权限验证成功，staffId: {}, userName: {}, dept: {}",
                     loginStaff.getStaffid(), loginStaff.getUsername(), loginStaff.getLinkDetp());
            return loginStaff;

        } catch (Exception e) {
            log.error("获取用户信息异常", e);
            return null;
        }
    }

    /**
     * 权限验证并返回错误响应
     * @return 验证失败返回错误响应字符串，验证成功返回null
     */
    protected String validateUserAndReturnErrorMessage() {
        TblStaffUtil loginStaff = validateUser();
        if (loginStaff == null) {
            return "用户已失效，请重新登录";
        }
        return null;
    }

    /**
     * 权限验证并返回错误响应
     * @return 验证失败返回错误响应，验证成功返回null
     */
    protected MyJsonBean<?> validateUserAndReturnError() {
        TblStaffUtil loginStaff = validateUser();
        if (loginStaff == null) {
            return MyJsonBean.businessError("用户已失效，请重新登录");
        }
        return null;
    }

    /**
     * 权限验证，如果失败则抛出异常
     * @throws RuntimeException 权限验证失败时抛出
     */
    protected void validateUserOrThrow() {
        TblStaffUtil loginStaff = validateUser();
        if (loginStaff == null) {
            throw new RuntimeException("用户已失效，请重新登录");
        }
    }

    /**
     * 权限验证并获取用户信息
     * @param errorMessage 自定义错误消息
     * @return 用户信息，验证失败抛出业务异常
     */
    protected TblStaffUtil validateUserOrThrow(String errorMessage) {
        TblStaffUtil loginStaff = validateUser();
        if (loginStaff == null) {
            throw new RuntimeException(errorMessage != null ? errorMessage : "用户已失效，请重新登录");
        }
        return loginStaff;
    }

    /**
     * 记录API调用日志
     * @param apiName API名称
     * @param loginStaff 登录用户信息
     * @param params 请求参数
     */
    protected void logApiCall(String apiName, TblStaffUtil loginStaff, Object params) {
        log.info("API调用 - {} by 用户: {} ({}), 部门: {}, 参数: {}",
                apiName,
                loginStaff.getUsername(),
                loginStaff.getStaffid(),
                loginStaff.getLinkDetp(),
                params);
    }

    /**
     * 记录API调用成功日志
     * @param apiName API名称
     * @param loginStaff 登录用户信息
     * @param result 结果数据
     */
    protected void logApiSuccess(String apiName, TblStaffUtil loginStaff, Object result) {
        log.info("API调用成功 - {} by 用户: {}, 结果: {}",
                apiName,
                loginStaff.getUsername(),
                result != null ? "成功返回数据" : "成功无返回数据");
    }

    /**
     * 记录API调用失败日志
     * @param apiName API名称
     * @param loginStaff 登录用户信息
     * @param exception 异常信息
     */
    protected void logApiError(String apiName, TblStaffUtil loginStaff, Exception exception) {
        log.error("API调用失败 - {} by 用户: {}, 错误: {}",
                apiName,
                loginStaff.getUsername(),
                exception.getMessage(),
                exception);
    }

    /**
     * 统一的成功响应
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 成功响应
     */
    protected <T> MyJsonBean<T> success(T data) {
        return MyJsonBean.successData(data);
    }

    /**
     * 统一的失败响应
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 失败响应
     */
    protected <T> MyJsonBean<T> error(String message) {
        return MyJsonBean.errorData(message);
    }

    /**
     * 统一的业务错误响应
     * @param message 业务错误消息
     * @param <T> 数据类型
     * @return 业务错误响应
     */
    protected <T> MyJsonBean<T> businessError(String message) {
        return MyJsonBean.businessError(message);
    }
}