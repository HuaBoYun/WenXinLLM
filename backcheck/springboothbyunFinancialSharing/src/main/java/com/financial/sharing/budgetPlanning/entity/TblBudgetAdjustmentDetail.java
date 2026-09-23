package com.financial.sharing.budgetPlanning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算调整明细表实体
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Data
@TableName("TBL_BUDGET_ADJUSTMENT_DETAIL")
public class TblBudgetAdjustmentDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 明细ID
     */
    @TableId(value = "DETAIL_ID", type = IdType.ASSIGN_ID)
    private String detailId;

    /**
     * 调整单ID
     */
    @TableField("ADJUSTMENT_ID")
    private String adjustmentId;

    /**
     * 预算数据ID
     */
    @TableField("DATA_ID")
    private String dataId;

    /**
     * 科目编码
     */
    @TableField("SUBJECT_CODE")
    private String subjectCode;

    /**
     * 科目名称
     */
    @TableField("SUBJECT_NAME")
    private String subjectName;

    /**
     * 组织编码
     */
    @TableField("ORGANIZATION_CODE")
    private String organizationCode;

    /**
     * 组织名称
     */
    @TableField("ORGANIZATION_NAME")
    private String organizationName;

    /**
     * 维度1编码
     */
    @TableField("DIMENSION1_CODE")
    private String dimension1Code;

    /**
     * 维度1名称
     */
    @TableField("DIMENSION1_NAME")
    private String dimension1Name;

    /**
     * 维度2编码
     */
    @TableField("DIMENSION2_CODE")
    private String dimension2Code;

    /**
     * 维度2名称
     */
    @TableField("DIMENSION2_NAME")
    private String dimension2Name;

    /**
     * 原值
     */
    @TableField("ORIGINAL_VALUE")
    private BigDecimal originalValue;

    /**
     * 调整值
     */
    @TableField("ADJUSTMENT_VALUE")
    private BigDecimal adjustmentValue;

    /**
     * 调整后值
     */
    @TableField("ADJUSTED_VALUE")
    private BigDecimal adjustedValue;

    /**
     * 调整原因
     */
    @TableField("ADJUSTMENT_REASON")
    private String adjustmentReason;

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

