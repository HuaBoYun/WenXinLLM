package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetFreeze;
import com.management.accountant.oracle.entity.budget.BudgetFreezeHistory;
import com.management.accountant.util.PageResult;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预算冻结Service接口
 * 
 * @description 预算冻结业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetFreezeService {

    /**
     * 创建预算冻结
     * 
     * @param freeze 冻结对象
     * @return 创建后的冻结对象
     */
    BudgetFreeze create(BudgetFreeze freeze);

    /**
     * 根据ID查询冻结
     * 
     * @param freezeId 冻结ID
     * @return 冻结对象
     */
    BudgetFreeze getById(String freezeId);

    /**
     * 更新预算冻结
     * 
     * @param freeze 冻结对象
     */
    void update(BudgetFreeze freeze);

    /**
     * 删除预算冻结
     * 
     * @param freezeId 冻结ID
     */
    void delete(String freezeId);

    /**
     * 分页查询冻结列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetFreeze> getPage(Map<String, Object> params);

    /**
     * 执行冻结
     * 
     * @param freezeId 冻结ID
     */
    void executeFreeze(String freezeId);

    /**
     * 解冻
     * 
     * @param freezeId 冻结ID
     */
    void unfreeze(String freezeId);

    /**
     * 批量冻结
     * 
     * @param params 批量冻结参数
     * @return 批量冻结结果
     */
    Map<String, Object> batchFreeze(Map<String, Object> params);

    /**
     * 批量解冻
     *
     * @param params 批量解冻参数
     * @return 批量解冻结果
     */
    Map<String, Object> batchUnfreeze(Map<String, Object> params);

    /**
     * 导出冻结数据
     *
     * @param params 查询参数
     * @return 冻结数据列表
     */
    java.util.List<BudgetFreeze> exportData(Map<String, Object> params);

    /**
     * 获取统计信息
     * @return 统计数据
     */
    Map<String, Object> getStatistics();

    /**
     * 查询冻结操作历史
     *
     * @param freezeId 冻结ID
     * @return 历史记录列表
     */
    List<BudgetFreezeHistory> getHistory(String freezeId);

    /**
     * 延期冻结
     *
     * @param freezeId 冻结ID
     * @param newPlannedUnfreezeDate 新的计划解冻日期
     * @param reason 延期原因
     */
    void extendFreeze(String freezeId, Date newPlannedUnfreezeDate, String reason);

    /**
     * 导入冻结数据
     *
     * @param dataList Excel解析后的数据列表
     * @return 导入结果（successCount, failCount, failMessages）
     */
    Map<String, Object> importData(List<Map<String, Object>> dataList);
}
