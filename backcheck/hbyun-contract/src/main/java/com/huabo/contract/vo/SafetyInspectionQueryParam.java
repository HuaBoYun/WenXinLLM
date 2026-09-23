package com.huabo.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 安全检查查询参数
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@Schema(name="SafetyInspectionQueryParam", description="安全检查查询参数")
public class SafetyInspectionQueryParam {

    @Schema(name = "页码")
    private Integer pageNum = 1;

    @Schema(name = "每页大小")
    private Integer pageSize = 10;

    @Schema(name = "项目ID")
    private Long projectId;

    @Schema(name = "检查编号")
    private String inspectionNo;

    @Schema(name = "检查名称")
    private String inspectionName;

    @Schema(name = "检查类型(1:日常检查,2:专项检查,3:综合检查)")
    private Integer inspectionType;

    @Schema(name = "检查开始日期")
    private Date inspectionDateStart;

    @Schema(name = "检查结束日期")
    private Date inspectionDateEnd;

    @Schema(name = "检查人ID")
    private Long inspectorId;

    @Schema(name = "检查地点")
    private String inspectionLocation;

    @Schema(name = "隐患等级(1:一般,2:较大,3:重大,4:特别重大)")
    private Integer hazardLevel;

    @Schema(name = "整改负责人ID")
    private Long rectificationPersonId;

    @Schema(name = "整改状态(1:待整改,2:整改中,3:已整改,4:已验收)")
    private Integer rectificationStatus;

    @Schema(name = "整改期限开始")
    private Date rectificationDeadlineStart;

    @Schema(name = "整改期限结束")
    private Date rectificationDeadlineEnd;

    @Schema(name = "紧急程度(1:一般,2:紧急,3:特急)")
    private Integer emergencyLevel;

    @Schema(name = "验证结果(1:合格,2:不合格)")
    private Integer verificationResult;

    @Schema(name = "验证人ID")
    private Long verificationPersonId;

    @Schema(name = "创建开始时间")
    private Date createStartTime;

    @Schema(name = "创建结束时间")
    private Date createEndTime;

    @Schema(name = "关键词搜索")
    private String keyword;
}
