package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 预测模型信息实体
 *
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("TBL_BUDGET_FORECAST_MODEL")
public class BudgetForecastModel {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /** 模型编码 */
    @TableField("MODEL_CODE")
    private String modelCode;

    /** 模型名称 */
    @TableField("MODEL_NAME")
    private String modelName;

    /** 模型类型 */
    @TableField("MODEL_TYPE")
    private String modelType;

    /** 模型描述 */
    @TableField("DESCRIPTION")
    private String description;

    /** 训练数据描述 */
    @TableField("TRAINING_DATA_DESC")
    private String trainingDataDesc;

    /** 准确率(%) */
    @TableField("ACCURACY")
    private BigDecimal accuracy;

    /** R²决定系数 */
    @TableField("R_SQUARED")
    private BigDecimal rSquared;

    /** 均方根误差 */
    @TableField("RMSE")
    private BigDecimal rmse;

    /** 平均绝对误差 */
    @TableField("MAE")
    private BigDecimal mae;

    /** 平均绝对百分比误差 */
    @TableField("MAPE")
    private BigDecimal mape;

    /** 是否激活(0-否 1-是) */
    @TableField("IS_ACTIVE")
    private Integer isActive;

    /** 最后训练时间 */
    @TableField("LAST_TRAIN_TIME")
    private Date lastTrainTime;

    /** 创建人 */
    @TableField("CREATE_BY")
    private String createBy;

    /** 创建时间 */
    @TableField("CREATE_TIME")
    private Date createTime;

    /** 更新人 */
    @TableField("UPDATE_BY")
    private String updateBy;

    /** 更新时间 */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /** 删除标志（0-正常 1-已删除） */
    @TableLogic
    @TableField("DEL_FLAG")
    private Integer delFlag;
}
