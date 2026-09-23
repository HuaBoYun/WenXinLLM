package com.huabo.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 进度管理查询参数
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name="ProgressQueryParam", description="进度管理查询参数")
public class ProgressQueryParam extends BaseQueryParam {

    @Schema(name = "项目ID")
    private Long projectId;

    @Schema(name = "进度编号")
    private String progressNo;

    @Schema(name = "进度名称")
    private String progressName;

    @Schema(name = "进度类型：1-总体进度，2-阶段进度，3-任务进度")
    private Integer progressType;

    @Schema(name = "父级进度ID")
    private Long parentProgressId;

    @Schema(name = "计划开始时间-开始")
    private Date plannedStartTimeBegin;

    @Schema(name = "计划开始时间-结束")
    private Date plannedStartTimeEnd;

    @Schema(name = "计划结束时间-开始")
    private Date plannedEndTimeBegin;

    @Schema(name = "计划结束时间-结束")
    private Date plannedEndTimeEnd;

    @Schema(name = "实际开始时间-开始")
    private Date actualStartTimeBegin;

    @Schema(name = "实际开始时间-结束")
    private Date actualStartTimeEnd;

    @Schema(name = "实际结束时间-开始")
    private Date actualEndTimeBegin;

    @Schema(name = "实际结束时间-结束")
    private Date actualEndTimeEnd;

    @Schema(name = "最小计划工期")
    private Integer minPlannedDuration;

    @Schema(name = "最大计划工期")
    private Integer maxPlannedDuration;

    @Schema(name = "最小实际工期")
    private Integer minActualDuration;

    @Schema(name = "最大实际工期")
    private Integer maxActualDuration;

    @Schema(name = "最小计划完成百分比")
    private BigDecimal minPlannedProgress;

    @Schema(name = "最大计划完成百分比")
    private BigDecimal maxPlannedProgress;

    @Schema(name = "最小实际完成百分比")
    private BigDecimal minActualProgress;

    @Schema(name = "最大实际完成百分比")
    private BigDecimal maxActualProgress;

    @Schema(name = "进度状态：1-未开始，2-进行中，3-已完成，4-已暂停，5-已取消")
    private Integer progressStatus;

    @Schema(name = "优先级：1-低，2-中，3-高，4-紧急")
    private Integer priority;

    @Schema(name = "负责人ID")
    private Long managerId;

    @Schema(name = "负责人姓名")
    private String managerName;

    @Schema(name = "里程碑标识：0-否，1-是")
    private Integer isMilestone;

    @Schema(name = "关键路径标识：0-否，1-是")
    private Integer isCriticalPath;

    @Schema(name = "创建人")
    private Long createBy;

    @Schema(name = "创建开始时间")
    private Date createStartTime;

    @Schema(name = "创建结束时间")
    private Date createEndTime;

    @Schema(name = "是否延期")
    private Boolean isDelayed;

    @Schema(name = "是否提前完成")
    private Boolean isEarlyCompletion;

    @Schema(name = "是否高优先级")
    private Boolean isHighPriority;

    @Schema(name = "关键词搜索")
    private String keyword;
}
