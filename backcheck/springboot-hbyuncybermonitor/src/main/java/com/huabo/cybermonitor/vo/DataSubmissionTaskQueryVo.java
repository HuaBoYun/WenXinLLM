package com.huabo.cybermonitor.vo;

import java.time.LocalDateTime;

import com.huabo.cybermonitor.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据报送任务查询VO
 *
 * @author huabo
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name="DataSubmissionTaskQueryVo", description="数据报送任务查询VO")
public class DataSubmissionTaskQueryVo extends BaseVo {

    @Schema(name = "企业ID")
    private String enterpriseId;

    @Schema(name = "企业名称")
    private String enterpriseName;

    @Schema(name = "任务名称")
    private String taskName;

    @Schema(name = "任务类型")
    private String taskType;

    @Schema(name = "报送类型")
    private String submissionType;

    @Schema(name = "监管部门")
    private String regulatoryDepartment;

    @Schema(name = "任务来源")
    private String taskSource;

    @Schema(name = "优先级")
    private String priority;

    @Schema(name = "任务状态")
    private String taskStatus;

    @Schema(name = "负责人")
    private String responsiblePerson;

    @Schema(name = "负责部门")
    private String responsibleDepartment;

    @Schema(name = "计划开始时间-开始")
    private LocalDateTime plannedStartTimeBegin;

    @Schema(name = "计划开始时间-结束")
    private LocalDateTime plannedStartTimeEnd;

    @Schema(name = "计划结束时间-开始")
    private LocalDateTime plannedEndTimeBegin;

    @Schema(name = "计划结束时间-结束")
    private LocalDateTime plannedEndTimeEnd;

    @Schema(name = "截止时间-开始")
    private LocalDateTime deadlineBegin;

    @Schema(name = "截止时间-结束")
    private LocalDateTime deadlineEnd;

    @Schema(name = "反馈处理状态")
    private String feedbackStatus;

    @Schema(name = "是否逾期")
    private String isOverdue;

    @Schema(name = "进度范围-最小值")
    private String progressMin;

    @Schema(name = "进度范围-最大值")
    private String progressMax;

    @Schema(name = "质量评分-最小值")
    private String qualityScoreMin;

    @Schema(name = "质量评分-最大值")
    private String qualityScoreMax;
}
