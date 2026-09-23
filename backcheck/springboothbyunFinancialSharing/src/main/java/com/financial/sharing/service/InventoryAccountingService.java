package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 存货核算服务接口
 * 
 * @author system
 * @since 2026-01-26
 */
public interface InventoryAccountingService {

    /**
     * 分页查询存货核算列表
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<Map<String, Object>> getAccountingPage(Map<String, Object> param);

    /**
     * 根据ID查询存货核算详情
     * 
     * @param accountingId 核算ID
     * @return 核算详情
     */
    Map<String, Object> getAccountingById(Long accountingId);

    /**
     * 执行存货核算
     * 
     * @param param 核算参数
     * @return 是否成功
     */
    boolean executeAccounting(Map<String, Object> param);

    /**
     * 删除存货核算记录
     * 
     * @param accountingId 核算ID
     * @return 是否成功
     */
    boolean deleteAccounting(Long accountingId);

    /**
     * 批量删除存货核算记录
     * 
     * @param accountingIds 核算ID列表
     * @return 是否成功
     */
    boolean batchDeleteAccounting(List<Long> accountingIds);

    /**
     * 批量执行存货核算
     * 
     * @param accountingIds 核算ID列表
     * @return 是否成功
     */
    boolean batchExecuteAccounting(List<Long> accountingIds);

    /**
     * 获取存货核算历史
     * 
     * @param inventoryId 存货ID
     * @return 核算历史列表
     */
    List<Map<String, Object>> getAccountingHistory(Long inventoryId);

    /**
     * 获取存货核算统计
     * 
     * @param param 查询参数
     * @return 统计结果
     */
    Map<String, Object> getAccountingStatistics(Map<String, Object> param);
}

