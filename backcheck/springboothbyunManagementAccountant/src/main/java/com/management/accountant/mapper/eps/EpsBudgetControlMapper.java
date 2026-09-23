package com.management.accountant.mapper.eps;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.entity.eps.EpsBudgetControl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 预算控制数据访问接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Mapper
public interface EpsBudgetControlMapper extends BaseMapper<EpsBudgetControl> {

    /**
     * 获取预算控制监控数据
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param monitorType 监控类型
     * @return 监控数据列表
     */
    List<Map<String, Object>> selectBudgetControlMonitor(@Param("versionId") Long versionId,
                                                        @Param("organizationId") Long organizationId,
                                                        @Param("monitorType") String monitorType);

    /**
     * 获取预算控制预警信息
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param alertLevel 预警级别
     * @param alertStatus 预警状态
     * @return 预警信息列表
     */
    List<Map<String, Object>> selectBudgetControlAlerts(@Param("versionId") Long versionId,
                                                       @Param("organizationId") Long organizationId,
                                                       @Param("alertLevel") String alertLevel,
                                                       @Param("alertStatus") String alertStatus);

    /**
     * 处理预算控制预警
     * 
     * @param alertId 预警ID
     * @param handleAction 处理动作
     * @param handleRemark 处理备注
     * @return 处理结果
     */
    int handleBudgetControlAlert(@Param("alertId") Long alertId,
                               @Param("handleAction") String handleAction,
                               @Param("handleRemark") String handleRemark);

    /**
     * 获取预算控制仪表板数据
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param dashboardType 仪表板类型
     * @return 仪表板数据
     */
    Map<String, Object> selectBudgetControlDashboard(@Param("versionId") Long versionId,
                                                    @Param("organizationId") Long organizationId,
                                                    @Param("dashboardType") String dashboardType);

    /**
     * 获取预算控制图表数据
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param chartType 图表类型
     * @return 图表数据列表
     */
    List<Map<String, Object>> selectBudgetControlChartData(@Param("versionId") Long versionId,
                                                          @Param("organizationId") Long organizationId,
                                                          @Param("chartType") String chartType);

    /**
     * 获取预算控制导出数据
     * 
     * @param versionId 版本ID
     * @param exportFields 导出字段
     * @return 导出数据列表
     */
    List<Map<String, Object>> selectBudgetControlForExport(@Param("versionId") Long versionId,
                                                          @Param("exportFields") List<String> exportFields);

    /**
     * 获取预算控制统计数据
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param statisticsType 统计类型
     * @return 统计数据
     */
    Map<String, Object> selectBudgetControlStatistics(@Param("versionId") Long versionId,
                                                     @Param("organizationId") Long organizationId,
                                                     @Param("statisticsType") String statisticsType);

    /**
     * 获取预算控制历史记录
     * 
     * @param versionId 版本ID
     * @param controlType 控制类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 历史记录列表
     */
    List<Map<String, Object>> selectBudgetControlHistory(@Param("versionId") Long versionId,
                                                        @Param("controlType") String controlType,
                                                        @Param("startDate") String startDate,
                                                        @Param("endDate") String endDate);

    /**
     * 测试预算控制规则
     * 
     * @param testParams 测试参数
     * @return 测试结果
     */
    Map<String, Object> testBudgetControl(@Param("testParams") Map<String, Object> testParams);

    /**
     * 复制预算控制规则
     * 
     * @param controlId 控制规则ID
     * @param copyParams 复制参数
     * @return 复制结果
     */
    Map<String, Object> copyBudgetControl(@Param("controlId") Long controlId,
                                        @Param("copyParams") Map<String, Object> copyParams);

    /**
     * 获取预算控制模板
     * 
     * @param templateType 模板类型
     * @return 模板列表
     */
    List<Map<String, Object>> selectBudgetControlTemplates(@Param("templateType") String templateType);

    /**
     * 应用预算控制模板
     * 
     * @param templateParams 模板参数
     * @return 应用结果
     */
    Map<String, Object> applyBudgetControlTemplate(@Param("templateParams") Map<String, Object> templateParams);

    /**
     * 获取预算控制建议
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param recommendationType 建议类型
     * @return 建议列表
     */
    List<Map<String, Object>> selectBudgetControlRecommendations(@Param("versionId") Long versionId,
                                                               @Param("organizationId") Long organizationId,
                                                               @Param("recommendationType") String recommendationType);

