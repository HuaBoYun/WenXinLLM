package com.huabo.cybermonitor.vo;

import java.time.LocalDateTime;

import com.huabo.cybermonitor.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 内控管理查询VO
 *
 * @author huabo
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name="InternalControlQueryVo", description="内控管理查询VO")
public class InternalControlQueryVo extends BaseVo {

    @Schema(name = "企业ID")
    private String enterpriseId;

    @Schema(name = "企业名称")
    private String enterpriseName;

    @Schema(name = "内控类型")
    private String controlType;

    @Schema(name = "内控名称")
    private String controlName;

    @Schema(name = "控制层级")
    private String controlLevel;

    @Schema(name = "责任部门")
    private String responsibleDepartment;

    @Schema(name = "责任人")
    private String responsiblePerson;

    @Schema(name = "监督部门")
    private String supervisionDepartment;

    @Schema(name = "制度状态")
    private String systemStatus;

    @Schema(name = "执行状态")
    private String executionStatus;

    @Schema(name = "风险等级")
    private String riskLevel;

    @Schema(name = "改进状态")
    private String improvementStatus;

    @Schema(name = "生效时间-开始")
    private LocalDateTime effectiveTimeBegin;

    @Schema(name = "生效时间-结束")
    private LocalDateTime effectiveTimeEnd;

    @Schema(name = "最后执行时间-开始")
    private LocalDateTime lastExecutionTimeBegin;

    @Schema(name = "最后执行时间-结束")
    private LocalDateTime lastExecutionTimeEnd;

    @Schema(name = "评估时间-开始")
    private LocalDateTime assessmentTimeBegin;

    @Schema(name = "评估时间-结束")
    private LocalDateTime assessmentTimeEnd;

    @Schema(name = "有效性评分-最小值")
    private String effectivenessScoreMin;

    @Schema(name = "有效性评分-最大值")
    private String effectivenessScoreMax;

    @Schema(name = "综合评分-最小值")
    private String overallScoreMin;

    @Schema(name = "综合评分-最大值")
    private String overallScoreMax;
}
