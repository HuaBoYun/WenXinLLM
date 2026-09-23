package com.huabo.contract.service;

import com.huabo.contract.entity.DebtCollectionLog;

import java.util.List;
import java.util.Map;

/**
 * 债权催收记录服务接口
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
public interface DebtCollectionLogService {

    /**
     * 根据债权ID查询催收记录列表
     *
     * @param debtId 债权ID
     * @return 催收记录列表
     */
    List<DebtCollectionLog> getCollectionLogsByDebtId(Long debtId);

    /**
     * 保存催收记录
     *
     * @param collectionLog 催收记录
     * @return 保存结果
     */
    boolean saveCollectionLog(DebtCollectionLog collectionLog);

    /**
     * 批量保存催收记录
     *
     * @param collectionLogs 催收记录列表
     * @return 保存结果
     */
    boolean batchSaveCollectionLogs(List<DebtCollectionLog> collectionLogs);

    /**
     * 根据ID删除催收记录
     *
     * @param id 主键ID
     * @return 删除结果
     */
    boolean deleteCollectionLog(Long id);

    /**
     * 根据ID获取催收记录详情
     *
     * @param id 主键ID
     * @return 催收记录详情
     */
    DebtCollectionLog getCollectionLogById(Long id);

    /**
     * 根据催收人员ID查询催收记录列表
     *
     * @param collectorId 催收人员ID
     * @return 催收记录列表
     */
    List<DebtCollectionLog> getCollectionLogsByCollectorId(Long collectorId);

    /**
     * 根据催收方式查询催收记录列表
     *
     * @param collectionMethod 催收方式
     * @return 催收记录列表
     */
    List<DebtCollectionLog> getCollectionLogsByMethod(Integer collectionMethod);

    /**
     * 根据催收结果查询催收记录列表
     *
     * @param collectionResult 催收结果
     * @return 催收记录列表
     */
    List<DebtCollectionLog> getCollectionLogsByResult(Integer collectionResult);

    /**
     * 获取债权催收统计信息
     *
     * @param debtId 债权ID
     * @return 统计信息
     */
    Map<String, Object> getCollectionSummary(Long debtId);

    /**
     * 获取催收数据（包含记录和统计）
     *
     * @param debtId 债权ID
     * @return 催收数据
     */
    Map<String, Object> getCollectionData(Long debtId);
}
