package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetReminder;
import com.management.accountant.util.PageResult;

import java.util.Map;

/**
 * 预算催报管理Service接口
 *
 * @description 预算催报管理业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetReminderService {

    /**
     * 创建催报配置
     *
     * @param reminder 催报对象
     * @return 创建后的催报对象
     */
    BudgetReminder create(BudgetReminder reminder);

    /**
     * 更新催报配置
     *
     * @param reminder 催报对象
     */
    void update(BudgetReminder reminder);

    /**
     * 删除催报配置
     *
     * @param reminderId 催报ID
     */
    void delete(String reminderId);

    /**
     * 分页查询催报配置
     *
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetReminder> getPage(Map<String, Object> params);

    /**
     * 执行催报
     *
     * @param reminderId 催报ID
     * @return 执行结果
     */
    Map<String, Object> executeReminder(String reminderId);

    /**
     * 批量催报
     *
     * @param params 批量参数
     * @return 批量结果
     */
    Map<String, Object> batchRemind(Map<String, Object> params);

    /**
     * 获取催报记录
     *
     * @param params 查询参数
     * @return 催报记录
     */
    PageResult<Map<String, Object>> getReminderRecords(Map<String, Object> params);

    /**
     * 启用催报策略
     *
     * @param reminderId 催报ID
     */
    void enable(String reminderId);

    /**
     * 禁用催报策略
     *
     * @param reminderId 催报ID
     */
    void disable(String reminderId);

    /**
     * 测试催报策略
     *
     * @param reminderId 催报ID
     * @return 测试结果
     */
    Map<String, Object> test(String reminderId);

    /**
     * 复制催报策略
     *
     * @param reminderId 催报ID
     * @return 复制后的催报对象
     */
    BudgetReminder copy(String reminderId);

    /**
     * 获取催报统计数据
     *
     * @param params 查询参数
     * @return 统计数据
     */
    Map<String, Object> getStats(Map<String, Object> params);

    /**
     * 获取催报目标列表
     *
     * @param reminderId 催报ID
     * @return 目标列表
     */
    Map<String, Object> getTargets(String reminderId);

    /**
     * 获取催报策略列表
     *
     * @param params 查询参数
     * @return 策略列表
     */
    Map<String, Object> getList(Map<String, Object> params);
}

