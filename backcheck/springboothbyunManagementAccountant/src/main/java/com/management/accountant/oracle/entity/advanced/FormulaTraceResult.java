package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 公式追踪结果实体类
 * 
 * @author AI Agent
 * @date 2026-02-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FORMULA_TRACE_RESULT")
public class FormulaTraceResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 结果ID (主键)
     */
    @TableId(value = "RESULT_ID", type = IdType.ASSIGN_UUID)
    private String resultId;

    /**
     * 任务ID (外键)
     */
    @TableField("TASK_ID")
    private String taskId;

    /**
     * 公式ID
     */
    @TableField("FORMULA_ID")
    private String formulaId;

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
     * 依赖的公式ID列表 (JSON格式)
     */
    @TableField("DEPENDENCY_FORMULA_IDS")
    private String dependencyFormulaIds;

    /**
     * 依赖的公式名称列表 (JSON格式)
     */
    @TableField("DEPENDENCY_FORMULA_NAMES")
    private String dependencyFormulaNames;

    /**
     * 当前追踪深度
     */
    @TableField("TRACE_DEPTH")
    private Integer traceDepth;

    /**
     * 追踪路径 (如: A->B->C)
     */
    @TableField("TRACE_PATH")
    private String tracePath;

    /**
     * 依赖树结构 (JSON格式)
     */
    @TableField("DEPENDENCY_TREE")
    private String dependencyTree;

    /**
     * 影响分析数据 (JSON格式)
     */
    @TableField("IMPACT_ANALYSIS")
    private String impactAnalysis;

    /**
     * 公司ID
     */
    @TableField("COMPANY_ID")
    private String companyId;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;
}

