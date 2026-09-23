package com.huabo.contract.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 风险评估查询参数
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RiskAssessmentQueryParam extends BaseQueryParam {

    private static final long serialVersionUID = 1L;

    /**
     * 评估编号
     */
    private String assessmentNo;

    /**
     * 评估名称
     */
    private String assessmentName;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 相对方ID
     */
    private Long counterpartId;

    /**
     * 相对方名称
     */
    private String counterpartName;

    /**
     * 评估类型(1:承接前,2:执行中,3:结项后)
     */
    private Integer assessmentType;

    /**
     * 评估状态(1:待评估,2:评估中,3:已完成)
     */
    private Integer assessmentStatus;

    /**
     * 风险等级(1:低,2:中,3:高,4:极高)
     */
    private Integer riskLevel;

    /**
     * 审批状态(1:待审批,2:已审批,3:已驳回)
     */
    private Integer approvalStatus;

    /**
     * 评估人ID
     */
    private Long assessorId;

    /**
     * 评估人姓名
     */
    private String assessorName;

    /**
     * 审批人ID
     */
    private Long approverId;

    /**
     * 审批人姓名
     */
    private String approverName;

    /**
     * 最小总分
     */
    private BigDecimal minTotalScore;

    /**
     * 最大总分
     */
    private BigDecimal maxTotalScore;

    /**
     * 评估开始日期
     */
    private Date assessmentStartDate;

    /**
     * 评估结束日期
     */
    private Date assessmentEndDate;

    /**
     * 审批开始日期
     */
    private Date approvalStartDate;

    /**
     * 审批结束日期
     */
    private Date approvalEndDate;

    /**
     * 创建开始时间
     */
    private Date createStartTime;

    /**
     * 创建结束时间
     */
    private Date createEndTime;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 创建人姓名
     */
    private String createByName;

    /**
     * 是否只查询高风险项目
     */
    private Boolean onlyHighRisk;

    /**
     * 是否只查询待审批项目
     */
    private Boolean onlyPendingApproval;

    /**
     * 是否只查询我的评估
     */
    private Boolean onlyMyAssessment;

    /**
     * 当前用户ID（用于查询我的评估）
     */
    private Long currentUserId;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向（ASC/DESC）
     */
    private String orderDirection;

    /**
     * 关键词搜索（评估编号、评估名称、相对方名称）
     */
    private String keyword;
}
