package com.huabo.finance.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 字段信息VO
 * 
 * @author 华博云开发团队
 * @since 2025-10-24
 */
@Data
@Schema(name = "FieldInfoVO", description = "字段信息视图对象")
public class FieldInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

   @Schema(name =  "字段名")
    private String fieldName;

   @Schema(name =  "字段类型(VARCHAR/NUMBER/DATE等)")
    private String fieldType;

   @Schema(name =  "字段注释")
    private String fieldComment;

   @Schema(name =  "字段长度")
    private Integer length;

   @Schema(name =  "字段精度")
    private Integer precision;

   @Schema(name =  "是否可为空(1-是,0-否)")
    private String nullable;

   @Schema(name =  "是否主键(1-是,0-否)")
    private String isPrimaryKey;
}

