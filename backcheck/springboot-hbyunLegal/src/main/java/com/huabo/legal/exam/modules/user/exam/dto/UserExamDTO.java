package com.huabo.legal.exam.modules.user.exam.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.legal.exam.core.annon.Dict;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

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
public class UserExamDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    private String id;
    
    @Schema(name = "用户ID", required = true)
    private String userId;

    @Dict(dictTable = "el_exam", dicText = "title", dicCode = "id")
    @Schema(name = "考试ID", required = true)
    private String examId;
    
    @Schema(name = "考试次数", required = true)
    private Integer tryCount;
    
    @Schema(name = "最高分数", required = true)
    private Integer maxScore;
    
    @Schema(name = "是否通过", required = true)
    private Boolean passed;
    
    @Schema(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    @Schema(name = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
}
