package com.huabo.fxgl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 催办提醒数据传输对象
 *
 * @author AI Assistant
 * @since 2025-01-07
 */
@Data
@Schema(description = "催办提醒数据传输对象")
public class ReminderDTO {

    @Schema(description = "内容")
    private String nr;

    @Schema(description = "类型")
    private String lx;

    @Schema(description = "发送人")
    private String xfr;

    @Schema(description = "发送时间")
    private String xfsj;
}
