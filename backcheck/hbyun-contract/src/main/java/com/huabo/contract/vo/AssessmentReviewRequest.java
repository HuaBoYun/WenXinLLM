package com.huabo.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 考核审核请求参数
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@Accessors(chain = true)
@Schema(name="AssessmentReviewRequest", description="考核审核请求参数")
public class AssessmentReviewRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "审核结果：approved-通过，rejected-驳回", required = true)
    @NotBlank(message = "审核结果不能为空")
    private String reviewResult;

    @Schema(name = "审核意见")
    private String reviewComments;

    @Schema(name = "审核人ID", required = true)
    @NotNull(message = "审核人ID不能为空")
    private Long reviewerId;
}
