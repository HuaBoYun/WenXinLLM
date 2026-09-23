package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("TBL_BUDGET_OPTIMIZATION")
public class BudgetOptimization implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    @TableField("OPTIMIZATION_ID")
    private String optimizationId;

    @TableField("TASK_NAME")
    private String taskName;

    @TableField("OPTIMIZATION_TYPE")
    private String optimizationType;

    /** 状态: PENDING/RUNNING/COMPLETED/FAILED/STOPPED */
    @TableField("OPTIMIZATION_STATUS")
    private String optimizationStatus;

    /** 算法: GENETIC_ALGORITHM/PARTICLE_SWARM/SIMULATED_ANNEALING/LINEAR_PROGRAMMING/GRADIENT_DESCENT */
    @TableField("ALGORITHM")
    private String algorithm;

    /** 优化目标: MINIMIZE_COST/MAXIMIZE_EFFICIENCY/MAXIMIZE_ROI/MINIMIZE_RISK */
    @TableField("OPTIMIZATION_GOAL")
    private String optimizationGoal;

    /** 改善幅度(百分比，如 15.5 表示 15.5%) */
    @TableField("IMPROVEMENT")
    private java.math.BigDecimal improvement;

    /** 执行进度(0-100) */
    @TableField("PROGRESS")
    private Integer progress;

    @TableField("TARGET_BUDGET")
    private String targetBudget;

    @TableField("OBJECTIVE_FUNCTION")
    private String objectiveFunction;

    @TableField("CONSTRAINTS")
    private String constraints;

    @TableField("OPTIMIZATION_RESULT")
    private String optimizationResult;

    @TableField("EXECUTION_TIME")
    private Long executionTime;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("DEL_FLAG")
    private Integer delFlag;
}
