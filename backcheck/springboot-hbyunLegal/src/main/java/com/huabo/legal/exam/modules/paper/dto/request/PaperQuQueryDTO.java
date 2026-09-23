package com.huabo.legal.exam.modules.paper.dto.request;

import com.huabo.legal.exam.core.api.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author bool
 */
@Data
@Schema(name="查找试卷题目详情请求类", description="查找试卷题目详情请求类")
public class PaperQuQueryDTO extends BaseDTO {

    @Schema(name = "试卷ID", required = true)
    private String paperId;

    @Schema(name = "题目ID", required = true)
    private String quId;

}
