package com.management.accountant.service.eps;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.eps.EpsBudgetControl;

import java.util.List;
import java.util.Map;

/**
 * 预算控制服务接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
public interface EpsBudgetControlService {

    /**
     * 分页查询预算控制规则
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param subjectId 科目ID
     * @param controlType 控制类型
     * @param controlStatus 控制状态
     * @return 分页结果
     */
    IPage<EpsBudgetControl> queryBudgetControlPage(Long current, Long size, Long versionId,
                                                  Long organizationId, Long subjectId,
                                                  String controlType, String controlStatus);

    /**
     * 创建预算控制规则
     * 
     * @param budgetControl 预算控制规则
     * @return 创建结果
     */
    boolean createBudgetControl(EpsBudgetControl budgetControl);

    /**
     * 更新预算控制规则
     * 
     * @param budgetControl 预算控制规则
     * @return 更新结果
     */
    boolean updateBudgetControl(EpsBudgetControl budgetControl);

    /**
     * 删除预算控制规则
     * 
     * @param controlId 控制规则ID
     * @return 删除结果
     */
    boolean deleteBudgetControl(Long controlId);

    /**
     * 根据ID查询预算控制规则
     * 
     * @param controlId 控制规则ID
     * @return 预算控制规则
     */
    EpsBudgetControl getBudgetControlById(Long controlId);

    /**
     * 启用预算控制规则
     * 
     * @param controlId 控制规则ID
     * @return 启用结果
     */
    boolean enableBudgetControl(Long controlId);

    /**
     * 禁用预算控制规则
     * 
     * @param controlId 控制规则ID
     * @return 禁用结果
     */
    boolean disableBudgetControl(Long controlId);

    /**
     * 执行预算控制检查
     * 
     * @param checkParams 检查参数
     * @return 检查结果
     */
    Map<String, Object> executeBudgetControlCheck(Map<String, Object> checkParams);

    /**
     * 获取预算控制监控数据
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param monitorType 监控类型
     * @return 监控数据
     */
    Map<String, Object> getBudgetControlMonitor(Long versionId, Long organizationId, String monitorType);

    /**
     * 获取预算控制预警信息
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param alertLevel 预警级别
     * @param alertStatus 预警状态
     * @return 预警信息列表
     */
    List<Map<String, Object>> getBudgetControlAlerts(Long versionId, Long organizationId,
                                                    String alertLevel, String alertStatus);

    /**
     * 处理预算控制预警
     * 
     * @param alertId 预警ID
     * @param handleParams 处理参数
     * @return 处理结果
     */
    boolean handleBudgetControlAlert(Long alertId, Map<String, Object> handleParams);

    /**
     * 获取预算控制仪表板
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param dashboardType 仪表板类型
     * @return 仪表板数据
     */
    Map<String, Object> getBudgetControlDashboard(Long versionId, Long organizationId, String dashboardType);

    /**
     * 批量操作预算控制规则
     * 
     * @param batchData 批量操作数据
     * @return 操作结果
     */
    Map<String, Object> batchOperateBudgetControl(Map<String, Object> batchData);

    /**
     * 导入预算控制规则
     * 
     * @param importData 导入数据
     * @return 导入结果
     */
    Map<String, Object> importBudgetControl(Map<String, Object> importData);

    /**
     * 导出预算控制规则
     * 
     * @param exportParams 导出参数
     * @return 导出结果
     */
    Map<String, Object> exportBudgetControl(Map<String, Object> exportParams);

    /**
     * 获取预算控制统计
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param statisticsType 统计类型
     * @return 统计结果
     */
    Map<String, Object> getBudgetControlStatistics(Long versionId, Long organizationId, String statisticsType);

    /**
     * 获取预算控制历史
     * 
     * @param versionId 版本ID
     * @param controlType 控制类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 控制历史列表
     */
    List<Map<String, Object>> getBudgetControlHistory(Long versionId, String controlType,
                                                     String startDate, String endDate);

    /**
     * 测试预算控制规则
     * 
     * @param testParams 测试参数
     * @return 测试结果
     */
    Map<String, Object> testBudgetControl(Map<String, Object> testParams);

    /**
     * 复制预算控制规则
     * 
     * @param controlId 控制规则ID
     * @param copyParams 复制参数
     * @return 复制结果
     */
    Map<String, Object> copyBudgetControl(Long controlId, Map<String, Object> copyParams);

    /**
     * 获取预算控制模板
     * 
     * @param templateType 模板类型
     * @return 控制模板列表
     */
    List<Map<String, Object>> getBudgetControlTemplates(String templateType);

    /**
     * 应用预算控制模板
     * 
     * @param templateParams 模板参数
     * @return 应用结果
     */
    Map<String, Object> applyBudgetControlTemplate(Map<String, Object> templateParams);

    /**
     * 获取预算控制建议
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param recommendationType 建议类型
     * @return 控制建议列表
     */
    List<Map<String, Object>> getBudgetControlRecommendations(Long versionId, Long organizationId,
                                                             String recommendationType);

    /**
     * 优化预算控制策略
     * 
     * @param optimizeParams 优化参数
     * @return 优化结果
     */
    Map<String, Object> optimizeBudgetControlStrategy(Map<String, Object> optimizeParams);

    /**
     * 刷新预算控制缓存
     * 
     * @param versionId 版本ID
     * @param cacheType 缓存类型
     * @return 刷新结果
     */
    boolean refreshBudgetControlCache(Long versionId, String cacheType);

    /**
     * 实时预算控制检查
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param subjectId 科目ID
     * @param amount 金额
     * @param operationType 操作类型
     * @return 控制检查结果
     */
    Map<String, Object> realTimeBudgetControlCheck(Long versionId, Long organizationId,
                                                  Long subjectId, Double amount, String operationType);

    /**
     * 预算控制预警通知
     * 
     * @param versionId 版本ID
     * @param alertData 预警数据
     * @return 通知结果
     */
    boolean sendBudgetControlAlert(Long versionId, Map<String, Object> alertData);

    /**
     * 预算控制自动调整
     * 
     * @param versionId 版本ID
     * @param adjustParams 调整参数
     * @return 调整结果
     */
    Map<String, Object> autoBudgetControlAdjust(Long versionId, Map<String, Object> adjustParams);

    /**
     * 预算控制规则验证
     * 
     * @param controlRule 控制规则
     * @return 验证结果
     */
    Map<String, Object> validateBudgetControlRule(EpsBudgetControl controlRule);

    /**
     * 预算控制效果评估
     * 
     * @param versionId 版本ID
     * @param evaluationParams 评估参数
     * @return 评估结果
     */
    Map<String, Object> evaluateBudgetControlEffectiveness(Long versionId, Map<String, Object> evaluationParams);

    /**
     * 预算控制智能推荐
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @return 智能推荐结果
     */
    Map<String, Object> intelligentBudgetControlRecommendation(Long versionId, Long organizationId);

    /**
     * 预算控制风险评估
     * 
     * @param versionId 版本ID
     * @param riskParams 风险参数
     * @return 风险评估结果
     */
    Map<String, Object> assessBudgetControlRisk(Long versionId, Map<String, Object> riskParams);

    /**
     * 预算控制合规检查
     * 
     * @param versionId 版本ID
     * @param complianceParams 合规参数
     * @return 合规检查结果
     */
    Map<String, Object> checkBudgetControlCompliance(Long versionId, Map<String, Object> complianceParams);

    /**
     * 预算控制性能监控
     * 
     * @param versionId 版本ID
     * @param monitorParams 监控参数
     * @return 性能监控结果
     */
    Map<String, Object> monitorBudgetControlPerformance(Long versionId, Map<String, Object> monitorParams);

    /**
     * 预算控制异常处理
     * 
     * @param versionId 版本ID
     * @param exceptionData 异常数据
     * @return 异常处理结果
     */
    Map<String, Object> handleBudgetControlException(Long versionId, Map<String, Object> exceptionData);

    /**
     * 预算控制规则学习
     * 
     * @param versionId 版本ID
     * @param learningParams 学习参数
     * @return 学习结果
     */
    Map<String, Object> learnBudgetControlRules(Long versionId, Map<String, Object> learningParams);

    /**
     * 预算控制智能预警
     * 
     * @param versionId 版本ID
     * @param alertParams 预警参数
     * @return 智能预警结果
     */
    Map<String, Object> intelligentBudgetControlAlert(Long versionId, Map<String, Object> alertParams);
}
