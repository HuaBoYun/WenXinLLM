package com.huabo.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目结算查询参数
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@Schema(name="ProjectSettlementQueryParam", description="项目结算查询参数")
public class ProjectSettlementQueryParam {

    @Schema(name = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(name = "每页大小", example = "10")
    private Integer pageSize = 10;

    @Schema(name = "项目ID")
    private Long projectId;

    @Schema(name = "结算编号")
    private String settlementNo;

    @Schema(name = "结算名称")
    private String settlementName;

    @Schema(name = "结算类型(1:进度结算,2:阶段结算,3:最终结算)")
    private Short settlementType;

    @Schema(name = "结算状态(1:待结算,2:结算中,3:已结算,4:已审核)")
    private Short settlementStatus;

    @Schema(name = "结算人ID")
    private Long settlorId;

    @Schema(name = "审核人ID")
    private Long reviewerId;

    @Schema(name = "结算日期开始")
    private Date settlementDateStart;

    @Schema(name = "结算日期结束")
    private Date settlementDateEnd;

    @Schema(name = "结算金额最小值")
    private BigDecimal settlementAmountMin;

    @Schema(name = "结算金额最大值")
    private BigDecimal settlementAmountMax;

    @Schema(name = "关键字搜索(结算编号、结算名称)")
    private String keyword;
}
