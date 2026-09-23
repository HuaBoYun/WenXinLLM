package com.huabo.legal.exam.modules.paper.dto.request;

import com.huabo.legal.exam.core.api.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author bool
 */
@Data
@Schema(name="试卷创建请求类", description="试卷创建请求类")
public class PaperCreateReqDTO extends BaseDTO {

    @JsonIgnore
    private String userId;

    @Schema(name = "考试ID", required = true)
    private String examId;

}
