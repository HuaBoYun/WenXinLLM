package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算优化结果实体
 */
@Data
@TableName("TBL_BUDGET_OPTIMIZATION_RESULT")
public class BudgetOptimizationResult implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    @TableField("RESULT_ID")
    private String resultId;

    /** 关联优化任务ID */
    @TableField("OPTIMIZATION_ID")
    private String optimizationId;

    /** 变量名称 */
    @TableField("VARIABLE_NAME")
    private String variableName;

    /** 原始值 */
    @TableField("ORIGINAL_VALUE")
    private BigDecimal originalValue;

    /** 优化后值 */
    @TableField("OPTIMIZED_VALUE")
    private BigDecimal optimizedValue;

    /** 改善幅度(%) */
    @TableField("IMPROVEMENT")
    private BigDecimal improvement;

    /** 影响程度: HIGH/MEDIUM/LOW */
    @TableField("IMPACT")
    private String impact;

    /** 建议说明 */
    @TableField("RECOMMENDATION")
    private String recommendation;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("DEL_FLAG")
    private Integer delFlag;
}
