package com.huabo.legal.exam.modules.paper.dto.response;

import com.huabo.legal.exam.modules.paper.dto.PaperDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* <p>
* 试卷请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-05-25 17:31
*/
@Data
@Schema(name="试卷列表响应类", description="试卷列表响应类")
public class PaperListRespDTO extends PaperDTO {

    private static final long serialVersionUID = 1L;

    @Schema(name = "人员", required = true)
    private String realName;

    
}
