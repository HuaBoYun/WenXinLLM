package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Control Service接口
 * 
 * @description Control业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetControlService {

    /**
     * createControlRule
     */
    Map<String, Object> createControlRule(Map<String, Object> params, String companyId, String userId);

    /**
     * getControlRule
     */
    Map<String, Object> getControlRule(Map<String, Object> params, String companyId, String userId);

    /**
     * enableControlRule
     */
    Map<String, Object> enableControlRule(Map<String, Object> params, String companyId, String userId);

    /**
     * batchExecuteBudgetControl
     */
    Map<String, Object> batchExecuteBudgetControl(Map<String, Object> params, String companyId, String userId);

    /**
     * getControlCheckResult
     */
    Map<String, Object> getControlCheckResult(String companyId, String userId);

    /**
     * restartControlEngine
     */
    Map<String, Object> restartControlEngine(Map<String, Object> params, String companyId, String userId);

    /**
     * createWarningRule
     */
    Map<String, Object> createWarningRule(Map<String, Object> params, String companyId, String userId);

    /**
     * getWarningRule
     */
    Map<String, Object> getWarningRule(Map<String, Object> params, String companyId, String userId);

    /**
     * getWarningRecordPage
     */
    Map<String, Object> getWarningRecordPage(Map<String, Object> params, String companyId, String userId);

    /**
     * handleWarningRecord
     */
    Map<String, Object> handleWarningRecord(Map<String, Object> params, String companyId, String userId);

    /**
     * createMonitorTask
     */
    Map<String, Object> createMonitorTask(Map<String, Object> params, String companyId, String userId);

    /**
     * getMonitorTask
     */
    Map<String, Object> getMonitorTask(Map<String, Object> params, String companyId, String userId);

    /**
     * startMonitorTask
     */
    Map<String, Object> startMonitorTask(String companyId, String userId);

    /**
     * getControlLogPage
     */
    Map<String, Object> getControlLogPage(Map<String, Object> params, String companyId, String userId);

    /**
     * getControlLogDetail
     */
    Map<String, Object> getControlLogDetail(Map<String, Object> params, String companyId, String userId);

    /**
     * getControlStats
     */
    Map<String, Object> getControlStats(String companyId, String userId);

    /**
     * getControlHealth
     */
    Map<String, Object> getControlHealth(String companyId, String userId);

}
