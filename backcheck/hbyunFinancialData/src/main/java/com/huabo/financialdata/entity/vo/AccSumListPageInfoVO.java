package com.huabo.financialdata.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 科目余额表 - 分页列表 - 返回参数
 *
 * @author lee
 * @version 1.0.0
 **/
@Data
@Schema(name="科目余额表 - 分页列表查询 - 返回参数")
public class AccSumListPageInfoVO implements Serializable {
    private static final long serialVersionUID = 4193499476539790852L;

    @Schema(name = "科目编码")
    private String accId;

    @Schema(name = "科目名称 - 额外扩展")
    private String accName;

    @Schema(name = "期初借方")
    private BigDecimal QCMD;

    @Schema(name = "期初贷方")
    private BigDecimal QCMC;

    @Schema(name = "本期借方")
    private BigDecimal BQMD;

    @Schema(name = "本期贷方")
    private BigDecimal BQMC;

    @Schema(name = "期末借方")
    private BigDecimal QMMD;

    @Schema(name = "期末贷方")
    private BigDecimal QMMC;

    @Schema(name = "外币名称")
    private String FNAME;

    @Schema(name = "外币 - 期初借方")
    private BigDecimal QCMD_F;

    @Schema(name = "外币 - 期初贷方")
    private BigDecimal QCMC_F;

    @Schema(name = "外币 - 本期借方")
    private BigDecimal BQMD_F;

    @Schema(name = "外币 - 本期贷方")
    private BigDecimal BQMC_F;

    @Schema(name = "外币 - 期末借方")
    private BigDecimal QMMD_F;

    @Schema(name = "外币 - 期末贷方")
    private BigDecimal QMMC_F;

    @Schema(name = "凭证数量")
    private Integer NUM;

    @Schema(name = "会计年度")
    private Integer AYEAR;

    @Schema(name = "会计期间")
    private Integer AMONTH;

    @Schema(name = "累计 - 借方")
    private BigDecimal LJMD;

    @Schema(name = "累计 - 贷方")
    private BigDecimal LJMC;

    @Schema(name = "外币 - 累计借方")
    private BigDecimal LJMD_F;

    @Schema(name = "外币 - 累计贷方")
    private BigDecimal LJMC_F;

    @Schema(name = "期初方向 D,借 C 贷,0 平")
    private String QCDC;
    @Schema(name = "期初方向名称")
    private String qcdcName;

    @Schema(name = "期末方向 D,借 C 贷,0 平")
    private String QMDC;
    @Schema(name = "期末方向名称")
    private String qmdcName;

}
