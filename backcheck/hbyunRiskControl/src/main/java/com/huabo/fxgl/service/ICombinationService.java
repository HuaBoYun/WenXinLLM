package com.huabo.fxgl.service;

import java.util.Map;

/**
 * 指标组合分析服务接口
 * 
 * @author AI Assistant
 * @date 2025-09-27
 * @description 提供指标组合分析的完整功能服务接口
 */
public interface ICombinationService {

    // =====================================================
    // 1. 组合管理接口
    // =====================================================

    /**
     * 获取组合列表
     *
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param combinationName 组合名称
     * @param combinationCode 组合编码
     * @param category 业务分类
     * @param tag 标签（模糊匹配，匹配包含该标签的组合）
     * @param status 状态
     * @param createUser 创建人
     * @return 分页组合列表
     */
    Map<String, Object> getCombinationList(Integer pageNum, Integer pageSize, String combinationName,
                                          String combinationCode, String category, String tag, String modelType, String status, String createUser);

    /**
     * 获取组合详情
     * 
     * @param combinationId 组合ID
     * @return 组合详细信息
     */
    Map<String, Object> getCombinationDetail(String combinationId);

    /**
     * 根据组合编码获取组合详情
     * 
     * @param combinationCode 组合编码
     * @return 组合详细信息
     */
    Map<String, Object> getCombinationDetailByCode(String combinationCode);

    /**
     * 保存组合配置
     * 
     * @param combinationData 组合配置数据JSON
     * @return 保存后的组合ID
     */
    String saveCombination(String combinationData);

    /**
     * 删除组合
     * 
     * @param combinationId 组合ID
     */
    void deleteCombination(String combinationId);

    /**
     * 复制组合
     * 
     * @param combinationId 源组合ID
     * @param newCombinationName 新组合名称
     * @param newCombinationCode 新组合编码
     * @return 新组合ID
     */
    String copyCombination(String combinationId, String newCombinationName, String newCombinationCode);

    // =====================================================
    // 2. 指标管理接口
    // =====================================================

    /**
     * 获取可用指标列表
     * 
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param indicatorName 指标名称
     * @param category 业务分类
     * @param source 指标来源
     * @return 可用指标列表
     */
    Map<String, Object> getAvailableIndicators(Integer pageNum, Integer pageSize, String indicatorName, 
                                              String category, String source);

    /**
     * 添加指标到组合
     * 
     * @param indicatorData 指标配置数据JSON
     * @return 配置ID
     */
    String addIndicatorToCombination(String indicatorData);

    /**
     * 移除组合中的指标
     * 
     * @param configId 配置ID
     */
    void removeIndicatorFromCombination(String configId);

    /**
     * 更新指标配置
     * 
     * @param configData 配置数据JSON
     */
    void updateIndicatorConfig(String configData);

    /**
     * 验证指标SQL
     * 
     * @param sqlContent SQL内容
     * @param parameterConfig 参数配置JSON
     * @param testParameters 测试参数JSON
     * @return 验证结果
     */
    Map<String, Object> validateIndicatorSql(String sqlContent, String parameterConfig, String testParameters);

    // =====================================================
    // 3. 执行引擎接口
    // =====================================================

    /**
     * 执行组合分析
     * 
     * @param combinationId 组合ID
     * @param executionName 执行名称
     * @param executionMode 执行模式
     * @param parameters 执行参数JSON
     * @param enableCache 是否启用缓存
     * @return 执行信息
     */
    Map<String, Object> executeCombination(String combinationId, String executionName, String executionMode, 
                                          String parameters, Boolean enableCache);

    /**
     * 获取执行状态
     * 
     * @param executionId 执行ID
     * @return 执行状态信息
     */
    Map<String, Object> getExecutionStatus(String executionId);

    /**
     * 获取执行结果
     * 
     * @param executionId 执行ID
     * @return 执行结果
     */
    Map<String, Object> getExecutionResult(String executionId);

    /**
     * 取消执行
     * 
     * @param executionId 执行ID
     */
    void cancelExecution(String executionId);

