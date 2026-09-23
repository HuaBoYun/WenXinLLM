package com.huabo.contract.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 任务书查询参数
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TaskAssignmentQueryParam implements Serializable {

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
     * 任务书编号
     */
    private String taskNo;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 任务书名称
     */
    private String taskName;

    /**
     * 任务类型(1:设计任务,2:施工任务,3:监理任务,4:咨询任务,5:其他任务)
     */
    private Integer taskType;

    /**
     * 任务类型列表
     */
    private List<Integer> taskTypeList;

    /**
     * 任务等级(1:一级,2:二级,3:三级,4:四级,5:五级)
     */
    private Integer taskLevel;

    /**
     * 任务等级列表
     */
    private List<Integer> taskLevelList;

    /**
     * 任务金额最小值
     */
    private BigDecimal taskAmountMin;

    /**
     * 任务金额最大值
     */
    private BigDecimal taskAmountMax;

    /**
     * 计划开始时间开始
     */
    private Date plannedStartDateStart;

    /**
     * 计划开始时间结束
     */
    private Date plannedStartDateEnd;

    /**
     * 计划结束时间开始
     */
    private Date plannedEndDateStart;

    /**
     * 计划结束时间结束
     */
    private Date plannedEndDateEnd;

    /**
     * 实际开始时间开始
     */
    private Date actualStartDateStart;

    /**
     * 实际开始时间结束
     */
    private Date actualStartDateEnd;

    /**
     * 实际结束时间开始
     */
    private Date actualEndDateStart;

    /**
     * 实际结束时间结束
     */
    private Date actualEndDateEnd;

    /**
     * 任务状态(1:草稿,2:待审核,3:已审核,4:已下达,5:执行中,6:已完成,7:已暂停,8:已取消)
     */
    private Integer taskStatus;

    /**
     * 任务状态列表
     */
    private List<Integer> taskStatusList;

    /**
     * 完成度最小值
     */
    private BigDecimal completionRateMin;

    /**
     * 完成度最大值
     */
    private BigDecimal completionRateMax;

    /**
     * 下达人ID
     */
    private Long issuerId;

    /**
     * 下达人姓名
     */
    private String issuerName;

    /**
     * 下达时间开始
     */
    private Date issueTimeStart;

    /**
     * 下达时间结束
     */
    private Date issueTimeEnd;

    /**
     * 接收人ID
     */
    private Long receiverId;

    /**
     * 接收人姓名
     */
    private String receiverName;

    /**
     * 接收时间开始
     */
    private Date receiveTimeStart;

    /**
     * 接收时间结束
     */
    private Date receiveTimeEnd;

    /**
     * 负责人ID
     */
    private Long responsiblePersonId;

    /**
     * 负责人姓名
     */
    private String responsiblePersonName;

    /**
     * 执行部门ID
     */
    private Long executeDepartmentId;

    /**
     * 执行部门名称
     */
    private String executeDepartmentName;

    /**
     * 审核人ID
     */
    private Long reviewerId;

    /**
     * 审核人姓名
     */
    private String reviewerName;

    /**
     * 审核时间开始
     */
    private Date reviewTimeStart;

    /**
     * 审核时间结束
     */
    private Date reviewTimeEnd;

    /**
     * 优先级(1:高,2:中,3:低)
     */
    private Integer priority;

    /**
     * 优先级列表
     */
    private List<Integer> priorityList;

    /**
     * 是否紧急(0:否,1:是)
     */
    private Integer isUrgent;

    /**
     * 是否重要(0:否,1:是)
     */
    private Integer isImportant;

    /**
     * 是否延期(0:否,1:是)
     */
    private Integer isDelayed;

    /**
     * 是否即将到期(0:否,1:是)
     */
    private Integer isExpiringSoon;

    /**
     * 是否大额任务(0:否,1:是)
     */
    private Integer isLargeTask;

    /**
     * 关键词搜索
     */
    private String keyword;

    /**
     * 创建时间开始
     */
    private Date createTimeStart;

    /**
     * 创建时间结束
     */
    private Date createTimeEnd;

    /**
     * 更新时间开始
     */
    private Date updateTimeStart;

    /**
     * 更新时间结束
     */
    private Date updateTimeEnd;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向(ASC/DESC)
     */
    private String orderDirection;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 组织ID
     */
    private Long organizationId;

    /**
     * 组织名称
     */
    private String organizationName;

    /**
     * 数据权限用户ID
     */
    private Long dataPermissionUserId;

    /**
     * 数据权限部门ID列表
     */
    private List<Long> dataPermissionDeptIds;

    /**
     * 是否包含子部门
     */
    private Boolean includeSubDept;

    /**
     * 统计开始时间
     */
    private Date statisticsStartTime;

    /**
     * 统计结束时间
     */
    private Date statisticsEndTime;

    /**
     * 统计类型(1:按月,2:按季度,3:按年)
     */
    private Integer statisticsType;

    /**
     * 分组字段
     */
    private String groupBy;

    /**
     * 是否需要统计
     */
    private Boolean needStatistics;

    /**
     * 导出类型(1:Excel,2:PDF,3:Word)
     */
    private Integer exportType;

    /**
     * 导出字段列表
     */
    private List<String> exportFields;

    /**
     * 是否导出明细
     */
    private Boolean exportDetail;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 自定义查询条件
     */
    private String customCondition;

    /**
     * 扩展参数
     */
    private String extendParams;
}
