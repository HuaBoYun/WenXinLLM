package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 差异分析实体类
 * 对应表：TBL_VARIANCE_ANALYSIS
 *
 * @author Financial Sharing System
 * @since 2024-12-29
 */
@Data
@TableName("TBL_VARIANCE_ANALYSIS")
public class VarianceAnalysisEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分析ID
     */
    @TableId(value = "ANALYSIS_ID", type = IdType.ASSIGN_ID)
    private String analysisId;

    /**
     * 分析编号
     */
    @TableField("ANALYSIS_NO")
    private String analysisNo;

    /**
     * 分析名称
     */
    @TableField("ANALYSIS_NAME")
    private String analysisName;

    /**
     * 分析期间
     */
    @TableField("ANALYSIS_PERIOD")
    private String analysisPeriod;

    /**
     * 预算ID
     */
    @TableField("BUDGET_ID")
    private String budgetId;

    /**
     * 预算金额
     */
    @TableField("BUDGET_AMOUNT")
    private BigDecimal budgetAmount;

    /**
     * 实际金额
     */
    @TableField("ACTUAL_AMOUNT")
    private BigDecimal actualAmount;

    /**
     * 差异金额
     */
    @TableField("VARIANCE_AMOUNT")
    private BigDecimal varianceAmount;

    /**
     * 差异率（%）
     */
    @TableField("VARIANCE_RATE")
    private BigDecimal varianceRate;

    /**
     * 差异类型（FAVORABLE-有利差异, UNFAVORABLE-不利差异）
     */
    @TableField("VARIANCE_TYPE")
    private String varianceType;

    /**
     * 差异原因
     */
    @TableField("VARIANCE_REASON")
    private String varianceReason;

    /**
     * 改进措施
     */
    @TableField("IMPROVEMENT_MEASURES")
    private String improvementMeasures;

    /**
     * 分析人ID
     */
    @TableField("ANALYST_ID")
    private String analystId;

    /**
     * 分析人姓名
     */
    @TableField("ANALYST_NAME")
    private String analystName;

    /**
     * 分析日期
     */
    @TableField("ANALYSIS_DATE")
    private Date analysisDate;

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

