package com.management.accountant.service.ss;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.ss.SsDigitalEmployee;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 数字员工服务接口
 *
 * @author AI Assistant
 * @since 2024-01-15
 */
public interface SsDigitalEmployeeService extends IService<SsDigitalEmployee> {

    /**
     * 分页查询数字员工列表
     */
    IPage<SsDigitalEmployee> getDigitalEmployeePage(Page<SsDigitalEmployee> page, Map<String, Object> params);

    /**
     * 根据ID查询数字员工详情
     */
    SsDigitalEmployee getDigitalEmployeeById(Long robotId, Long tenantId);

    /**
     * 根据编码查询数字员工
     */
    SsDigitalEmployee getDigitalEmployeeByCode(String robotCode, Long tenantId);

    /**
     * 创建数字员工
     */
    boolean createDigitalEmployee(SsDigitalEmployee digitalEmployee, Long tenantId);

    /**
     * 更新数字员工
     */
    boolean updateDigitalEmployee(SsDigitalEmployee digitalEmployee, Long tenantId);

    /**
     * 删除数字员工
     */
    boolean deleteDigitalEmployee(Long robotId, Long tenantId);

    /**
     * 批量删除数字员工
     */
    boolean batchDeleteDigitalEmployee(List<Long> robotIds, Long tenantId);

    /**
     * 激活数字员工
     */
    boolean activateDigitalEmployee(Long robotId, Long tenantId);

    /**
     * 停用数字员工
     */
    boolean deactivateDigitalEmployee(Long robotId, String reason, Long tenantId);

    /**
     * 批量激活数字员工
     */
    boolean batchActivateDigitalEmployee(List<Long> robotIds, Long tenantId);

    /**
     * 批量停用数字员工
     */
    boolean batchDeactivateDigitalEmployee(List<Long> robotIds, String reason, Long tenantId);

