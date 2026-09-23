package com.financial.sharing.service;

import com.financial.sharing.vo.param.BatchGenerationParam;
import com.financial.sharing.vo.result.VoucherGenerationVO;
import com.financial.sharing.vo.result.GenerationPreviewVO;
import com.financial.sharing.vo.result.VoucherGenerationProgress;

import java.util.List;
import java.util.Map;

/**
 * 凭证生成服务接口
 *
 * @author system
 * @since 2024-12-08
 */
public interface VoucherGenerationService {

    /**
     * 批量生成凭证
     *
     * @param param 生成参数
     * @return 生成结果
     */
    VoucherGenerationVO batchGenerateVouchers(BatchGenerationParam param);

    /**
     * 预览凭证生成结果
     *
     * @param transactionIds 事项ID列表
     * @return 预览结果
     */
    List<GenerationPreviewVO> previewVoucherGeneration(List<Long> transactionIds);

    /**
     * 查询生成进度
     *
     * @param batchId 批次ID
     * @return 生成进度
     */
    VoucherGenerationProgress getGenerationProgress(String batchId);

    /**
     * 停止生成任务
     *
     * @param taskId 任务ID
     * @return 是否成功
     */
    boolean stopGenerationTask(String taskId);

    /**
     * 删除生成任务
     *
     * @param taskId 任务ID
     * @return 是否成功
     */
    boolean deleteGenerationTask(String taskId);

    /**
     * 查询任务详情
     *
     * @param taskId 任务ID
     * @return 任务详情
     */
    Map<String, Object> getTaskDetail(String taskId);

    /**
     * 获取统计数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> getVoucherStatistics(Map<String, Object> param);

    /**
     * 获取生成趋势
     *
     * @param param 查询参数
     * @return 趋势数据
     */
    List<Map<String, Object>> getVoucherGenerationTrend(Map<String, Object> param);

    /**
     * 获取待执行的任务
     *
     * @return 任务列表
     */
    List<Map<String, Object>> getScheduledTasks();

    /**
     * 更新任务状态
     *
     * @param taskId 任务ID
     * @param status 状态
     */
    void updateTaskStatus(String taskId, String status);

    /**
     * 清理历史任务
     *
     * @return 删除数量
     */
    int cleanupHistoryTasks();
}