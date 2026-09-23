package com.huabo.financialdata.entity.dto;

import com.huabo.financialdata.entity.base.BasePageDO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 科目余额表 - 分页列表请求参数
 *
 * @author lee
 * @version 1.0.0
 **/
@Data
@Schema(name="科目余额表 - 分页列表查询 - 请求参数")
public class AccSumListPageQuery extends BasePageDO implements Serializable {
    private static final long serialVersionUID = -5581937958011525412L;

    @Schema(name = "科目编码")
    private String accId;

    @Schema(name = "科目名称")
    private String accName;

    @Schema(name = "开始月份")
    private Integer minMonth;

    @Schema(name = "结束月份")
    private Integer maxMonth;

    @Schema(name = "年份")
    private Integer year;

    @Schema(name = "等于，不等于，小于，小于等于，大于等于，大于，包含，不包含")
    private String status;

    @Schema(name = "初期余额-借方")
    private BigDecimal qcmd;

    @Schema(name = "初期余额-贷方")
    private BigDecimal qcmc;

    @Schema(name = "本期余额-借方")
    private BigDecimal bqmd;

    @Schema(name = "本期余额-贷方")
    private BigDecimal bqmc;

    @Schema(name = "末期余额-借方")
    private BigDecimal qmmd;

    @Schema(name = "末期余额-贷方")
    private BigDecimal qmmc;

    private Integer bookYear;

}
