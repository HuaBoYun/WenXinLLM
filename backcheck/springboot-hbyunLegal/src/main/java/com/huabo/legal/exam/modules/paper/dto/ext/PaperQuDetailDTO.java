package com.huabo.legal.exam.modules.paper.dto.ext;

import com.huabo.legal.exam.modules.paper.dto.PaperQuDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
* <p>
* 试卷考题请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-05-25 17:31
*/
@Data
@Schema(name="试卷题目详情类", description="试卷题目详情类")
public class PaperQuDetailDTO extends PaperQuDTO {

    private static final long serialVersionUID = 1L;

    @Schema(name = "图片", required = true)
    private String image;

    @Schema(name = "题目内容", required = true)
    private String content;

    @Schema(name = "答案内容", required = true)
    List<PaperQuAnswerExtDTO> answerList;
}