    /**
     * 优化预算控制策略
     * 
     * @param optimizeParams 优化参数
     * @return 优化结果
     */
    Map<String, Object> optimizeBudgetControlStrategy(@Param("optimizeParams") Map<String, Object> optimizeParams);

    /**
     * 刷新预算控制缓存
     * 
     * @param versionId 版本ID
     * @param cacheType 缓存类型
     * @return 刷新结果
     */
    int refreshBudgetControlCache(@Param("versionId") Long versionId,
                                @Param("cacheType") String cacheType);

    /**
     * 实时预算控制检查
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param subjectId 科目ID
     * @param amount 金额
     * @param operationType 操作类型
     * @return 检查结果
     */
    Map<String, Object> realTimeBudgetControlCheck(@Param("versionId") Long versionId,
                                                  @Param("organizationId") Long organizationId,
                                                  @Param("subjectId") Long subjectId,
                                                  @Param("amount") Double amount,
                                                  @Param("operationType") String operationType);

    /**
     * 发送预算控制预警
     * 
     * @param versionId 版本ID
     * @param alertData 预警数据
     * @return 发送结果
     */
    int sendBudgetControlAlert(@Param("versionId") Long versionId,
                             @Param("alertData") Map<String, Object> alertData);

    /**
     * 自动预算控制调整
     * 
     * @param versionId 版本ID
     * @param adjustParams 调整参数
     * @return 调整结果
     */
    Map<String, Object> autoBudgetControlAdjust(@Param("versionId") Long versionId,
                                              @Param("adjustParams") Map<String, Object> adjustParams);

    /**
     * 验证预算控制规则
     * 
     * @param controlRule 控制规则
     * @return 验证结果
     */
    Map<String, Object> validateBudgetControlRule(@Param("controlRule") EpsBudgetControl controlRule);

    /**
     * 评估预算控制效果
     * 
     * @param versionId 版本ID
     * @param evaluationParams 评估参数
     * @return 评估结果
     */
    Map<String, Object> evaluateBudgetControlEffectiveness(@Param("versionId") Long versionId,
                                                          @Param("evaluationParams") Map<String, Object> evaluationParams);

    /**
     * 智能预算控制推荐
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @return 推荐结果
     */
    Map<String, Object> intelligentBudgetControlRecommendation(@Param("versionId") Long versionId,
                                                              @Param("organizationId") Long organizationId);

    /**
     * 预算控制风险评估
     * 
     * @param versionId 版本ID
     * @param riskParams 风险参数
     * @return 风险评估结果
     */
    Map<String, Object> assessBudgetControlRisk(@Param("versionId") Long versionId,
                                              @Param("riskParams") Map<String, Object> riskParams);

    /**
     * 预算控制合规检查
     * 
     * @param versionId 版本ID
     * @param complianceParams 合规参数
     * @return 合规检查结果
     */
    Map<String, Object> checkBudgetControlCompliance(@Param("versionId") Long versionId,
                                                    @Param("complianceParams") Map<String, Object> complianceParams);

    /**
     * 预算控制性能监控
     * 
     * @param versionId 版本ID
     * @param monitorParams 监控参数
     * @return 性能监控结果
     */
    Map<String, Object> monitorBudgetControlPerformance(@Param("versionId") Long versionId,
                                                       @Param("monitorParams") Map<String, Object> monitorParams);

    /**
     * 预算控制异常处理
     * 
     * @param versionId 版本ID
     * @param exceptionData 异常数据
     * @return 异常处理结果
     */
    Map<String, Object> handleBudgetControlException(@Param("versionId") Long versionId,
                                                    @Param("exceptionData") Map<String, Object> exceptionData);

    /**
     * 预算控制规则学习
     * 
     * @param versionId 版本ID
     * @param learningParams 学习参数
     * @return 学习结果
     */
    Map<String, Object> learnBudgetControlRules(@Param("versionId") Long versionId,
                                              @Param("learningParams") Map<String, Object> learningParams);

    /**
     * 智能预算控制预警
     * 
     * @param versionId 版本ID
     * @param alertParams 预警参数
     * @return 智能预警结果
     */
    Map<String, Object> intelligentBudgetControlAlert(@Param("versionId") Long versionId,
                                                     @Param("alertParams") Map<String, Object> alertParams);

