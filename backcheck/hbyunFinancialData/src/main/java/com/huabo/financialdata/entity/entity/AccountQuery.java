package com.huabo.financialdata.entity.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author lee
 * @version 1.0.0
 **/
@Data
@Schema(name=" 财务科目表 结果集")
public class AccountQuery extends Account {

    /**
     * AccId集合
     */
    private List<String> accIdList;


    /**
     * 排序字段
     */
    @Schema(name = "排序字段")
    private String orderByClause;

    /**
     * 数据库源
     */
    @Schema(name = "数据库源")
    private String dbSource;

    /**
     * 等于，不等于，小于，小于等于，大于等于，大于，包含，不包含
     */
    @Schema(name = "状态：等于，不等于，小于，小于等于，大于等于，大于，包含，不包含")
    private String status;

    @Schema(name = "accIdStrs 追加")
    private String accIdStrs;
}
