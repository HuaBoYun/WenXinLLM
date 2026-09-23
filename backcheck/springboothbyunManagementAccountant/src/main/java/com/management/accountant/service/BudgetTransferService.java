package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetTransfer;
import com.management.accountant.util.PageResult;

import java.util.Map;

/**
 * 预算转移Service接口
 * 
 * @description 预算转移业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetTransferService {

    /**
     * 创建转移申请
     * 
     * @param transfer 转移对象
     * @return 创建后的转移对象
     */
    BudgetTransfer create(BudgetTransfer transfer);

    /**
     * 根据ID查询转移
     * 
     * @param transferId 转移ID
     * @return 转移对象
     */
    BudgetTransfer getById(String transferId);

    /**
     * 更新转移申请
     * 
     * @param transfer 转移对象
     */
    void update(BudgetTransfer transfer);

    /**
     * 删除转移申请
     * 
     * @param transferId 转移ID
     */
    void delete(String transferId);

    /**
     * 分页查询转移列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetTransfer> getPage(Map<String, Object> params);

    /**
     * 审批转移申请
     * 
     * @param params 审批参数
     */
    void approve(Map<String, Object> params);

    /**
     * 执行转移
     * 
     * @param transferId 转移ID
     */
    void execute(String transferId);

    /**
     * 批量转移
     *
     * @param params 批量参数
     * @return 批量结果
     */
    Map<String, Object> batchTransfer(Map<String, Object> params);

    /**
     * 导出转移数据
     *
     * @param params 查询参数
     * @return 转移数据列表
     */
    java.util.List<BudgetTransfer> exportData(Map<String, Object> params);

    /**
     * 获取统计信息
     * @return 统计数据
     */
    Map<String, Object> getStatistics();

    /**
     * 根据ID更新转移记录
     *
     * @param transfer 转移对象
     * @return 是否成功
     */
    boolean updateById(BudgetTransfer transfer);

    /**
     * 保存转移记录
     *
     * @param transfer 转移对象
     * @return 是否成功
     */
    boolean save(BudgetTransfer transfer);
}

