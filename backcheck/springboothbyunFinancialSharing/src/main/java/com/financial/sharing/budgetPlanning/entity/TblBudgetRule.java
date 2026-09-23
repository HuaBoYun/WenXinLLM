package com.financial.sharing.budgetPlanning.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 业务规则实体类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@TableName("TBL_BUDGET_RULE")
public class TblBudgetRule {

    /**
     * 规则ID
     */
    @TableId("RULE_ID")
    private String ruleId;

    /**
     * 模型ID
     */
    @TableField("MODEL_ID")
    private String modelId;

    /**
     * 规则编码
     */
    @TableField("RULE_CODE")
    private String ruleCode;

    /**
     * 规则名称
     */
    @TableField("RULE_NAME")
    private String ruleName;

    /**
     * 规则类型：CALCULATE(计算规则)/VALIDATE(校验规则)/ALLOCATE(分摊规则)
     */
    @TableField("RULE_TYPE")
    private String ruleType;

    /**
     * 规则表达式(JSON格式)
     * 计算规则: {sourceFields: [], targetField: "", formula: ""}
     * 校验规则: {validateField: "", condition: "", errorMessage: ""}
     * 分摊规则: {baseField: "", dimensions: [], method: ""}
     */
    @TableField("RULE_EXPRESSION")
    private String ruleExpression;

    /**
     * 优先级(数字越小优先级越高)
     */
    @TableField("PRIORITY")
    private Integer priority;

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

    /**
     * 模型名称(非数据库字段)
     */
    @TableField(exist = false)
    private String modelName;
}

