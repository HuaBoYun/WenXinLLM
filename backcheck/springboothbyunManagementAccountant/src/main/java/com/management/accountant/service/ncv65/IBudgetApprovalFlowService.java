package com.management.accountant.service.ncv65;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.ncv65.BudgetApprovalFlow;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算审批流程服务接口
 * 
 * @description 预算审批流程业务逻辑接口，支持审批流程的完整生命周期管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
public interface IBudgetApprovalFlowService extends IService<BudgetApprovalFlow> {

    // ==================== 基础CRUD操作 ====================

    /**
     * 创建审批流程
     * @param flow 审批流程信息
     * @return 是否创建成功
     */
    boolean createApprovalFlow(BudgetApprovalFlow flow);

    /**
     * 更新审批流程
     * @param flow 审批流程信息
     * @return 是否更新成功
     */
    boolean updateApprovalFlow(BudgetApprovalFlow flow);

    /**
     * 删除审批流程
     * @param id 审批流程ID
     * @return 是否删除成功
     */
    boolean deleteApprovalFlow(String id);

    /**
     * 批量删除审批流程
     * @param ids 审批流程ID列表
     * @return 是否删除成功
     */
    boolean batchDeleteApprovalFlows(List<String> ids);

    /**
     * 根据ID查询审批流程
     * @param id 审批流程ID
     * @return 审批流程信息
     */
    BudgetApprovalFlow getApprovalFlowById(String id);

    /**
     * 根据编码查询审批流程
     * @param flowCode 流程编码
     * @return 审批流程信息
     */
    BudgetApprovalFlow getApprovalFlowByCode(String flowCode);

    // ==================== 查询操作 ====================

    /**
     * 分页查询审批流程
     * @param current 当前页
     * @param size 页大小
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetApprovalFlow> getApprovalFlowPage(Integer current, Integer size, Map<String, Object> params);

    /**
     * 根据流程类型查询审批流程
     * @param flowType 流程类型
     * @return 审批流程列表
     */
    List<BudgetApprovalFlow> getApprovalFlowsByType(String flowType);

    /**
     * 根据适用组织查询审批流程
     * @param applicableOrgId 适用组织ID
     * @return 审批流程列表
     */
    List<BudgetApprovalFlow> getApprovalFlowsByOrg(String applicableOrgId);

    /**
     * 查询已发布的审批流程
     * @return 已发布的审批流程列表
     */
    List<BudgetApprovalFlow> getPublishedApprovalFlows();

    /**
     * 查询可用的审批流程
     * @param flowType 流程类型
     * @param orgId 组织ID
     * @return 可用的审批流程列表
     */
    List<BudgetApprovalFlow> getAvailableApprovalFlows(String flowType, String orgId);

    /**
     * 查询默认审批流程
     * @param flowType 流程类型
     * @param orgId 组织ID
     * @return 默认审批流程
     */
    BudgetApprovalFlow getDefaultApprovalFlow(String flowType, String orgId);

    /**
     * 查询流程版本列表
     * @param flowCode 流程编码
     * @return 流程版本列表
     */
    List<BudgetApprovalFlow> getFlowVersions(String flowCode);

    /**
     * 查询最新流程版本
     * @param flowCode 流程编码
     * @return 最新流程版本
     */
    BudgetApprovalFlow getLatestFlowVersion(String flowCode);

    // ==================== 业务操作 ====================

    /**
     * 发布审批流程
     * @param flowId 流程ID
     * @return 是否发布成功
     */
    boolean publishApprovalFlow(String flowId);

    /**
     * 取消发布审批流程
     * @param flowId 流程ID
     * @return 是否取消发布成功
     */
    boolean unpublishApprovalFlow(String flowId);

    /**
     * 归档审批流程
     * @param flowId 流程ID
     * @return 是否归档成功
     */
    boolean archiveApprovalFlow(String flowId);

    /**
     * 启用审批流程
     * @param flowId 流程ID
     * @return 是否启用成功
     */
    boolean enableApprovalFlow(String flowId);

    /**
     * 停用审批流程
     * @param flowId 流程ID
     * @return 是否停用成功
     */
    boolean disableApprovalFlow(String flowId);

    /**
     * 设置默认审批流程
     * @param flowId 流程ID
     * @param flowType 流程类型
     * @param orgId 组织ID
     * @return 是否设置成功
     */
    boolean setDefaultApprovalFlow(String flowId, String flowType, String orgId);

    /**
     * 复制审批流程
     * @param sourceFlowId 源流程ID
     * @param newFlowCode 新流程编码
     * @param newFlowName 新流程名称
     * @return 新流程ID
     */
    String copyApprovalFlow(String sourceFlowId, String newFlowCode, String newFlowName);

    /**
     * 创建流程版本
     * @param flowId 流程ID
     * @param versionName 版本名称
     * @return 新版本ID
     */
    String createFlowVersion(String flowId, String versionName);

    /**
     * 批量发布审批流程
     * @param flowIds 流程ID列表
     * @return 发布成功的流程数量
     */
    int batchPublishApprovalFlows(List<String> flowIds);

    /**
     * 批量启用审批流程
     * @param flowIds 流程ID列表
     * @return 启用成功的流程数量
     */
    int batchEnableApprovalFlows(List<String> flowIds);

