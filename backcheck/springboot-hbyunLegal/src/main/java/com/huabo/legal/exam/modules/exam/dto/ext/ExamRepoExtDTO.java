package com.huabo.legal.exam.modules.exam.dto.ext;

import com.huabo.legal.exam.modules.exam.dto.ExamRepoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* <p>
* 考试题库数据传输类
* </p>
*
* @author 聪明笨狗
* @since 2020-09-05 11:14
*/
@Data
@Schema(name="考试题库扩展响应类", description="考试题库扩展响应类")
public class ExamRepoExtDTO extends ExamRepoDTO {

    private static final long serialVersionUID = 1L;

    
    @Schema(name = "单选题总量", required = true)
    private Integer totalRadio;
    
    @Schema(name = "多选题总量", required = true)
    private Integer totalMulti;
    
    @Schema(name = "判断题总量", required = true)
    private Integer totalJudge;
    
}
