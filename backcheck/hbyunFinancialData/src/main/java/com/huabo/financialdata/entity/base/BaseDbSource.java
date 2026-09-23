package com.huabo.financialdata.entity.base;

import com.huabo.financialdata.config.annotations.DbSourceTypeField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据源类型 - 通用
 *
 * @author lee
 * @version 1.0.0
 **/
@Data
public class BaseDbSource {

    /**
     * 数据库源
     */
    @Schema(hidden=true)
    private String dbSource;

    /**
     * 数据源类型
     */
    @DbSourceTypeField
    @Schema(hidden=true)
    private String dbSourceType;

    /**
     * 年费
     */
    @Schema(hidden=true)
    private String year;

}
