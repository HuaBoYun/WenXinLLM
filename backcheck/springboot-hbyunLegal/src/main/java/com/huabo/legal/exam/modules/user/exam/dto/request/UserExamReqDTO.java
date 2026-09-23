package com.huabo.legal.exam.modules.user.exam.dto.request;

import com.huabo.legal.exam.modules.user.exam.dto.UserExamDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* <p>
* 考试记录数据传输类
* </p>
*
* @author 聪明笨狗
* @since 2020-09-21 15:13
*/
@Data
@Schema(name="考试记录", description="考试记录")
public class UserExamReqDTO extends UserExamDTO {

    private static final long serialVersionUID = 1L;


    @Schema(name = "考试名称", required = true)
    private String title;

    @Schema(name = "人员名称", required = true)
    private String realName;


}
