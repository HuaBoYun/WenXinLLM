package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;

import java.util.Map;

/**
 * 财务组织服务接口
 * 处理财务组织、财务组织树等功能
 * 
 * @author system
 * @date 2024-12-19
 */
public interface FinanceOrgService {

    // ==================== 财务组织管理 ====================

    /**
     * 财务组织列表
     */
    PageResult<Map<String, Object>> getCwzzList(Map<String, Object> params);

    /**
     * 财务组织关联公司
     */
    boolean relateCompany(Map<String, Object> data);

    /**
     * 财务组织树列表
     */
    PageResult<Map<String, Object>> getCwzzTreeList(Map<String, Object> params);
}