package com.huabo.legal.exam.modules.exam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
* <p>
* 考试题库数据传输类
* </p>
*
* @author 聪明笨狗
* @since 2020-09-05 11:14
*/
@Data
@Schema(name="考试题库", description="考试题库")
public class ExamRepoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    @Schema(name = "ID", required = true)
    private String id;
    
    @Schema(name = "考试ID", required = true)
    private String examId;
    
    @Schema(name = "题库ID", required = true)
    private String repoId;
    
    @Schema(name = "单选题数量", required = true)
    private Integer radioCount;
    
    @Schema(name = "单选题分数", required = true)
    private Integer radioScore;
    
    @Schema(name = "多选题数量", required = true)
    private Integer multiCount;
    
    @Schema(name = "多选题分数", required = true)
    private Integer multiScore;
    
    @Schema(name = "判断题数量", required = true)
    private Integer judgeCount;
    
    @Schema(name = "判断题分数", required = true)
    private Integer judgeScore;
    
}
