package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算公式实体类
 *
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_FORMULA")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BudgetFormula implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公式ID (主键)
     */
    @TableId(value = "FORMULA_ID", type = IdType.ASSIGN_UUID)
    private String formulaId;

    /**
     * 公式编码
     */
    @TableField("FORMULA_CODE")
    private String formulaCode;

    /**
     * 公式名称
     */
    @TableField("FORMULA_NAME")
    private String formulaName;

    /**
     * 公式表达式
     */
    @TableField("FORMULA_EXPRESSION")
    private String formulaExpression;

    /**
     * 公式类型
     * CALCULATION: 计算公式
     * VALIDATION: 验证公式
     * ALLOCATION: 分配公式
     */
    @TableField("FORMULA_TYPE")
    private String formulaType;

    /**
     * 输入参数 (JSON格式)
     */
    @TableField("INPUT_PARAMETERS")
    private String inputParameters;

    /**
     * 输出参数 (JSON格式)
     */
    @TableField("OUTPUT_PARAMETERS")
    private String outputParameters;

    /**
     * 依赖公式ID列表 (JSON数组)
     */
    @TableField("DEPENDENCY_FORMULAS")
    private String dependencyFormulas;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
     private Integer isEnabled;

    /**
     * 公式描述
     */
    @TableField("FORMULA_DESCRIPTION")
    private String formulaDescription;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 删除标志
     */
    @TableField("DEL_FLAG")
    private Integer delFlag;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    // ========== 虚拟字段（不映射数据库列，用于前端展示） ==========

    /**
     * 公式状态 (DRAFT/PUBLISHED/ARCHIVED)，根据 isEnabled 映射
     */
    @TableField(exist = false)
    private String formulaStatus;

    /**
     * 复杂度 (1-5)，根据公式表达式长度计算
     */
    @TableField(exist = false)
    private Integer complexity;

    /**
     * 验证状态 (VALID/INVALID/PENDING)
     */
    @TableField(exist = false)
    private String validationStatus;
}

