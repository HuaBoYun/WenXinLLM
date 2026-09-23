package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 风险评估实体 - 对齐 TBL_RISK_ASSESSMENT 真实表结构
 */
@Data
@TableName("TBL_RISK_ASSESSMENT")
public class RiskAssessment implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    @TableField("RISK_ID")
    private String riskId;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    /** 风险类别 */
    @TableField("RISK_CATEGORY")
    private String riskCategory;

    /** 风险事项 */
    @TableField("RISK_ITEM")
    private String riskItem;

    /** 风险等级: HIGH/MEDIUM/LOW */
    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("RISK_DESCRIPTION")
    private String riskDescription;

    /** 影响程度 */
    @TableField("IMPACT_DEGREE")
    private String impactDegree;

    /** 发生概率 */
    @TableField("OCCURRENCE_PROBABILITY")
    private String occurrenceProbability;

    /** 应对措施 */
    @TableField("COUNTERMEASURES")
    private String countermeasures;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField("ASSESSMENT_DATE")
    private Date assessmentDate;

    /** 状态 */
    @TableField("STATUS")
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("CREATE_TIME")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /** 风险评分 */
    @TableField("RISK_SCORE")
    private BigDecimal riskScore;

    /** 评估人 */
    @TableField("ASSESSOR")
    private String assessor;

    @TableField("F_DATASOURCETYPE")
    private Integer fDatasourcetype;

    @TableField("F_IMPORTBATCHES")
    private String fImportbatches;
}
