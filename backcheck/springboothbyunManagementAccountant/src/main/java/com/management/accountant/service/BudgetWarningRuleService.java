package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetWarningRule;
import com.management.accountant.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 预算预警规则Service接口
 * 
 * @description 预算预警规则业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetWarningRuleService {

    /**
     * 创建预警规则
     * 
     * @param rule 规则对象
     * @return 创建后的规则对象
     */
    BudgetWarningRule create(BudgetWarningRule rule);

    /**
     * 根据ID查询规则
     * 
     * @param ruleId 规则ID
     * @return 规则对象
     */
    BudgetWarningRule getById(String ruleId);

    /**
     * 更新预警规则
     * 
     * @param rule 规则对象
     */
    void update(BudgetWarningRule rule);

    /**
     * 删除预警规则
     * 
     * @param ruleId 规则ID
     */
    void delete(String ruleId);

    /**
     * 分页查询规则列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetWarningRule> getPage(Map<String, Object> params);

    /**
     * 启用规则
     * 
     * @param ruleId 规则ID
     */
    void enable(String ruleId);

    /**
     * 禁用规则
     * 
     * @param ruleId 规则ID
     */
    void disable(String ruleId);

    /**
     * 批量删除
     * 
     * @param ids 规则ID列表
     */
    void batchDelete(List<String> ids);

    /**
     * 触发预警检查
     * 
     * @param params 检查参数
     * @return 检查结果
     */
    Map<String, Object> triggerWarning(Map<String, Object> params);

    /**
     * 获取预警统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getStatistics();

    /**
     * 获取生效的预警规则列表
     * 
     * @param warningType 预警类型
     * @return 规则列表
     */
    List<BudgetWarningRule> getActiveRules(String warningType);

    /**
     * 发送预警通知
     *
     * @param ruleId 规则ID
     * @param message 预警消息
     */
    void sendWarningNotification(String ruleId, String message);

    /**
     * 批量处理预警
     *
     * @param params 批量处理参数
     * @return 处理结果
     */
    Map<String, Object> batchProcess(Map<String, Object> params);

    /**
     * 导出预警规则数据
     *
     * @param params 查询参数
     * @return 规则数据列表
     */
    List<BudgetWarningRule> exportData(Map<String, Object> params);

    /**
     * 导入预警规则数据
     *
     * @param file 导入的Excel文件
     * @return 导入结果
     */
    Map<String, Object> importData(org.springframework.web.multipart.MultipartFile file);
}

