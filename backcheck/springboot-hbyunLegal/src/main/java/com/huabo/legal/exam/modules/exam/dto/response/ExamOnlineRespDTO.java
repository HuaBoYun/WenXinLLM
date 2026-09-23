package com.huabo.legal.exam.modules.exam.dto.response;

import com.huabo.legal.exam.modules.exam.dto.ExamDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* <p>
* 考试分页响应类
* </p>
*
* @author 聪明笨狗
* @since 2020-07-25 16:18
*/
@Data
@Schema(name="在线考试分页响应类", description="在线考试分页响应类")
public class ExamOnlineRespDTO extends ExamDTO {

    private static final long serialVersionUID = 1L;


}
