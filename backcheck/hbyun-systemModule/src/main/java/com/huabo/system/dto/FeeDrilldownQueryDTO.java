package com.huabo.system.dto;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "FeeDrilldownQueryDTO", description = "费用穿透查询参数")
public class FeeDrilldownQueryDTO {

    @Schema(name = "大模块类型")
    private String moduleType;

    @Schema(name = "维度：personal=个人, company=公司, group=集团")
    private String dimension;

    @Schema(name = "开始时间")
    private Date startTime;

    @Schema(name = "结束时间")
    private Date endTime;
}
