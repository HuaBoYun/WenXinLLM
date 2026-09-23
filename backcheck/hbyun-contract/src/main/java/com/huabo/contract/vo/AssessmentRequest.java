package com.huabo.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 启动考核请求参数
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@Accessors(chain = true)
@Schema(name="AssessmentRequest", description="启动考核请求参数")
public class AssessmentRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "项目ID", required = true)
    @NotBlank(message = "项目ID不能为空")
    private String projectId;

    @Schema(name = "考核类型：1-月度，2-季度，3-年度，4-项目完成", required = true)
    @NotNull(message = "考核类型不能为空")
    private Integer assessmentType;

    @Schema(name = "考核期间，格式：YYYY-MM", required = true)
    @NotBlank(message = "考核期间不能为空")
    private String assessmentPeriod;

    @Schema(name = "考核人ID", required = true)
    @NotNull(message = "考核人ID不能为空")
    private Long assessorId;

    @Schema(name = "备注信息")
    private String remarks;
}
