package com.huabo.contract.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 项目变更管理查询参数
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ChangeManagementQueryParam implements Serializable {

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
     * 变更编号
     */
    private String changeNo;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 变更标题
     */
    private String changeTitle;

    /**
     * 变更类型(1:设计变更,2:工程变更,3:合同变更,4:进度变更,5:成本变更,6:其他变更)
     */
    private Integer changeType;

    /**
     * 变更等级(1:重大变更,2:一般变更,3:轻微变更)
     */
    private Integer changeLevel;

    /**
     * 变更类型列表
     */
    private List<Integer> changeTypeList;

    /**
     * 变更类别(1:重大变更,2:一般变更,3:轻微变更)
     */
    private Integer changeCategory;

    /**
     * 变更状态
     */
    private Integer changeStatus;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 紧急程度
     */
    private Integer urgency;

    /**
     * 是否紧急(0:否,1:是)
     */
    private Integer isUrgent;

    /**
     * 申请人ID
     */
    private Long applicantId;

    /**
     * 申请人姓名
     */
    private String applicantName;

    /**
     * 审核人ID
     */
    private Long reviewerId;

    /**
     * 批准人ID
     */
    private Long approverId;

    /**
     * 实施人ID
     */
    private Long implementerId;

    /**
     * 最小成本影响
     */
    private BigDecimal minCostImpact;

    /**
     * 最大成本影响
     */
    private BigDecimal maxCostImpact;

    /**
     * 最小进度影响
     */
    private Integer minScheduleImpact;

    /**
     * 最大进度影响
     */
    private Integer maxScheduleImpact;

    /**
     * 申请开始日期
     */
    private Date applicationStartDate;

    /**
     * 申请结束日期
     */
    private Date applicationEndDate;

    /**
     * 实施开始日期
     */
    private Date implementationStartDate;

    /**
     * 实施结束日期
     */
    private Date implementationEndDate;

    /**
     * 关键词搜索
     */
    private String keyword;

    /**
     * 创建时间开始
     */
    private Date createStartTime;

    /**
     * 创建时间结束
     */
    private Date createEndTime;

    /**
     * 创建人
     */
    private Long createBy;
}
