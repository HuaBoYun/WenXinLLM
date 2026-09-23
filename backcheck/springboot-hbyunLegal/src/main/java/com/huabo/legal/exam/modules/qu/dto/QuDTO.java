package com.huabo.legal.exam.modules.qu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
* <p>
* 问题题目请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-05-25 13:23
*/
@Data
@Schema(name="问题题目", description="问题题目")
public class QuDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Schema(name = "题目ID", required = true)
    private String id;

    @Schema(name = "题目类型", required = true)
    private Integer quType;

    @Schema(name = "1普通,2较难", required = true)
    private Integer levela;

    @Schema(name = "题目图片", required = true)
    private String image;

    @Schema(name = "题目内容", required = true)
    private String content;
    
    @Schema(name = "所属题库名称", required = true)
    private String title;
    
    @Schema(name = "所属题库id", required = true)
    private String tblrepoid;


    @Schema(name = "创建时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(name = "更新时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Schema(name = "题目备注", required = true)
    private String remark;

    @Schema(name = "整题解析", required = true)
    private String analysis;
    
}
