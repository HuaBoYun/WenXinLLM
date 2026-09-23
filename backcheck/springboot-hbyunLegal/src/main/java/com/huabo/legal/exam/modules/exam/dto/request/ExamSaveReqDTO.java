package com.huabo.legal.exam.modules.exam.dto.request;

import com.huabo.legal.exam.modules.exam.dto.ExamDTO;
import com.huabo.legal.exam.modules.exam.dto.ext.ExamRepoExtDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
* <p>
* 考试保存请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-07-25 16:18
*/
@Data
@Schema(name="考试保存请求类", description="考试保存请求类")
public class ExamSaveReqDTO extends ExamDTO {

    private static final long serialVersionUID = 1L;


    @Schema(name = "题库列表", required = true)
    private List<ExamRepoExtDTO> repoList;

    @Schema(name = "考试部门列表", required = true)
    private List<String> departIds;

}
