package com.management.accountant.service;

import java.util.Map;

/**
 * 预算敏感性变量 Service 接口
 */
public interface BudgetSensitivityVariableService {

    /** 分页查询 */
    Map<String, Object> getPage(Map<String, Object> params);

    /** 新增 */
    Map<String, Object> createVariable(Map<String, Object> params);

    /** 更新 */
    Map<String, Object> updateVariable(Map<String, Object> params);

    /** 删除 */
    void deleteVariable(String id);

    /** 更新启用状态 */
    Map<String, Object> updateEnabled(String id, Integer enabled);
}
