package com.huabo.finance.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 源表信息VO
 * 
 * @author 华博云开发团队
 * @since 2025-10-24
 */
@Data
@Schema(name = "SourceTableVO", description = "源表信息视图对象")
public class SourceTableVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name =  "表名")
    private String tableName;

    @Schema(name =  "表注释")
    private String tableComment;

    @Schema(name =  "字段数量")
    private Integer fieldCount;

    @Schema(name =  "记录数量")
    private Integer recordCount;
}

