package com.global.treasurer.service;

import com.global.treasurer.entity.TblPendingData;

import java.util.List;
import java.util.Map;

/**
 * 待结算数据服务接口
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
public interface PendingDataService {

    /**
     * 分页查询待结算数据
     *
     * @param params 查询参数
     * @return 分页结果
     */
    Map<String, Object> getPendingDataPage(Map<String, Object> params);

    /**
     * 根据ID查询待结算数据
     *
     * @param pendingId 待结算ID
     * @return 待结算数据
     */
    TblPendingData getPendingDataById(Long pendingId);

    /**
     * 创建待结算数据
     *
     * @param pendingData 待结算数据
     * @return 影响行数
     */
    int createPendingData(TblPendingData pendingData);

    /**
     * 更新待结算数据
     *
     * @param pendingData 待结算数据
     * @return 影响行数
     */
    int updatePendingData(TblPendingData pendingData);

    /**
     * 删除待结算数据
     *
     * @param pendingIds 待结算ID列表
     * @return 影响行数
     */
    int deletePendingData(List<Long> pendingIds);

    /**
     * 批量更新结算状态
     *
     * @param ids 待结算ID列表
     * @param status 结算状态
     * @return 影响行数
     */
    int batchUpdateStatus(List<Long> ids, String status);

    /**
     * 审批待结算数据
     *
     * @param pendingId 待结算ID
     * @param approvalStatus 审批状态
     * @param approvalBy 审批人
     * @param approvalOpinion 审批意见
     * @return 影响行数
     */
    int approvePendingData(Long pendingId, String approvalStatus, Long approvalBy, String approvalOpinion);

    /**
     * 查询高优先级待结算数据
     *
     * @param orgId 组织ID
     * @return 待结算数据列表
     */
    List<TblPendingData> getHighPriorityPending(Long orgId);

    /**
     * 查询逾期待结算数据
     *
     * @param orgId 组织ID
     * @return 待结算数据列表
     */
    List<TblPendingData> getOverduePending(Long orgId);

    /**
     * 查询大额待结算数据
     *
     * @param params 查询参数
     * @return 待结算数据列表
     */
    List<TblPendingData> getLargeAmountPending(Map<String, Object> params);

    /**
     * 统计待结算数据概要
     *
     * @param orgId 组织ID
     * @return 统计数据
     */
    Map<String, Object> getPendingDataSummary(Long orgId);

    /**
     * 风险评估
     *
     * @param pendingId 待结算ID
     * @return 风险评估结果
     */
    Map<String, Object> assessPendingDataRisk(Long pendingId);

    /**
     * 导出待结算数据
     *
     * @param params 查询参数
     * @return 待结算数据列表
     */
    List<TblPendingData> exportPendingData(Map<String, Object> params);
}
