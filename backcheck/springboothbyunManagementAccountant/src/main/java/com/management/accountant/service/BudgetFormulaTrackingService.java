package com.management.accountant.service;

import com.management.accountant.util.PageResult;
import java.util.Map;

/**
 * 预算公式追踪Service接口
 *
 * @description 预算公式追踪业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetFormulaTrackingService {

    /**
     * 创建追踪任务
     *
     * @param params 任务参数
     * @return 创建结果
     */
    Map<String, Object> create(Map<String, Object> params);

    /**
     * 更新追踪任务
     *
     * @param taskId 任务ID
     * @param params 更新参数
     */
    void update(String taskId, Map<String, Object> params);

    /**
     * 删除追踪任务
     *
     * @param taskId 任务ID
     */
    void delete(String taskId);

    /**
     * 分页查询追踪任务
     *
     * @param current 当前页
     * @param size 页大小
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<Map<String, Object>> page(int current, int size, Map<String, Object> params);

    /**
     * 执行追踪任务
     *
     * @param taskId 任务ID
     * @return 执行结果
     */
    Map<String, Object> execute(String taskId);

    /**
     * 停止追踪任务
     *
     * @param taskId 任务ID
     */
    void stop(String taskId);

    /**
     * 复制追踪任务
     *
     * @param taskId 任务ID
     * @return 新任务
     */
    Map<String, Object> copy(String taskId);

    /**
     * 获取追踪任务详情
     *
     * @param taskId 任务ID
     * @return 任务详情
     */
    Map<String, Object> getDetail(String taskId);

    /**
     * 获取追踪任务日志
     *
     * @param taskId 任务ID
     * @return 日志列表
     */
    Map<String, Object> getLogs(String taskId);

    /**
     * 追踪公式依赖
     *
     * @param formulaId 公式ID
     * @return 依赖信息
     */
    Map<String, Object> trackDependencies(String formulaId);

    /**
     * 分析影响范围
     *
     * @param params 分析参数
     * @return 影响分析结果
     */
    Map<String, Object> analyzeImpact(Map<String, Object> params);

    /**
     * 获取公式链路
     *
     * @param formulaId 公式ID
     * @return 公式链路信息
     */
    Map<String, Object> getFormulaChain(String formulaId);

    /**
     * 验证公式循环依赖
     *
     * @param params 验证参数
     * @return 验证结果
     */
    Map<String, Object> validateCircularDependency(Map<String, Object> params);

    /**
     * 生成依赖关系图
     *
     * @param params 图参数
     * @return 关系图数据
     */
    Map<String, Object> generateDependencyGraph(Map<String, Object> params);

    /**
     * 获取公式追踪统计数据
     *
     * @param params 查询参数
     * @return 统计数据
     */
    Map<String, Object> getDependenciesStats(Map<String, Object> params);

    /**
     * 获取可用公式列表
     *
     * @param params 查询参数
     * @return 公式列表
     */
    Map<String, Object> getAvailableFormulas(Map<String, Object> params);

    /**
     * 获取任务公式列表
     *
     * @param taskId 任务ID
     * @return 公式列表
     */
    Map<String, Object> getTaskFormulas(String taskId);
}

