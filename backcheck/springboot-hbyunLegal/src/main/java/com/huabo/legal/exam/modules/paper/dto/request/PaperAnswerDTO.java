package com.huabo.legal.exam.modules.paper.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author bool
 */
@Data
@Schema(name="查找试卷题目详情请求类", description="查找试卷题目详情请求类")
public class PaperAnswerDTO extends PaperQuQueryDTO {

    @Schema(name = "回答列表", required = true)
    private List<String> answers;

    @Schema(name = "主观答案", required = true)
    private String answer;

}
