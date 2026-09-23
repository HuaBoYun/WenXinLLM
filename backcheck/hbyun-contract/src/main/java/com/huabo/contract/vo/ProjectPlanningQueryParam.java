package com.huabo.contract.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目策划查询参数
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProjectPlanningQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码
     */
    private Integer pageNumber;

    /**
     * 页大小
     */
    private Integer pageSize;

    /**
     * 策划编号
     */
    private String planningNo;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 策划名称
     */
    private String planningName;

    /**
     * 策划类型(1:可行性研究,2:初步设计,3:施工图设计,4:专项方案,5:其他)
     */
    private Integer planningType;

    /**
     * 策划阶段(1:前期策划,2:设计策划,3:施工策划,4:运营策划)
     */
    private Integer planningStage;

    /**
     * 策划状态(1:草稿,2:待审核,3:已审核,4:执行中,5:已完成,6:已暂停,7:已取消)
     */
    private Integer planningStatus;

    /**
     * 负责人ID
     */
    private Long responsiblePersonId;

    /**
     * 负责人姓名
     */
    private String responsiblePersonName;

    /**
     * 审核人ID
     */
    private Long reviewerId;

    /**
     * 审核人姓名
     */
    private String reviewerName;

    /**
     * 预算金额最小值
     */
    private BigDecimal minBudgetAmount;

    /**
     * 预算金额最大值
     */
    private BigDecimal maxBudgetAmount;

    /**
     * 完成度最小值
     */
    private BigDecimal minCompletionRate;

    /**
     * 完成度最大值
     */
    private BigDecimal maxCompletionRate;

    /**
     * 计划开始时间-开始
     */
    private Date plannedStartDateBegin;

    /**
     * 计划开始时间-结束
     */
    private Date plannedStartDateEnd;

    /**
     * 计划结束时间-开始
     */
    private Date plannedEndDateBegin;

    /**
     * 计划结束时间-结束
     */
    private Date plannedEndDateEnd;

    /**
     * 实际开始时间-开始
     */
    private Date actualStartDateBegin;

    /**
     * 实际开始时间-结束
     */
    private Date actualStartDateEnd;

    /**
     * 实际结束时间-开始
     */
    private Date actualEndDateBegin;

    /**
     * 实际结束时间-结束
     */
    private Date actualEndDateEnd;

    /**
     * 审核时间-开始
     */
    private Date reviewTimeBegin;

    /**
     * 审核时间-结束
     */
    private Date reviewTimeEnd;

    /**
     * 创建时间-开始
     */
    private Date createTimeBegin;

    /**
     * 创建时间-结束
     */
    private Date createTimeEnd;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 创建人姓名
     */
    private String createByName;

    /**
     * 是否只查询可编辑的策划
     */
    private Boolean onlyCanEdit;

    /**
     * 是否只查询待审核的策划
     */
    private Boolean onlyPendingReview;

    /**
     * 是否只查询执行中的策划
     */
    private Boolean onlyInProgress;

    /**
     * 是否只查询已完成的策划
     */
    private Boolean onlyCompleted;

    /**
     * 是否只查询延期的策划
     */
    private Boolean onlyDelayed;

    /**
     * 是否只查询即将到期的策划
     */
    private Boolean onlyExpiringSoon;

    /**
     * 是否只查询重点策划
     */
    private Boolean onlyKeyPlanning;

    /**
     * 是否只查询我负责的策划
     */
    private Boolean onlyMyResponsible;

    /**
     * 是否只查询我参与的策划
     */
    private Boolean onlyMyParticipate;

    /**
     * 当前用户ID（用于查询我负责/参与的策划）
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
     * 关键词搜索（策划编号、策划名称、项目名称）
     */
    private String keyword;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 参与人员
     */
    private String participants;
}
