package com.management.accountant.service;

import java.util.Map;

/**
 * 预算场景变量 Service 接口
 */
public interface BudgetScenarioVariableService {

    /** 分页查询变量列表 */
    Map<String, Object> getPage(Map<String, Object> params);

    /** 新增变量 */
    Map<String, Object> createVariable(Map<String, Object> params);

    /** 更新变量 */
    Map<String, Object> updateVariable(Map<String, Object> params);

    /** 删除变量 */
    void deleteVariable(String id);
}
