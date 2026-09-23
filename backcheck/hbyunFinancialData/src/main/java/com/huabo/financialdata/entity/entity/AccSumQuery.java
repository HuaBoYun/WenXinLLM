package com.huabo.financialdata.entity.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 科目余额实体 - 扩展查询
 *
 * @author lee
 * @version 1.0.0
 **/
@Data
@Schema(name=" 科目余额表-扩展查询 ")
public class AccSumQuery extends AccSum implements Serializable {
    private static final long serialVersionUID = 5138561265118813853L;

    /**
     * 最小月份
     */
    @Schema(name = "最小月份")
    private Integer minMonth;

    /**
     * 最大月份
     */
    @Schema(name = "最大月份")
    private Integer maxMonth;


    /**
     * 数据库源
     */
    @Schema(name = "数据库源")
    private String dbSource;

    /**
     * 排序字段
     */
    @Schema(name = "排序字段")
    private String orderByClause;

    /**
     * 等于，不等于，小于，小于等于，大于等于，大于，包含，不包含
     */
    @Schema(name = " 等于，不等于，小于，小于等于，大于等于，大于，包含，不包含")
    private String status;
}
