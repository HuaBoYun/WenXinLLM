package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 成本模型实体类
 * 对应表：TBL_COST_MODEL
 *
 * @author Financial Sharing System
 * @since 2024-12-29
 */
@Data
@TableName("TBL_COST_MODEL")
public class CostModelEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模型ID
     */
    @TableId(value = "MODEL_ID", type = IdType.ASSIGN_ID)
    private String modelId;

    /**
     * 模型编码
     */
    @TableField("MODEL_CODE")
    private String modelCode;

    /**
     * 模型名称
     */
    @TableField("MODEL_NAME")
    private String modelName;

    /**
     * 模型类型（LINEAR-线性模型, NONLINEAR-非线性模型, MIXED-混合模型）
     */
    @TableField("MODEL_TYPE")
    private String modelType;

    /**
     * 模型公式
     */
    @TableField("MODEL_FORMULA")
    private String modelFormula;

    /**
     * 参数配置（JSON格式）
     */
    @TableField("PARAMETERS")
    private String parameters;

    /**
     * 模型描述
     */
    @TableField("MODEL_DESCRIPTION")
    private String modelDescription;

    /**
     * 适用场景
     */
    @TableField("APPLICABLE_SCENARIO")
    private String applicableScenario;

    /**
     * 适用行业（manufacturing / service / construction / retail 等枚举值）
     */
    @TableField("INDUSTRY")
    private String industry;

    /**
     * 算法（linear_regression / activity_based / variable_costing / neural_network 等）
     */
    @TableField("ALGORITHM")
    private String algorithm;

    /**
     * 准确度评分（0-100）
     */
    @TableField("ACCURACY_SCORE")
    private Integer accuracyScore;

    /**
     * 是否启用（1-启用, 0-停用）
     */
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    /**
     * 账套ID
     */
    @TableField("BOOK_ID")
    private String bookId;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;
}

