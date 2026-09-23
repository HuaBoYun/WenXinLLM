package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblComplianceResult;

import java.util.List;
import java.util.Map;

/**
 * 合规检查结果服务接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
public interface ComplianceResultService {

    /**
     * 分页查询合规检查结果列表
     *
     * @param params 查询参数
     * @return 分页结果
     */
    PageInfo<TblComplianceResult> getResultList(Map<String, Object> params);

    /**
     * 根据ID查询合规检查结果详情
     *
     * @param resultId 结果ID
     * @return 合规检查结果
     */
    TblComplianceResult getResultById(String resultId);

    /**
     * 保存合规检查结果（新增或更新）
     *
     * @param result 合规检查结果
     * @return 保存后的合规检查结果
     */
    TblComplianceResult saveResult(TblComplianceResult result);

    /**
     * 删除合规检查结果
     *
     * @param resultId 结果ID
     */
    void deleteResult(String resultId);

    /**
     * 批量删除合规检查结果
     *
     * @param resultIds 结果ID列表
     */
    void batchDeleteResults(List<String> resultIds);

    /**
     * 查询需要关注的检查结果
     *
     * @return 合规检查结果列表
     */
    List<TblComplianceResult> getResultsNeedingAttention();

    /**
     * 查询需要立即处理的检查结果
     *
     * @return 合规检查结果列表
     */
    List<TblComplianceResult> getResultsNeedingImmediateAction();

    /**
     * 处理合规检查结果
     *
     * @param resultId 结果ID
     * @param actionTaken 采取的措施
     * @return 处理后的结果
     */
    TblComplianceResult processResult(String resultId, String actionTaken);

    /**
     * 解决合规问题
     *
     * @param resultId 结果ID
     * @param resolution 解决方案
     * @return 解决后的结果
     */
    TblComplianceResult resolveResult(String resultId, String resolution);

    /**
     * 升级合规问题
     *
     * @param resultId 结果ID
     * @param escalateReason 升级原因
     * @return 升级后的结果
     */
    TblComplianceResult escalateResult(String resultId, String escalateReason);

    /**
     * 批量处理合规检查结果
     */
    void batchProcessResults(List<String> resultIds);

    /**
     * 批量解决合规检查结果
     */
    void batchResolveResults(List<String> resultIds);

    /**
     * 批量升级合规检查结果
     */
    void batchEscalateResults(List<String> resultIds);

    /**
     * 获取合规检查统计信息
     */
    Map<String, Object> getComplianceStatistics();

    /**
     * 导出合规检查结果数据
     */
    List<TblComplianceResult> exportResults(Map<String, Object> params);
}

