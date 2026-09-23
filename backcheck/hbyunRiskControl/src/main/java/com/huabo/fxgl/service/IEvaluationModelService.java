package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import com.huabo.fxgl.dto.EvaluationModelQueryDTO;
import com.huabo.fxgl.entity.TblEvaluationModel;

import java.util.List;
import java.util.Map;

/**
 * 评估模型服务接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
public interface IEvaluationModelService extends IService<TblEvaluationModel> {

    /**
     * 分页查询评估模型列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    String getEvaluationModelList(EvaluationModelQueryDTO queryDTO);

    /**
     * 按预警状态分页查询存在该状态预警的评估模型（fxyj2 专用，数量为0的模型不返回）
     *
     * @param requestBody 查询参数（pageNum/pageSize/modelName/status/isEnabled/warningStatus）
     * @return 分页结果（结构与模型列表一致）
     */
    String getWarningModelList(Map<String, Object> requestBody);

    /**
     * 保存评估模型
     *
     * @param model 评估模型
     * @param currentUser 当前用户
     * @return 保存结果
     */
    String saveEvaluationModel(TblEvaluationModel model, String currentUser);

    /**
     * 根据ID获取评估模型详情
     *
     * @param modelId 模型ID
     * @return 模型详情
     */
    String getEvaluationModelDetail(String modelId);

    /**
     * 删除评估模型
     *
     * @param modelId 模型ID
     * @return 删除结果
     */
    String deleteEvaluationModel(String modelId);

    /**
     * 发布评估模型
     *
     * @param modelId 模型ID
     * @param currentUser 当前用户
     * @return 发布结果
     */
    String publishEvaluationModel(String modelId, String currentUser);

    /**
     * 启用/禁用评估模型
     *
     * @param modelId 模型ID
     * @param isEnabled 是否启用
     * @param currentUser 当前用户
     * @return 操作结果
     */
    String toggleEvaluationModel(String modelId, String isEnabled, String currentUser);

    /**
     * 复制评估模型
     *
     * @param sourceModelId 源模型ID
     * @param newModelName 新模型名称
     * @param currentUser 当前用户
     * @return 复制结果
     */
    String copyEvaluationModel(String sourceModelId, String newModelName, String currentUser);

    /**
     * 测试评估模型
     *
     * @param modelId 模型ID
     * @param testDataSource 测试数据源
     * @param testDescription 测试描述
     * @param currentUser 当前用户
     * @return 测试结果
     */
    com.hbfk.util.JsonBean testEvaluationModel(String modelId, String testDataSource, String testDescription, Object currentUser);

    /**
     * 获取评估模型统计信息
     *
     * @return 统计信息
     */
    String getEvaluationModelStatistics();

    /**
     * 根据业务场景获取启用的模型列表
     * 
     * @param businessScenario 业务场景
     * @return 模型列表
     */
    List<TblEvaluationModel> getEnabledModelsByScenario(String businessScenario);

    /**
     * 验证模型编码是否唯一
     *
     * @param modelCode 模型编码
     * @param excludeId 排除的模型ID(用于编辑时验证)
     * @return 是否唯一
     */
    boolean isModelCodeUnique(String modelCode, String excludeId);

    /**
     * 获取评估模型SQL执行结果
     *
     * @param requestBody 请求参数
     * @param currentUser 当前用户
     * @return SQL执行结果
     */
    String getEvaluationModelSqlExecutionResult(Map<String, Object> requestBody, String currentUser);

    /**
     * 启动评估模型
     *
     * @param modelId 模型ID
     * @param scheduleConfig 调度配置
     * @param currentUser 当前用户
     * @return 启动结果
     */
    String startEvaluationModel(String modelId, Map<String, Object> scheduleConfig, String currentUser);

    /**
     * 批量启动评估模型
     *
     * @param modelIds 模型ID列表
     * @param scheduleConfig 调度配置
     * @param currentUser 当前用户
     * @return 启动结果
     */
    String batchStartEvaluationModels(List<String> modelIds, Map<String, Object> scheduleConfig, String currentUser);

    /**
     * 停止评估模型
     *
     * @param modelId 模型ID
     * @param currentUser 当前用户
     * @return 停止结果
     */
    String stopEvaluationModel(String modelId, String currentUser);
}