    /**
     * 获取执行历史
     *
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param combinationId 组合ID
     * @param status 执行状态
     * @param executeUser 执行人
     * @param executionName 执行名称
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 执行历史列表
     */
    Map<String, Object> getExecutionHistory(Integer pageNum, Integer pageSize, String combinationId,
                                          String status, String executeUser, String executionName, String startDate, String endDate);

    /**
     * 删除执行记录
     *
     * @param executionId 执行ID
     */
    void deleteExecutionRecord(String executionId);

    /**
     * 强制删除执行记录（支持删除脏数据）
     *
     * @param executionId 执行ID
     * @param forceDelete 是否强制删除
     */
    void deleteExecutionRecord(String executionId, boolean forceDelete);

    /**
     * 修复脏数据
     *
     * @return 修复结果统计
     */
    Map<String, Object> fixDirtyData();

    // =====================================================
    // 4. 结果分析接口
    // =====================================================

    /**
     * 获取指标结果详情
     * 
     * @param resultId 结果ID
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 指标结果详情
     */
    Map<String, Object> getIndicatorResultDetail(String resultId, Integer pageNum, Integer pageSize);

    /**
     * 执行交集分析
     * 
     * @param analysisData 分析配置数据JSON
     * @return 交集分析结果
     */
    Map<String, Object> executeIntersectionAnalysis(String analysisData);

    /**
     * 导出分析结果
     *
     * @param exportData 导出配置数据JSON
     * @return 导出结果信息
     */
    Map<String, Object> exportAnalysisResult(String exportData);

    /**
     * 下载导出文件
     *
     * @param exportId 导出ID
     * @param response HTTP响应对象
     */
    void downloadExportFile(String exportId, javax.servlet.http.HttpServletResponse response);

    // =====================================================
    // 5. 流程图接口
    // =====================================================

    /**
     * 获取流程图配置
     * 
     * @param combinationId 组合ID
     * @return 流程图配置
     */
    Map<String, Object> getCombinationFlow(String combinationId);

    /**
     * 保存流程图配置
     * 
     * @param flowData 流程图配置数据JSON
     */
    void saveCombinationFlow(String flowData);

    /**
     * 自动生成流程图
     *
     * @param combinationId 组合ID
     * @return 生成的流程图配置
     */
    Map<String, Object> generateCombinationFlow(String combinationId);

    /**
     * 获取节点执行结果
     *
     * @param nodeId 节点ID
     * @param executionId 执行ID（可选）
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 节点执行结果
     */
    Map<String, Object> getNodeExecutionResult(String nodeId, String executionId, Integer pageNum, Integer pageSize);

    /**
     * 🔥 新增：根据指标编码获取指标执行结果
     *
     * @param indicatorCode 指标编码
     * @param combinationId 组合ID
     * @param executionId 执行ID（可选）
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 指标执行结果
     */
    Map<String, Object> getIndicatorExecutionResult(String indicatorCode, String combinationId, String executionId, Integer pageNum, Integer pageSize);

    /**
     * 🔥 新增：获取汇聚节点结果
     *
     * @param combinationId 组合ID
     * @param executionId 执行ID（可选）
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 汇聚节点结果
     */
    Map<String, Object> getMergeNodeResult(String combinationId, String executionId, Integer pageNum, Integer pageSize);

    /**
     * 获取节点详细信息
     *
     * @param nodeId 节点ID
     * @param combinationId 组合ID（可选）
     * @return 节点详细信息
     */
    Map<String, Object> getNodeDetail(String nodeId, String combinationId);

    // =====================================================
    // 6. 统计分析接口
    // =====================================================

    /**
     * 获取组合统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getCombinationStatistics();

    /**
     * 执行单个指标并创建临时表
     *
     * @param configId 指标配置ID
     * @param dataSourceId 数据源ID
     * @param sqlContent SQL内容
     * @param parameters 参数
     * @return 执行结果
     */
    Map<String, Object> executeIndicatorWithTempTable(String configId, String dataSourceId,
                                                     String sqlContent, Map<String, Object> parameters);

    // =====================================================
    // 7. 模型文档接口
    // =====================================================

    /**
     * 获取组合关联的模型文档
     *
     * @param combinationId 组合ID
     * @return 模型文档信息（docId, docName），如无关联则返回空
     */
    Map<String, Object> getModelDoc(String combinationId);
}
