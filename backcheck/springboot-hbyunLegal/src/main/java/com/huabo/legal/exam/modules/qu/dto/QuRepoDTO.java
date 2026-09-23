package com.huabo.legal.exam.modules.qu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
* <p>
* 试题题库请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-05-25 13:23
*/
@Data
@Schema(name="试题题库", description="试题题库")
public class QuRepoDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private String id;

    @Schema(name = "试题", required = true)
    private String quId;

    @Schema(name = "归属题库", required = true)
    private String repoId;

    @Schema(name = "题目类型", required = true)
    private Integer quType;

    @Schema(name = "排序", required = true)
    private Integer sort;

}