    /**
     * 批量停用审批流程
     * @param flowIds 流程ID列表
     * @return 停用成功的流程数量
     */
    int batchDisableApprovalFlows(List<String> flowIds);

    // ==================== 流程配置管理 ====================

    /**
     * 更新流程定义
     * @param flowId 流程ID
     * @param flowDefinition 流程定义
     * @return 是否更新成功
     */
    boolean updateFlowDefinition(String flowId, String flowDefinition);

    /**
     * 更新节点配置
     * @param flowId 流程ID
     * @param nodeConfig 节点配置
     * @return 是否更新成功
     */
    boolean updateNodeConfig(String flowId, String nodeConfig);

    /**
     * 更新审批规则
     * @param flowId 流程ID
     * @param approvalRules 审批规则
     * @return 是否更新成功
     */
    boolean updateApprovalRules(String flowId, String approvalRules);

    /**
     * 更新超时策略
     * @param flowId 流程ID
     * @param timeoutStrategy 超时策略
     * @return 是否更新成功
     */
    boolean updateTimeoutStrategy(String flowId, String timeoutStrategy);

    /**
     * 验证流程配置
     * @param flowId 流程ID
     * @return 验证结果
     */
    Map<String, Object> validateFlowConfig(String flowId);

    /**
     * 测试流程执行
     * @param flowId 流程ID
     * @param testData 测试数据
     * @return 测试结果
     */
    Map<String, Object> testFlowExecution(String flowId, Map<String, Object> testData);

    // ==================== 统计分析 ====================

    /**
     * 获取审批流程统计信息
     * @return 统计信息
     */
    Map<String, Object> getApprovalFlowStatistics();

    /**
     * 按流程类型统计审批流程数量
     * @return 统计结果
     */
    List<Map<String, Object>> getApprovalFlowCountByType();

    /**
     * 按状态统计审批流程数量
     * @return 统计结果
     */
    List<Map<String, Object>> getApprovalFlowCountByStatus();

    /**
     * 获取流程使用统计
     * @return 使用统计
     */
    List<Map<String, Object>> getFlowUsageStatistics();

    /**
     * 获取流程性能统计
     * @param flowId 流程ID
     * @return 性能统计
     */
    Map<String, Object> getFlowPerformanceStatistics(String flowId);

    /**
     * 获取用户审批统计
     * @param userId 用户ID
     * @return 用户审批统计
     */
    Map<String, Object> getUserApprovalStatistics(String userId);

    // ==================== 流程执行支持 ====================

    /**
     * 启动流程实例
     * @param flowId 流程ID
     * @param businessKey 业务键
     * @param variables 流程变量
     * @return 流程实例ID
     */
    String startFlowInstance(String flowId, String businessKey, Map<String, Object> variables);

    /**
     * 获取下一个审批节点
     * @param flowId 流程ID
     * @param currentNodeId 当前节点ID
     * @param variables 流程变量
     * @return 下一个审批节点信息
     */
    Map<String, Object> getNextApprovalNode(String flowId, String currentNodeId, Map<String, Object> variables);

    /**
     * 获取审批人列表
     * @param flowId 流程ID
     * @param nodeId 节点ID
     * @param variables 流程变量
     * @return 审批人列表
     */
    List<Map<String, Object>> getApprovers(String flowId, String nodeId, Map<String, Object> variables);

    /**
     * 检查审批权限
     * @param flowId 流程ID
     * @param nodeId 节点ID
     * @param userId 用户ID
     * @return 是否有审批权限
     */
    boolean checkApprovalPermission(String flowId, String nodeId, String userId);

    // ==================== 数据导入导出 ====================

    /**
     * 导出审批流程配置
     * @param flowIds 流程ID列表
     * @return 导出文件路径
     */
    String exportApprovalFlows(List<String> flowIds);

    /**
     * 导入审批流程配置
     * @param filePath 文件路径
     * @return 导入结果
     */
    Map<String, Object> importApprovalFlows(String filePath);

    // ==================== 数据清理 ====================

    /**
     * 清理草稿流程
     * @param days 草稿天数
     * @return 清理数量
     */
    int cleanupDraftFlows(Integer days);

    /**
     * 清理已归档流程
     * @param days 归档天数
     * @return 清理数量
     */
    int cleanupArchivedFlows(Integer days);

    // ==================== 验证方法 ====================

    /**
     * 检查流程编码是否存在
     * @param flowCode 流程编码
     * @param excludeId 排除的流程ID
     * @return 是否存在
     */
    boolean checkFlowCodeExists(String flowCode, String excludeId);

    /**
     * 检查流程名称是否存在
     * @param flowName 流程名称
     * @param excludeId 排除的流程ID
     * @return 是否存在
     */
    boolean checkFlowNameExists(String flowName, String excludeId);

    /**
     * 检查流程是否被使用
     * @param flowId 流程ID
     * @return 是否被使用
     */
    boolean checkFlowInUse(String flowId);

    /**
     * 检查用户是否有流程操作权限
     * @param flowId 流程ID
     * @param userId 用户ID
     * @param operation 操作类型
     * @return 是否有权限
     */
    boolean hasFlowPermission(String flowId, String userId, String operation);
}
