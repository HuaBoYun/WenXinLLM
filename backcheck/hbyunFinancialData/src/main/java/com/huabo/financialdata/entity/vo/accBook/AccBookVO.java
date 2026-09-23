package com.huabo.financialdata.entity.vo.accBook;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 账簿管理 - 分页查询 - 返回对象
 *
 * @author lee
 * @version 1.0.0
 **/
@Data
@Schema(name="APP版本管理 - 根据ID集合查询 - 返回对象")
public class AccBookVO implements Serializable {

    @Schema(name = "主键")
    private String bookId;

    @Schema(name = "数据源名称")
    private String acctId;

    @Schema(name = "账簿名称")
    private String bookName;

    @Schema(name = "公司ID")
    private BigDecimal orgId;

    @Schema(name = "公司名称")
    private String orgName;

    @Schema(name = "年份")
    private String bookYear;

    @Schema(name = "账簿描述")
    private String bookDesc;

}
