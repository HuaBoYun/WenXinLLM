package com.huabo.legal.exam.modules.paper.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.legal.exam.core.annon.Dict;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
* <p>
* 试卷请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-05-25 17:31
*/
@Data
@Schema(name="试卷", description="试卷")
public class PaperDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Schema(name = "试卷ID", required = true)
    private String id;

    @Dict(dictTable = "sys_user", dicText = "real_name", dicCode = "id")
    @Schema(name = "用户ID", required = true)
    private String userId;

    @Dict(dictTable = "sys_depart", dicText = "dept_name", dicCode = "id")
    @Schema(name = "部门ID", required = true)
    private String departId;

    @Schema(name = "规则ID", required = true)
    private String examId;

    @Schema(name = "考试标题", required = true)
    private String title;

    @Schema(name = "考试时长", required = true)
    private Integer totalTime;

    @Schema(name = "用户时长", required = true)
    private Integer userTime;

    @Schema(name = "试卷总分", required = true)
    private Integer totalScore;

    @Schema(name = "及格分", required = true)
    private Integer qualifyScore;

    @Schema(name = "客观分", required = true)
    private Integer objScore;

    @Schema(name = "主观分", required = true)
    private Integer subjScore;

    @Schema(name = "用户得分", required = true)
    private Integer userScore;

    @Schema(name = "是否包含简答题", required = true)
    private Boolean hasSaq;

    @Schema(name = "试卷状态", required = true)
    private Integer state;

    @Schema(name = "创建时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(name = "更新时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Schema(name = "截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date limitTime;
    
}
