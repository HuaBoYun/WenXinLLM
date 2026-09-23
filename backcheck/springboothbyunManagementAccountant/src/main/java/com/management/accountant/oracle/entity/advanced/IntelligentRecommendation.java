package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 智能推荐实体类
 * 
 * @description 智能推荐主表实体，支持基于AI算法的预算编制建议
 * @author AI Assistant
 * @date 2026-02-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_INTELLIGENT_RECOMMENDATION")
public class IntelligentRecommendation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 推荐ID（主键）
     */
    @TableId(value = "RECOMMENDATION_ID", type = IdType.ASSIGN_UUID)
    private String recommendationId;

    /**
     * 推荐名称
     */
    @TableField("RECOMMENDATION_NAME")
    private String recommendationName;

    /**
     * 推荐类型：budget_optimization(预算优化)、cost_reduction(成本削减)、
     * revenue_growth(收入增长)、risk_warning(风险预警)
     */
    @TableField("RECOMMENDATION_TYPE")
    private String recommendationType;

    /**
     * 关联预算ID
     */
    @TableField("BUDGET_ID")
    private String budgetId;

    /**
     * 预算名称
     */
    @TableField("BUDGET_NAME")
    private String budgetName;

    /**
     * 状态：pending(待处理)、accepted(已接受)、rejected(已拒绝)
     */
    @TableField("STATUS")
    private String status;

    /**
     * 置信度分数(0-100)
     */
    @TableField("CONFIDENCE_SCORE")
    private BigDecimal confidenceScore;

    /**
     * 推荐内容JSON格式
     */
    @TableField("RECOMMENDATION_CONTENT")
    private String recommendationContent;

    /**
     * 分析数据JSON格式
     */
    @TableField("ANALYSIS_DATA")
    private String analysisData;

    /**
     * 预期影响描述
     */
    @TableField("EXPECTED_IMPACT")
    private String expectedImpact;

    /**
     * 公司ID
     */
    @TableField("COMPANY_ID")
    private String companyId;

    /**
     * 公司名称
     */
    @TableField("COMPANY_NAME")
    private String companyName;

    /**
     * 创建人ID
     */
    @TableField("CREATED_BY")
    private String createdBy;

    /**
     * 创建人姓名
     */
    @TableField("CREATOR_NAME")
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 接受人ID
     */
    @TableField("ACCEPTED_BY")
    private String acceptedBy;

    /**
     * 接受人姓名
     */
    @TableField("ACCEPTOR_NAME")
    private String acceptorName;

    /**
     * 接受时间
     */
    @TableField("ACCEPT_TIME")
    private Date acceptTime;

    /**
     * 拒绝人ID
     */
    @TableField("REJECTED_BY")
    private String rejectedBy;

    /**
     * 拒绝人姓名
     */
    @TableField("REJECTOR_NAME")
    private String rejectorName;

    /**
     * 拒绝时间
     */
    @TableField("REJECT_TIME")
    private Date rejectTime;

    /**
     * 拒绝原因
     */
    @TableField("REJECT_REASON")
    private String rejectReason;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;
}

