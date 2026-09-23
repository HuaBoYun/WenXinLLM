package com.huabo.legal.exam.modules.qu.dto.ext;

import com.huabo.legal.exam.modules.qu.dto.QuAnswerDTO;
import com.huabo.legal.exam.modules.qu.dto.QuDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
* <p>
* 问题题目请求类
* </p>
* @author 聪明笨狗
* @since 2020-05-25 13:23
*/
@Data
@Schema(name="问题题目详情", description="问题题目详情")
public class QuDetailDTO extends QuDTO {

    private static final long serialVersionUID = 1L;
    
    
    @Schema(name = "备选项列表", required = true)
    private List<QuAnswerDTO> answerList;

    @Schema(name = "题库列表", required = true)
    private List<String> repoIds;

    
}
