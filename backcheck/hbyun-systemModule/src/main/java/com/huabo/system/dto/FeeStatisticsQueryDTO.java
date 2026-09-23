package com.huabo.system.dto;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "FeeStatisticsQueryDTO", description = "费用统计查询参数")
public class FeeStatisticsQueryDTO {

    @Schema(name = "维度：personal=个人, company=公司, group=集团")
    private String dimension;

    @Schema(name = "开始时间")
    private Date startTime;

    @Schema(name = "结束时间")
    private Date endTime;

    @Schema(name = "模块类型")
    private String moduleType;

    @Schema(name = "页码")
    private Integer pageNum = 1;

    @Schema(name = "每页条数")
    private Integer pageSize = 20;
}
