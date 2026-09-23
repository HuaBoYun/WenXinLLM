package com.financial.sharing.service;

import com.financial.sharing.vo.param.BalanceRefreshParam;
import com.financial.sharing.vo.result.BalanceRefreshResult;

import java.util.List;
import java.util.Map;

/**
 * 余额刷新服务接口
 *
 * @author system
 * @since 2024-12-19
 */
public interface BalanceRefreshService {

    /**
     * 全量刷新余额
     *
     * @param bookId 账簿ID
     * @param period 会计期间
     */
    void fullRefresh(Long bookId, String period);

    /**
     * 增量刷新余额
     *
     * @param bookId 账簿ID
     * @param period 会计期间
     * @param subjectIds 科目ID列表
     */
    void incrementalRefresh(Long bookId, String period, List<Long> subjectIds);

    /**
     * 智能刷新（只刷新有变动的科目）
     *
     * @param bookId 账簿ID
     * @param period 会计期间
     */
    void smartRefresh(Long bookId, String period);

    /**
     * 执行余额刷新
     *
     * @param param 刷新参数
     * @return 刷新结果
     */
    BalanceRefreshResult refreshBalance(BalanceRefreshParam param);

    /**
     * 异步执行余额刷新
     *
     * @param param 刷新参数
     * @return 任务ID
     */
    String asyncRefreshBalance(BalanceRefreshParam param);

    /**
     * 查询刷新进度
     *
     * @param taskId 任务ID
     * @return 进度信息
     */
    Object getRefreshProgress(String taskId);

    /**
     * 取消刷新任务
     *
     * @param taskId 任务ID
     * @return 是否成功取消
     */
    boolean cancelRefresh(String taskId);

    /**
     * 获取刷新历史
     *
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param period 会计期间
     * @return 历史记录列表
     */
    List<BalanceRefreshResult> getRefreshHistory(Long bookId, Long tenantId, String period);

    /**
     * 获取需要刷新的科目列表
     *
     * @param param 刷新参数
     * @return 科目ID列表
     */
    List<Long> getSubjectsToRefresh(BalanceRefreshParam param);

    /**
     * 验证余额数据
     *
     * @param bookId 账簿ID
     * @param period 会计期间
     * @return 验证结果
     */
    Map<String, Object> validateBalanceData(Long bookId, String period);

    /**
     * 获取刷新统计信息
     *
     * @param bookId 账簿ID
     * @param period 会计期间
     * @return 统计信息
     */
    Map<String, Object> getRefreshStatistics(Long bookId, String period);

    /**
     * 重置余额数据
     *
     * @param bookId 账簿ID
     * @param period 会计期间
     * @return 是否成功
     */
    boolean resetBalanceData(Long bookId, String period);

    /**
     * 修复余额数据
     *
     * @param bookId 账簿ID
     * @param period 会计期间
     * @param subjectIds 科目ID列表
     * @return 修复结果
     */
    Map<String, Object> repairBalanceData(Long bookId, String period, List<Long> subjectIds);

    /**
     * 获取刷新建议
     *
     * @param bookId 账簿ID
     * @param period 会计期间
     * @return 刷新建议
     */
    Map<String, Object> getRefreshRecommendations(Long bookId, String period);
}