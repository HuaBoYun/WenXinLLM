package com.huabo.legal.exam.modules.repo.dto.response;

import com.huabo.legal.exam.modules.repo.dto.RepoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* <p>
* 题库请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-05-25 13:23
*/
@Data
@Schema(name="题库分页请求类", description="题库分页请求类")
public class RepoRespDTO extends RepoDTO {

    private static final long serialVersionUID = 1L;

    @Schema(name = "多选题数量", required = true)
    private Integer multiCount;

    @Schema(name = "单选题数量", required = true)
    private Integer radioCount;

    @Schema(name = "判断题数量", required = true)
    private Integer judgeCount;

}
