package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;

import java.util.Map;

/**
 * 财务账簿服务接口
 * 处理账簿管理、账簿类型、账簿授权等功能
 * 
 * @author system
 * @date 2024-12-19
 */
public interface FinanceBookService {

    // ==================== 账簿管理 ====================

    /**
     * 账簿管理列表
     */
    PageResult<Map<String, Object>> getZbglList(Map<String, Object> params);

    /**
     * 账簿管理详情
     */
    Map<String, Object> getZbglDetail(Map<String, Object> params);

    /**
     * 账簿管理新增
     */
    Map<String, Object> saveZbgl(Map<String, Object> data);

    /**
     * 账簿管理删除
     */
    boolean deleteZbgl(Map<String, Object> data);

    // ==================== 账簿类型管理 ====================

    /**
     * 账簿类型列表
     */
    PageResult<Map<String, Object>> getZblxList(Map<String, Object> params);

    /**
     * 账簿类型详情
     */
    Map<String, Object> getZblxDetail(Map<String, Object> params);

    /**
     * 账簿类型新增
     */
    Map<String, Object> saveZblx(Map<String, Object> data);

    /**
     * 账簿类型删除
     */
    boolean deleteZblx(Map<String, Object> data);

    // ==================== 账簿授权管理 ====================

    /**
     * 账簿授权角色
     */
    boolean zbglAuthRole(Map<String, Object> data);

    /**
     * 账簿授权角色列表
     */
    PageResult<Map<String, Object>> zbglAuthRoleList(Map<String, Object> params);

    /**
     * 取消授权
     */
    boolean zbglCancelAuthRole(Map<String, Object> data);

    /**
     * 获取公司账簿授权角色列表
     */
    PageResult<Map<String, Object>> getGsZbglAuthRoleList(Map<String, Object> params);

    /**
     * 公司账簿选择
     */
    boolean sureGsZb(Map<String, Object> data);
}