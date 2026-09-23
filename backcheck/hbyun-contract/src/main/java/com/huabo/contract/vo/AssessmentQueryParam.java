package com.huabo.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 项目考核查询参数
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Schema(name="AssessmentQueryParam", description="项目考核查询参数")
public class AssessmentQueryParam extends BaseQueryParam {

    @Schema(name = "项目ID")
    private String projectId;

    @Schema(name = "项目名称")
    private String projectName;

    @Schema(name = "考核期间")
    private String assessmentPeriod;

    @Schema(name = "考核类型：1-月度，2-季度，3-年度，4-项目完成")
    private Integer assessmentType;

    @Schema(name = "考核状态：1-进行中，2-已完成，3-已审核")
    private Integer assessmentStatus;

    @Schema(name = "考核等级")
    private String assessmentLevel;

    @Schema(name = "考核人ID")
    private Long assessorId;

    @Schema(name = "审核人ID")
    private Long reviewerId;

    @Schema(name = "开始日期")
    private String startDate;

    @Schema(name = "结束日期")
    private String endDate;

    @Schema(name = "最低分数")
    private Double minScore;

    @Schema(name = "最高分数")
    private Double maxScore;

    @Schema(name = "部门ID")
    private Long deptId;

    @Schema(name = "排序字段")
    private String orderBy;

    @Schema(name = "排序方向：asc-升序，desc-降序")
    private String orderDirection;
}
