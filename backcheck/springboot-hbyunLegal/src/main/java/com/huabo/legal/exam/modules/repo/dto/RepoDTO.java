package com.huabo.legal.exam.modules.repo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
* <p>
* 题库请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-05-25 13:23
*/
@Data
@Schema(name="题库", description="题库")
public class RepoDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Schema(name = "题库ID", required = true)
    private String id;

    @Schema(name = "题库编号", required = true)
    private String code;

    @Schema(name = "题库名称", required = true)
    private String title;

    @Schema(name = "单选数量", required = true)
    private Integer radioCount;

    @Schema(name = "多选数量", required = true)
    private Integer multiCount;

    @Schema(name = "判断数量", required = true)
    private Integer judgeCount;


    @Schema(name = "题库备注", required = true)
    private String remark;

    @Schema(name = "创建时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(name = "更新时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
}
