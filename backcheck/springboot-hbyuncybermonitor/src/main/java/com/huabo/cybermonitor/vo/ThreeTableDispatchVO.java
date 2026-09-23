package com.huabo.cybermonitor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 三表比对派单请求VO
 * @author system
 * @date 2025-01-01
 */
@Data
@Schema(name = "ThreeTableDispatchVO", description = "三表比对差异核查派单请求参数")
public class ThreeTableDispatchVO {

    @Schema(description = "关联三表比对ID", required = true)
    private String compareId;

    @Schema(description = "差异企业名称")
    private String companyName;

    @Schema(description = "核查责任人", required = true)
    private String owner;

    @Schema(description = "核查期限", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String deadline;

    @Schema(description = "核查要求")
    private String requirement;
}
