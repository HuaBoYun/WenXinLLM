package com.huabo.contract.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 招投标项目查询参数
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BiddingProjectQueryParam implements Serializable {

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
     * 招投标编号
     */
    private String biddingNo;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 业主名称
     */
    private String ownerName;

    /**
     * 项目类型(1:公开招标,2:邀请招标,3:竞争性谈判,4:单一来源)
     */
    private Integer projectType;

    /**
     * 招标方式(1:公开招标,2:邀请招标,3:竞争性谈判,4:单一来源)
     */
    private Integer biddingMethod;

    /**
     * 项目经理ID
     */
    private Long managerId;

    /**
     * 项目经理姓名
     */
    private String managerName;

    /**
     * 项目规模
     */
    private String projectScale;

    /**
     * 预算金额最小值
     */
    private BigDecimal minBudgetAmount;

    /**
     * 预算金额最大值
     */
    private BigDecimal maxBudgetAmount;

    /**
     * 保证金金额最小值
     */
    private BigDecimal minGuaranteeAmount;

    /**
     * 保证金金额最大值
     */
    private BigDecimal maxGuaranteeAmount;

    /**
     * 项目状态(1:发布,2:投标中,3:开标,4:评标,5:中标公示,6:完成,7:流标)
     */
    private Integer projectStatus;

    /**
     * 是否参与投标(0:否,1:是)
     */
    private Integer isParticipate;

    /**
     * 参与状态(1:准备中,2:已投标,3:中标,4:未中标)
     */
    private Integer participateStatus;

    /**
     * 招标公告发布开始时间
     */
    private Date announcementStartDate;

    /**
     * 招标公告发布结束时间
     */
    private Date announcementEndDate;

    /**
     * 投标截止开始时间
     */
    private Date bidDeadlineStartDate;

    /**
     * 投标截止结束时间
     */
    private Date bidDeadlineEndDate;

    /**
     * 开标开始时间
     */
    private Date bidOpeningStartDate;

    /**
     * 开标结束时间
     */
    private Date bidOpeningEndDate;

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
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系邮箱
     */
    private String contactEmail;

    /**
     * 是否只查询可投标项目
     */
    private Boolean onlyCanBid;

    /**
     * 是否只查询已过期项目
     */
    private Boolean onlyExpired;

    /**
     * 是否只查询重点项目
     */
    private Boolean onlyKeyProject;

    /**
     * 是否只查询紧急项目
     */
    private Boolean onlyUrgent;

    /**
     * 是否只查询我参与的项目
     */
    private Boolean onlyMyParticipate;

    /**
     * 是否只查询中标项目
     */
    private Boolean onlyWinning;

    /**
     * 当前用户ID（用于查询我参与的项目）
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
     * 关键词搜索（项目编号、项目名称、招标方名称）
     */
    private String keyword;

    /**
     * 地区
     */
    private String region;

    /**
     * 行业
     */
    private String industry;

    /**
     * 开标地点
     */
    private String bidOpeningLocation;
}
