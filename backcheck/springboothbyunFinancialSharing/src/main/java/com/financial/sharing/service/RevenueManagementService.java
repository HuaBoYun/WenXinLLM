package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.PageableParam;

import java.util.List;
import java.util.Map;

/**
 * 收入管理服务接口
 * 提供收入确认、分配、调整、分析等功能
 * 
 * @author system
 * @date 2024-12-19
 */
public interface RevenueManagementService {

    // ==================== 收入确认管理 ====================

    /**
     * 查询收入确认列表
     * 
     * @param param 分页参数
     * @return 分页结果
     */
    PageResult<Map<String, Object>> getRevenueRecognitionList(PageableParam param);

    /**
     * 执行收入确认
     * 
     * @param param 执行参数
     * @return 执行结果
     */
    Map<String, Object> executeRevenueRecognition(Map<String, Object> param);

    /**
     * 撤销收入确认
     * 
     * @param recognitionId 确认ID
     * @param param 撤销参数
     * @return 撤销结果
     */
    Map<String, Object> revokeRevenueRecognition(String recognitionId, Map<String, Object> param);

    /**
     * 批量确认收入
     * 
     * @param param 批量参数
     * @return 批量结果
     */
    Map<String, Object> batchRevenueRecognition(Map<String, Object> param);

    /**
     * 获取收入确认统计概览
     *
     * @return 统计数据
     */
    Map<String, Object> getRevenueRecognitionStats();

    /**
     * 获取收入确认详情
     *
     * @param recognitionId 确认ID
     * @return 详情数据
     */
    Map<String, Object> getRevenueRecognitionDetail(String recognitionId);

    /**
     * 根据收入确认ID获取凭证信息
     *
     * @param recognitionId 确认ID
     * @return 凭证数据
     */
    Map<String, Object> getRecognitionVoucher(String recognitionId);

    // ==================== 收入分配管理 ====================

    /**
     * 获取收入分配列表
     * 
     * @param param 分页参数
     * @return 分页结果
     */
    PageResult<Map<String, Object>> getRevenueAllocationList(PageableParam param);

    /**
     * 创建收入分配
     * 
     * @param param 创建参数
     * @return 创建结果
     */
    Map<String, Object> createRevenueAllocation(Map<String, Object> param);

    /**
     * 执行收入分配
     * 
     * @param allocationId 分配ID
     * @return 执行结果
     */
    Map<String, Object> executeRevenueAllocation(String allocationId);

    /**
     * 获取分配规则列表
     * 
     * @return 规则列表
     */
    List<Map<String, Object>> getAllocationRuleList();

    // ==================== 合同收入管理 ====================

    /**
     * 获取合同收入列表
     * 
     * @param param 分页参数
     * @return 分页结果
     */
    PageResult<Map<String, Object>> getContractRevenueList(PageableParam param);

    /**
     * 创建合同收入
     * 
     * @param param 创建参数
     * @return 创建结果
     */
    Map<String, Object> createContractRevenue(Map<String, Object> param);

    /**
     * 识别履约义务
     * 
     * @param contractId 合同ID
     * @return 识别结果
     */
    Map<String, Object> identifyPerformanceObligations(String contractId);

    /**
     * 更新履约进度
     * 
     * @param contractId 合同ID
     * @param param 更新参数
     * @return 更新结果
     */
    Map<String, Object> updatePerformanceProgress(String contractId, Map<String, Object> param);

    // ==================== 递延收入管理 ====================

    /**
     * 获取递延收入列表
     * 
     * @param param 分页参数
     * @return 分页结果
     */
    PageResult<Map<String, Object>> getDeferredRevenueList(PageableParam param);

    /**
     * 创建递延收入
     * 
     * @param param 创建参数
     * @return 创建结果
     */
    Map<String, Object> createDeferredRevenue(Map<String, Object> param);

    /**
     * 分期确认递延收入
     * 
     * @param deferredId 递延ID
     * @param param 确认参数
     * @return 确认结果
     */
    Map<String, Object> recognizeDeferredRevenue(String deferredId, Map<String, Object> param);

    /**
     * 获取确认计划
     *
     * @param deferredId 递延ID
     * @return 计划列表
     */
    Map<String, Object> getRecognitionSchedule(String deferredId);

    /**
     * 获取递延收入统计
     *
     * @return 统计结果
     */
    Map<String, Object> getDeferredRevenueStats();

    // ==================== 收入调整管理 ====================

    /**
     * 获取收入调整列表
     * 
     * @param param 分页参数
     * @return 分页结果
     */
    PageResult<Map<String, Object>> getRevenueAdjustmentList(PageableParam param);

    /**
     * 创建收入调整
     * 
     * @param param 创建参数
     * @return 创建结果
     */
    Map<String, Object> createRevenueAdjustment(Map<String, Object> param);

    /**
     * 审批收入调整
     * 
     * @param adjustmentId 调整ID
     * @param param 审批参数
     * @return 审批结果
     */
    Map<String, Object> approveRevenueAdjustment(String adjustmentId, Map<String, Object> param);

    /**
     * 执行收入调整
     * 
     * @param adjustmentId 调整ID
     * @return 执行结果
     */
    Map<String, Object> executeRevenueAdjustment(String adjustmentId);

    /**
     * 获取调整影响分析
     *
     * @param adjustmentId 调整ID
     * @return 影响分析结果
     */
    Map<String, Object> getAdjustmentImpactAnalysis(String adjustmentId);

    // ==================== 收入结构分析管理 ====================

    /**
     * 获取收入结构分析列表（分页）
     *
     * @param param 分页参数
     * @return 分页结果
     */
    PageResult<Map<String, Object>> getRevenueStructureAnalysisList(PageableParam param);
}

