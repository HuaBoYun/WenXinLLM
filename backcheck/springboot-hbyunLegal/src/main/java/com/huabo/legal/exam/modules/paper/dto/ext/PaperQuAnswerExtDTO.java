package com.huabo.legal.exam.modules.paper.dto.ext;

import com.huabo.legal.exam.modules.paper.dto.PaperQuAnswerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* <p>
* 试卷考题备选答案请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-05-25 17:31
*/
@Data
@Schema(name="试卷考题备选答案", description="试卷考题备选答案")
public class PaperQuAnswerExtDTO extends PaperQuAnswerDTO {

    private static final long serialVersionUID = 1L;

    @Schema(name = "试题图片", required = true)
    private String image;

    @Schema(name = "答案内容", required = true)
    private String content;

    
}
