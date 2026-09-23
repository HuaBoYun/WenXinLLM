package com.management.accountant.entity.as;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 档案智能分类实体类
 * 
 * @author AI Assistant
 * @since 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_as_intelligent_classification")
public class AsIntelligentClassification implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID - 主键
     */
    @TableId(value = "classification_id", type = IdType.AUTO)
    private Long classificationId;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Long tenantId;

    /**
     * 分类编号
     */
    @TableField("classification_code")
    private String classificationCode;

    /**
     * 分类名称
     */
    @TableField("classification_name")
    private String classificationName;

    /**
     * 分类描述
     */
    @TableField("classification_description")
    private String classificationDescription;

    /**
     * 分类类型：AUTO-自动分类, MANUAL-手动分类, HYBRID-混合分类, RULE_BASED-规则分类, ML_BASED-机器学习分类
     */
    @TableField("classification_type")
    private String classificationType;

    /**
     * 分类状态：ACTIVE-活跃, INACTIVE-非活跃, TRAINING-训练中, TESTING-测试中, DEPLOYED-已部署
     */
    @TableField("classification_status")
    private String classificationStatus;

    /**
     * 分类算法：NAIVE_BAYES-朴素贝叶斯, SVM-支持向量机, RANDOM_FOREST-随机森林, NEURAL_NETWORK-神经网络, RULE_ENGINE-规则引擎
     */
    @TableField("classification_algorithm")
    private String classificationAlgorithm;

    /**
     * 分类准确率
     */
    @TableField("classification_accuracy")
    private BigDecimal classificationAccuracy;

    /**
     * 分类置信度阈值
     */
    @TableField("confidence_threshold")
    private BigDecimal confidenceThreshold;

    /**
     * 训练数据集大小
     */
    @TableField("training_dataset_size")
    private Integer trainingDatasetSize;

    /**
     * 测试数据集大小
     */
    @TableField("test_dataset_size")
    private Integer testDatasetSize;

    /**
     * 验证数据集大小
     */
    @TableField("validation_dataset_size")
    private Integer validationDatasetSize;

    /**
     * 特征数量
     */
    @TableField("feature_count")
    private Integer featureCount;

    /**
     * 类别数量
     */
    @TableField("category_count")
    private Integer categoryCount;

    /**
     * 模型版本
     */
    @TableField("model_version")
    private String modelVersion;

    /**
     * 模型路径
     */
    @TableField("model_path")
    private String modelPath;

    /**
     * 模型大小（MB）
     */
    @TableField("model_size")
    private BigDecimal modelSize;

    /**
     * 训练开始时间
     */
    @TableField("training_start_time")
    private LocalDateTime trainingStartTime;

    /**
     * 训练结束时间
     */
    @TableField("training_end_time")
    private LocalDateTime trainingEndTime;

    /**
     * 训练耗时（秒）
     */
    @TableField("training_duration")
    private Integer trainingDuration;

    /**
     * 最后训练时间
     */
    @TableField("last_training_time")
    private LocalDateTime lastTrainingTime;

    /**
     * 部署时间
     */
    @TableField("deployment_time")
    private LocalDateTime deploymentTime;

    /**
     * 预测次数
     */
    @TableField("prediction_count")
    private Long predictionCount;

    /**
     * 成功预测次数
     */
    @TableField("successful_predictions")
    private Long successfulPredictions;

    /**
     * 失败预测次数
     */
    @TableField("failed_predictions")
    private Long failedPredictions;

    /**
     * 平均预测时间（毫秒）
     */
    @TableField("avg_prediction_time")
    private BigDecimal avgPredictionTime;

    /**
     * 最大预测时间（毫秒）
     */
    @TableField("max_prediction_time")
    private BigDecimal maxPredictionTime;

    /**
     * 最小预测时间（毫秒）
     */
    @TableField("min_prediction_time")
    private BigDecimal minPredictionTime;

    /**
     * 分类规则配置（JSON格式）
     */
    @TableField("classification_rules")
    private String classificationRules;

    /**
     * 特征配置（JSON格式）
     */
    @TableField("feature_config")
    private String featureConfig;

    /**
     * 标签配置（JSON格式）
     */
    @TableField("label_config")
    private String labelConfig;

    /**
     * 预处理配置（JSON格式）
     */
    @TableField("preprocessing_config")
    private String preprocessingConfig;

    /**
     * 后处理配置（JSON格式）
     */
    @TableField("postprocessing_config")
    private String postprocessingConfig;

    /**
     * 评估指标（JSON格式）
     */
    @TableField("evaluation_metrics")
    private String evaluationMetrics;

    /**
     * 混淆矩阵（JSON格式）
     */
    @TableField("confusion_matrix")
    private String confusionMatrix;

    /**
     * 分类报告（JSON格式）
     */
    @TableField("classification_report")
    private String classificationReport;

    /**
     * 优化建议
     */
    @TableField("optimization_suggestions")
    private String optimizationSuggestions;

    /**
     * 性能指标（JSON格式）
     */
    @TableField("performance_metrics")
    private String performanceMetrics;

    /**
     * 资源使用情况（JSON格式）
     */
    @TableField("resource_usage")
    private String resourceUsage;

    /**
     * 错误日志
     */
    @TableField("error_log")
    private String errorLog;

    /**
     * 调试信息
     */
    @TableField("debug_info")
    private String debugInfo;

    /**
     * 备注信息
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 扩展字段1
     */
    @TableField("ext_field1")
    private String extField1;

    /**
     * 扩展字段2
     */
    @TableField("ext_field2")
    private String extField2;

    /**
     * 扩展字段3
     */
    @TableField("ext_field3")
    private String extField3;

    /**
     * 扩展字段4
     */
    @TableField("ext_field4")
    private String extField4;

    /**
     * 扩展字段5
     */
    @TableField("ext_field5")
    private String extField5;

    /**
     * 创建人
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 逻辑删除标志：0-未删除，1-已删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号（乐观锁）
     */
    @Version
    @TableField("version")
    private Integer version;
}
