package com.global.treasurer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.PredictiveAnalysis;

import java.util.Map;

/**
 * 预测分析Service接口
 *
 * @author 华博云开发团队
 * @since 2025-01-12
 */
public interface IPredictiveAnalysisService extends IService<PredictiveAnalysis> {

    /**
     * 分页查询预测分析
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<PredictiveAnalysis> selectPage(IPage<PredictiveAnalysis> page, Map<String, Object> params);

    /**
     * 执行预测分析
     * @param analysisId 分析ID
     * @param executeUser 执行人ID
     * @return 是否成功
     */
    boolean executeAnalysis(Long analysisId, Long executeUser);

    /**
     * 取消预测分析
     * @param analysisId 分析ID
     * @param cancelUser 取消人ID
     * @return 是否成功
     */
    boolean cancelAnalysis(Long analysisId, Long cancelUser);

    /**
     * 重试失败的预测分析
     * @param analysisId 分析ID
     * @param retryUser 重试人ID
     * @return 是否成功
     */
    boolean retryAnalysis(Long analysisId, Long retryUser);

    /**
     * 更新实际结果
     * @param analysisId 分析ID
     * @param actualResult 实际结果
     * @param updateUser 更新人ID
     * @return 是否成功
     */
    boolean updateActualResult(Long analysisId, String actualResult, Long updateUser);

    /**
     * 计算预测准确率
     * @param analysisId 分析ID
     * @param calculateUser 计算人ID
     * @return 是否成功
     */
    boolean calculateAccuracy(Long analysisId, Long calculateUser);
}
