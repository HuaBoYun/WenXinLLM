package com.huabo.system.dto;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "FeeRecordQueryDTO", description = "费用记录查询参数")
public class FeeRecordQueryDTO {

    @Schema(name = "开始时间")
    private String startTime;

    @Schema(name = "结束时间")
    private String endTime;

    @Schema(name = "大模块类型")
    private String moduleType;

    @Schema(name = "小模块ID")
    private BigDecimal rightId;

    @Schema(name = "小模块名称")
    private String subModuleName;

    @Schema(name = "页码")
    private Integer pageNum = 1;

    @Schema(name = "每页条数")
    private Integer pageSize = 20;
}
