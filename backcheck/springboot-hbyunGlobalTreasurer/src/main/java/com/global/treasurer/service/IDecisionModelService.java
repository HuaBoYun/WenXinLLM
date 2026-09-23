package com.global.treasurer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.DecisionModel;

import java.util.Map;

/**
 * 决策模型Service接口
 *
 * @author 华博云开发团队
 * @since 2025-01-12
 */
public interface IDecisionModelService extends IService<DecisionModel> {

    /**
     * 分页查询决策模型
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<DecisionModel> selectPage(IPage<DecisionModel> page, Map<String, Object> params);

    /**
     * 训练决策模型
     * @param modelId 模型ID
     * @param trainingData 训练数据
     * @return 训练结果
     */
    boolean trainModel(Long modelId, String trainingData);

    /**
     * 激活决策模型
     * @param modelId 模型ID
     * @return 是否成功
     */
    boolean activateModel(Long modelId);

    /**
     * 停用决策模型
     * @param modelId 模型ID
     * @return 是否成功
     */
    boolean deactivateModel(Long modelId);

    /**
     * 验证决策模型
     * @param modelId 模型ID
     * @param validationData 验证数据(JSON格式)
     * @return 验证结果,包含验证状态、准确率等信息
     */
    Map<String, Object> validateModel(Long modelId, String validationData);

    /**
     * 测试决策模型
     * @param modelId 模型ID
     * @param testData 测试数据
     * @return 测试结果,包含预测值、准确率、执行时间等
     */
    Map<String, Object> testModel(Long modelId, Map<String, Object> testData);
}
