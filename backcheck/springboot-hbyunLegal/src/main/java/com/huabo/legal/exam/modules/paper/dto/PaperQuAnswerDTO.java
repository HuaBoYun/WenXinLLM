package com.huabo.legal.exam.modules.paper.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

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
public class PaperQuAnswerDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Schema(name = "自增ID", required = true)
    private String id;

    @Schema(name = "试卷ID", required = true)
    private String paperId;

    @Schema(name = "回答项ID", required = true)
    private String answerId;

    @Schema(name = "题目ID", required = true)
    private String quId;

    @Schema(name = "是否正确项", required = true)
    private Boolean isRight;

    @Schema(name = "是否选中", required = true)
    private Boolean checked;

    @Schema(name = "排序", required = true)
    private Integer sort;

    @Schema(name = "选项标签", required = true)
    private String abc;
    
}
