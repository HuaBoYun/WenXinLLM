package com.huabo.financialdata.jdbc;

import lombok.*;

import java.io.Serializable;

/**
 * @Description: 字段VO
 */
@Getter
@Setter
@ToString
public class JdbcColumnVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    private String name;

    /**
     * 注释
     */
    private String comment;

    /**
     * 字段类型
     */
    private String fieldType;

    /**
     * 索引名称
     */
    private String indexName;

}
