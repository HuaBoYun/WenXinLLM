package com.huabo.legal.exam.modules.user.book.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
* <p>
* 错题本请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-05-27 17:56
*/
@Data
@Schema(name="错题本", description="错题本")
public class UserBookDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Schema(name = "ID", required = true)
    private String id;

    @Schema(name = "考试ID", required = true)
    private String examId;

    @Schema(name = "用户ID", required = true)
    private String userId;

    @Schema(name = "题目ID", required = true)
    private String quId;

    @Schema(name = "加入时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(name = "最近错误时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Schema(name = "错误时间", required = true)
    private Integer wrongCount;

    @Schema(name = "题目标题", required = true)
    private String title;

    @Schema(name = "错题序号", required = true)
    private Integer sort;

}