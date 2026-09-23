package com.huabo.legal.exam.modules.paper.dto.response;

import com.huabo.legal.exam.modules.paper.dto.PaperDTO;
import com.huabo.legal.exam.modules.paper.dto.ext.PaperQuDetailDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name="考试结果展示响应类", description="考试结果展示响应类")
public class ExamResultRespDTO extends PaperDTO {

    @Schema(name = "问题列表", required = true)
    private List<PaperQuDetailDTO> quList;

}
