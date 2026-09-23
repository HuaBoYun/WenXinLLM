package com.financial.sharing.budgetPlanning.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 预算模型实体类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@TableName("TBL_BUDGET_MODEL")
public class TblBudgetModel {

    /**
     * 模型ID
     */
    @TableId("MODEL_ID")
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
     * 预算年度
     */
    @TableField("BUDGET_YEAR")
    private String budgetYear;

    /**
     * 预算类型：ANNUAL(年度预算)/ROLLING(滚动预测)/SPECIAL(专项预算)
     */
    @TableField("BUDGET_TYPE")
    private String budgetType;

    /**
     * 预算周期：YEAR(年)/HALF_YEAR(半年)/QUARTER(季)/MONTH(月)
     */
    @TableField("BUDGET_CYCLE")
    private String budgetCycle;

    /**
     * 维度配置(JSON格式)
     * 包含:科目维度、主体维度、期间维度、版本维度等
     */
    @TableField("DIMENSION_CONFIG")
    private String dimensionConfig;

    /**
     * 状态：DRAFT(草稿)/ACTIVE(启用)/INACTIVE(停用)
     */
    @TableField("STATUS")
    private String status;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 排序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 租户ID
     */
    @TableField("ORG_ID")
    private String orgId;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 修改人
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 修改时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;
}

