package com.huabo.legal.exam.modules.qu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
* <p>
* 候选答案请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-05-25 13:23
*/
@Data
@Schema(name="候选答案", description="候选答案")
public class QuAnswerDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Schema(name = "答案ID", required = true)
    private String id;

    @Schema(name = "问题ID", required = true)
    private String quId;

    @Schema(name = "是否正确", required = true)
    private Boolean isRight;

    @Schema(name = "选项图片", required = true)
    private String image;

    @Schema(name = "答案内容", required = true)
    private String content;

    @Schema(name = "答案分析", required = true)
    private String analysis;
    
}