    /**
     * 启动维护模式
     */
    boolean startMaintenance(Long robotId, String reason, LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 结束维护模式
     */
    boolean endMaintenance(Long robotId, String result, Long tenantId);

    /**
     * 批量启动维护模式
     */
    boolean batchStartMaintenance(List<Long> robotIds, String reason, LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 批量结束维护模式
     */
    boolean batchEndMaintenance(List<Long> robotIds, String result, Long tenantId);

    /**
     * 部署数字员工
     */
    boolean deployDigitalEmployee(Long robotId, String deploymentEnvironment, Map<String, Object> deploymentConfig, Long tenantId);

    /**
     * 回滚数字员工
     */
    boolean rollbackDigitalEmployee(Long robotId, String version, String reason, Long tenantId);

    /**
     * 升级数字员工
     */
    boolean upgradeDigitalEmployee(Long robotId, String newVersion, Map<String, Object> upgradeConfig, Long tenantId);

    /**
     * 配置数字员工
     */
    boolean configureDigitalEmployee(Long robotId, Map<String, Object> configuration, Long tenantId);

    /**
     * 分配任务给数字员工
     */
    boolean assignTask(Long robotId, Long taskId, String taskType, Map<String, Object> taskConfig, Long tenantId);

    /**
     * 批量分配任务
     */
    boolean batchAssignTask(List<Long> robotIds, Long taskId, String taskType, Map<String, Object> taskConfig, Long tenantId);

    /**
     * 取消任务分配
     */
    boolean unassignTask(Long robotId, Long taskId, String reason, Long tenantId);

    /**
     * 执行任务
     */
    Map<String, Object> executeTask(Long robotId, Long taskId, Map<String, Object> parameters, Long tenantId);

    /**
     * 停止任务执行
     */
    boolean stopTaskExecution(Long robotId, Long taskId, String reason, Long tenantId);

    /**
     * 暂停任务执行
     */
    boolean pauseTaskExecution(Long robotId, Long taskId, String reason, Long tenantId);

    /**
     * 恢复任务执行
     */
    boolean resumeTaskExecution(Long robotId, Long taskId, Long tenantId);

    /**
     * 监控数字员工状态
     */
    Map<String, Object> monitorDigitalEmployee(Long robotId, Long tenantId);

    /**
     * 批量监控数字员工状态
     */
    List<Map<String, Object>> batchMonitorDigitalEmployee(List<Long> robotIds, Long tenantId);

    /**
     * 健康检查
     */
    Map<String, Object> performHealthCheck(Long robotId, Long tenantId);

    /**
     * 批量健康检查
     */
    List<Map<String, Object>> batchPerformHealthCheck(List<Long> robotIds, Long tenantId);

    /**
     * 性能评估
     */
    Map<String, Object> evaluatePerformance(Long robotId, LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 批量性能评估
     */
    List<Map<String, Object>> batchEvaluatePerformance(List<Long> robotIds, LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 学习训练
     */
    boolean trainDigitalEmployee(Long robotId, Map<String, Object> trainingData, String trainingType, Long tenantId);

    /**
     * 批量学习训练
     */
    boolean batchTrainDigitalEmployee(List<Long> robotIds, Map<String, Object> trainingData, String trainingType, Long tenantId);

    /**
     * 优化配置
     */
    Map<String, Object> optimizeConfiguration(Long robotId, String optimizationType, Long tenantId);

    /**
     * 负载均衡
     */
    Map<String, Object> performLoadBalancing(List<Long> robotIds, String strategy, Long tenantId);

    /**
     * 资源分配
     */
    boolean allocateResources(Long robotId, Map<String, Object> resourceConfig, Long tenantId);

    /**
     * 释放资源
     */
    boolean releaseResources(Long robotId, List<String> resourceTypes, Long tenantId);

    /**
     * 设置优先级
     */
    boolean setPriority(Long robotId, Integer priority, String reason, Long tenantId);

    /**
     * 批量设置优先级
     */
    boolean batchSetPriority(List<Long> robotIds, Integer priority, String reason, Long tenantId);

    /**
     * 设置工作时间
     */
    boolean setWorkSchedule(Long robotId, Map<String, Object> workSchedule, Long tenantId);

    /**
     * 设置通知配置
     */
    boolean setNotificationConfig(Long robotId, Map<String, Object> notificationConfig, Long tenantId);

    /**
     * 发送通知
     */
    boolean sendNotification(Long robotId, String notificationType, String message, Map<String, Object> parameters, Long tenantId);

    /**
     * 批量发送通知
     */
    boolean batchSendNotification(List<Long> robotIds, String notificationType, String message, Map<String, Object> parameters, Long tenantId);

    /**
     * 生成报告
     */
    Map<String, Object> generateReport(Long robotId, String reportType, LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 批量生成报告
     */
    List<Map<String, Object>> batchGenerateReport(List<Long> robotIds, String reportType, LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 导出数据
     */
    Map<String, Object> exportData(List<Long> robotIds, String exportFormat, Map<String, Object> exportConfig, Long tenantId);

    /**
     * 导入数据
     */
    Map<String, Object> importData(List<Map<String, Object>> dataList, Map<String, Object> importConfig, Long tenantId);

    /**
     * 备份配置
     */
    Map<String, Object> backupConfiguration(Long robotId, String backupType, Long tenantId);

    /**
     * 恢复配置
     */
    boolean restoreConfiguration(Long robotId, String backupId, Long tenantId);

    /**
     * 克隆数字员工
     */
    SsDigitalEmployee cloneDigitalEmployee(Long robotId, String newName, String newCode, Long tenantId);

    /**
     * 复制配置
     */
    boolean copyConfiguration(Long sourceRobotId, Long targetRobotId, List<String> configTypes, Long tenantId);

    /**
     * 同步配置
     */
    boolean syncConfiguration(List<Long> robotIds, Map<String, Object> configuration, Long tenantId);

    /**
     * 验证配置
     */
    Map<String, Object> validateConfiguration(Long robotId, Map<String, Object> configuration, Long tenantId);

    /**
     * 测试连接
     */
    Map<String, Object> testConnection(Long robotId, Map<String, Object> connectionConfig, Long tenantId);

    /**
     * 重启数字员工
     */
    boolean restartDigitalEmployee(Long robotId, String reason, Long tenantId);

    /**
     * 批量重启数字员工
     */
    boolean batchRestartDigitalEmployee(List<Long> robotIds, String reason, Long tenantId);

    /**
     * 清理缓存
     */
    boolean clearCache(Long robotId, List<String> cacheTypes, Long tenantId);

    /**
     * 重置统计信息
     */
    boolean resetStatistics(Long robotId, Long tenantId);

    /**
     * 批量重置统计信息
     */
    boolean batchResetStatistics(List<Long> robotIds, Long tenantId);

    /**
     * 查询统计信息
     */
    Map<String, Object> getStatistics(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 查询状态分布
     */
    List<Map<String, Object>> getStatusDistribution(Long tenantId);

    /**
     * 查询类型分布
     */
    List<Map<String, Object>> getTypeDistribution(Long tenantId);

    /**
     * 查询分类分布
     */
    List<Map<String, Object>> getCategoryDistribution(Long tenantId);

    /**
     * 查询部门分布
     */
    List<Map<String, Object>> getDepartmentDistribution(Long tenantId);

    /**
     * 查询性能趋势
     */
    List<Map<String, Object>> getPerformanceTrend(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 查询工作负载趋势
     */
    List<Map<String, Object>> getWorkloadTrend(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 查询排行榜
     */
    List<Map<String, Object>> getRanking(String rankingType, Integer limit, Long tenantId);

    /**
     * 查询告警信息
     */
    List<Map<String, Object>> getAlerts(String alertType, Long tenantId);

    /**
     * 查询待处理事项
     */
    List<Map<String, Object>> getPendingItems(Long tenantId);

    /**
     * 检查编码是否存在
     */
    boolean checkCodeExists(String robotCode, Long robotId, Long tenantId);

    /**
     * 检查名称是否存在
     */
    boolean checkNameExists(String robotName, Long robotId, Long tenantId);

    /**
     * 计算成功率
     */
    BigDecimal calculateSuccessRate(Long robotId, LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 计算平均执行时间
     */
    BigDecimal calculateAverageExecutionTime(Long robotId, LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 计算综合评分
     */
    BigDecimal calculateOverallScore(Long robotId, Long tenantId);

    /**
     * 预测性能
     */
    Map<String, Object> predictPerformance(Long robotId, Integer days, Long tenantId);

    /**
     * 推荐优化方案
     */
    List<Map<String, Object>> recommendOptimization(Long robotId, Long tenantId);

    /**
     * 智能调度
     */
    Map<String, Object> intelligentScheduling(List<Long> robotIds, List<Map<String, Object>> tasks, Long tenantId);

    /**
     * 自动扩缩容
     */
    Map<String, Object> autoScaling(String scalingType, Map<String, Object> scalingConfig, Long tenantId);
}
