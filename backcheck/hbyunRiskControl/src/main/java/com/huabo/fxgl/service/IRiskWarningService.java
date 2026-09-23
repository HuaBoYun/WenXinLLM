package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.TblRiskWarning;

import java.util.List;
import java.util.Map;

/**
 * 风险预警服务接口
 * 
 * @author 华博云开发团队
 * @since 2025-10-07
 */
public interface IRiskWarningService extends IService<TblRiskWarning> {

    /**
     * 分页查询风险预警列表
     * 
     * @param queryParams 查询参数
     * @return 分页结果
     */
    JsonBean getRiskWarningList(Map<String, Object> queryParams);

    /**
     * 获取风险预警详情
     * 
     * @param warningId 预警ID
     * @return 预警详情
     */
    JsonBean getRiskWarningDetail(String warningId);

    /**
     * 处理风险预警
     * 
     * @param warningId 预警ID
     * @param status 处理状态
     * @param handler 处理人
     * @param handleRemark 处理备注
     * @return 处理结果
     */
    JsonBean processRiskWarning(String warningId, String status, String handler, String handleRemark);

    /**
     * 批量处理风险预警
     * 
     * @param warningIds 预警ID列表
     * @param status 处理状态
     * @param handler 处理人
     * @param handleRemark 处理备注
     * @return 处理结果
     */
    JsonBean batchProcessRiskWarning(List<String> warningIds, String status, String handler, String handleRemark);

    /**
     * 获取预警统计信息
     *
     * @return 统计信息
     */
    JsonBean getWarningStatistics();

    /**
     * 🔧 新增：获取今日新增预警数量
     *
     * @return 今日新增数量
     */
    JsonBean getTodayWarningCount();

    /**
     * 生成预警报告
     * 
     * @param reportParams 报告参数
     * @return 报告生成结果
     */
    JsonBean generateWarningReport(Map<String, Object> reportParams);

    /**
     * 获取预警配置
     * 
     * @return 预警配置
     */
    JsonBean getWarningConfig();

    /**
     * 更新预警配置
     * 
     * @param configParams 配置参数
     * @return 更新结果
     */
    JsonBean updateWarningConfig(Map<String, Object> configParams);

    /**
     * 创建风险预警
     * 
     * @param evalModelId 评估模型ID
     * @param modelName 模型名称
     * @param riskLevel 风险等级
     * @param riskScore 风险分数
     * @param warningContent 预警内容
     * @param businessScenario 业务场景
     * @param triggerCondition 触发条件
     * @param relatedData 相关数据
     * @return 创建结果
     */
    JsonBean createRiskWarning(String evalModelId, String modelName, String riskLevel, 
                              Double riskScore, String warningContent, String businessScenario,
                              String triggerCondition, String relatedData);

    /**
     * 标记预警为已读
     * 
     * @param warningId 预警ID
     * @return 标记结果
     */
    JsonBean markAsRead(String warningId);

    /**
     * 批量标记预警为已读
     * 
     * @param warningIds 预警ID列表
     * @return 标记结果
     */
    JsonBean batchMarkAsRead(List<String> warningIds);

    /**
     * 根据评估模型ID获取相关预警
     * 
     * @param evalModelId 评估模型ID
     * @return 相关预警列表
     */
    List<TblRiskWarning> getWarningsByModelId(String evalModelId);

    /**
     * 获取最近的预警记录
     * 
     * @param limit 限制数量
     * @return 最近的预警列表
     */
    List<TblRiskWarning> getRecentWarnings(Integer limit);

    /**
     * 自动生成风险预警
     * 基于评估模型的测试结果自动生成预警
     *
     * @param evalModelId 评估模型ID
     * @param testResult 测试结果
     * @return 生成结果
     */
    JsonBean autoGenerateWarning(String evalModelId, Map<String, Object> testResult);

    // =====================================================
    // 评估模型预警功能增强接口
    // =====================================================

    /**
     * 获取模型预警数量
     *
     * @param modelId 评估模型ID
     * @return 预警数量统计
     */
    JsonBean getModelWarningCount(String modelId);

    /**
     * 🔧 新增：获取模型待处理预警数量（只统计PENDING和PROCESSING状态）
     *
     * @param modelId 评估模型ID
     * @return 待处理预警数量统计
     */
    JsonBean getModelPendingWarningCount(String modelId);

    /**
     * 批量获取所有评估模型的预警数量与待处理预警数量
     * 一次GROUP BY查询替代逐模型两次COUNT，fxyj2页面专用
     *
     * @return 模型预警数量列表
     */
    JsonBean getModelWarningCountBatch();

    /**
     * 按预警状态获取统计明细（总数、按预警级别分组、按模型分组）
     * fxyj2页面专用
     *
     * @param warningStatus 预警状态
     * @return 统计明细
     */
    JsonBean getWarningStatusDetail(String warningStatus);

    /**
     * 获取模型预警结果
     *
     * @param requestBody 查询参数
     * @return 预警结果列表
     */
    JsonBean getModelWarningResults(Map<String, Object> requestBody);

    /**
     * 获取预警源数据
     *
     * @param requestBody 查询参数
     * @return 源数据列表
     */
    JsonBean getWarningSourceData(Map<String, Object> requestBody);

    /**
     * 获取预警源数据详情
     *
     * @param requestBody 查询参数
     * @return 预警源数据详情
     */
    JsonBean getWarningSourceDataDetail(Map<String, Object> requestBody);

    /**
     * 生成单条预警报告
     *
     * @param requestBody 报告生成参数
     * @return 生成结果
     */
    JsonBean generateSingleWarningReport(Map<String, Object> requestBody);

    /**
     * 生成模型整体报告
     *
     * @param requestBody 报告生成参数
     * @return 生成结果
     */
    JsonBean generateModelWarningReport(Map<String, Object> requestBody);

    /**
     * 获取预警数据穿透分析
     *
     * @param requestBody 查询参数，包含warningId、pageNum、pageSize等
     * @return 穿透数据结果
     */
    String getWarningDrillDownData(Map<String, Object> requestBody);

    /**
     * 导出预警穿透数据
     *
     * @param requestBody 导出参数，包含warningId、exportFormat等
     * @return 导出结果
     */
    String exportWarningDrillDownData(Map<String, Object> requestBody);
}
