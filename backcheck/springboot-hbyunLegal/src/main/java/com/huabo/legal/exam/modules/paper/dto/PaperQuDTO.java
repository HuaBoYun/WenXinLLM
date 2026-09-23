package com.huabo.legal.exam.modules.paper.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
* <p>
* 试卷考题请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-05-25 17:31
*/
@Data
@Schema(name="试卷考题", description="试卷考题")
public class PaperQuDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Schema(name = "ID", required = true)
    private String id;

    @Schema(name = "试卷ID", required = true)
    private String paperId;

    @Schema(name = "题目ID", required = true)
    private String quId;

    @Schema(name = "题目类型", required = true)
    private Integer quType;

    @Schema(name = "是否已答", required = true)
    private Boolean answered;

    @Schema(name = "主观答案", required = true)
    private String answer;

    @Schema(name = "问题排序", required = true)
    private Integer sort;

    @Schema(name = "单题分分值", required = true)
    private Integer score;

    @Schema(name = "实际得分(主观题)", required = true)
    private Integer actualScore;

    @Schema(name = "是否答对", required = true)
    private Boolean isRight;
    
}
