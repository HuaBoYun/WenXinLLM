package com.huabo.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 质量检查管理查询参数
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name="QualityInspectionQueryParam", description="质量检查管理查询参数")
public class QualityInspectionQueryParam extends BaseQueryParam {

    @Schema(name = "项目ID")
    private Long projectId;

    @Schema(name = "检查编号")
    private String inspectionNo;

    @Schema(name = "检查名称")
    private String inspectionName;

    @Schema(name = "检查类型(1:自检,2:互检,3:专检,4:验收)")
    private Integer inspectionType;

    @Schema(name = "检查开始日期")
    private Date inspectionStartDate;

    @Schema(name = "检查结束日期")
    private Date inspectionEndDate;

    @Schema(name = "检查人ID")
    private Long inspectorId;

    @Schema(name = "检查结论(1:合格,2:不合格,3:待整改)")
    private Integer checkResult;

    @Schema(name = "整改负责人ID")
    private Long rectificationPersonId;

    @Schema(name = "整改状态(1:待整改,2:整改中,3:已整改,4:已验收)")
    private Integer rectificationStatus;

    @Schema(name = "整改期限开始")
    private Date rectificationDeadlineStart;

    @Schema(name = "整改期限结束")
    private Date rectificationDeadlineEnd;

    @Schema(name = "复查结果(1:合格,2:不合格)")
    private Integer recheckResult;

    @Schema(name = "复查人ID")
    private Long recheckPersonId;

    @Schema(name = "创建开始时间")
    private Date createStartTime;

    @Schema(name = "创建结束时间")
    private Date createEndTime;

    @Schema(name = "关键词搜索")
    private String keyword;
}