    /**
     * 获取总预算金额
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @return 总预算金额
     */
    BigDecimal getTotalBudgetAmount(@Param("versionId") Long versionId,
                                  @Param("organizationId") Long organizationId);

    /**
     * 获取总已用金额
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @return 总已用金额
     */
    BigDecimal getTotalUsedAmount(@Param("versionId") Long versionId,
                                @Param("organizationId") Long organizationId);

    /**
     * 获取总可用金额
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @return 总可用金额
     */
    BigDecimal getTotalAvailableAmount(@Param("versionId") Long versionId,
                                     @Param("organizationId") Long organizationId);

    /**
     * 获取平均使用率
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @return 平均使用率
     */
    BigDecimal getAverageUsageRate(@Param("versionId") Long versionId,
                                 @Param("organizationId") Long organizationId);

    /**
     * 获取控制有效性
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @return 控制有效性
     */
    BigDecimal getControlEffectiveness(@Param("versionId") Long versionId,
                                     @Param("organizationId") Long organizationId);

    /**
     * 批量更新预算控制状态
     * 
     * @param controlIds 控制规则ID列表
     * @param status 状态
     * @return 更新行数
     */
    int batchUpdateControlStatus(@Param("controlIds") List<Long> controlIds,
                               @Param("status") String status);

    /**
     * 批量更新预算控制阈值
     * 
     * @param controlIds 控制规则ID列表
     * @param controlThreshold 控制阈值
     * @param warningThreshold 预警阈值
     * @return 更新行数
     */
    int batchUpdateControlThreshold(@Param("controlIds") List<Long> controlIds,
                                  @Param("controlThreshold") BigDecimal controlThreshold,
                                  @Param("warningThreshold") BigDecimal warningThreshold);

    /**
     * 获取预算控制违规记录
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 违规记录列表
     */
    List<Map<String, Object>> selectBudgetControlViolations(@Param("versionId") Long versionId,
                                                           @Param("organizationId") Long organizationId,
                                                           @Param("startDate") String startDate,
                                                           @Param("endDate") String endDate);

    /**
     * 获取预算控制趋势数据
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param trendType 趋势类型
     * @param periods 周期数
     * @return 趋势数据列表
     */
    List<Map<String, Object>> selectBudgetControlTrend(@Param("versionId") Long versionId,
                                                      @Param("organizationId") Long organizationId,
                                                      @Param("trendType") String trendType,
                                                      @Param("periods") Integer periods);

    /**
     * 计算预算控制健康度
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @return 健康度分数
     */
    BigDecimal calculateBudgetControlHealthScore(@Param("versionId") Long versionId,
                                               @Param("organizationId") Long organizationId);

    /**
     * 获取预算控制执行报告
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param reportType 报告类型
     * @param reportPeriod 报告周期
     * @return 执行报告数据
     */
    Map<String, Object> generateBudgetControlExecutionReport(@Param("versionId") Long versionId,
                                                            @Param("organizationId") Long organizationId,
                                                            @Param("reportType") String reportType,
                                                            @Param("reportPeriod") String reportPeriod);

    /**
     * 预算控制规则冲突检测
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @return 冲突检测结果
     */
    List<Map<String, Object>> detectBudgetControlRuleConflicts(@Param("versionId") Long versionId,
                                                              @Param("organizationId") Long organizationId);

    /**
     * 预算控制规则优化建议
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @return 优化建议列表
     */
    List<Map<String, Object>> generateBudgetControlOptimizationSuggestions(@Param("versionId") Long versionId,
                                                                          @Param("organizationId") Long organizationId);

    /**
     * 预算控制规则影响分析
     * 
     * @param controlId 控制规则ID
     * @param analysisParams 分析参数
     * @return 影响分析结果
     */
    Map<String, Object> analyzeBudgetControlRuleImpact(@Param("controlId") Long controlId,
                                                      @Param("analysisParams") Map<String, Object> analysisParams);

    /**
     * 预算控制自动化配置
     * 
     * @param versionId 版本ID
     * @param automationParams 自动化参数
     * @return 配置结果
     */
    Map<String, Object> configureBudgetControlAutomation(@Param("versionId") Long versionId,
                                                        @Param("automationParams") Map<String, Object> automationParams);
}
