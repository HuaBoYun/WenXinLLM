package com.huabo.financialdata.entity.vo.accBseg;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 明细分类账 - 查询展示详情对象 vo
 *
 * @author lee
 * @version 1.0.0
 **/
@Data
public class DetailClassInfoVO implements Serializable {
    private static final long serialVersionUID = 1932509931090247427L;

    @Schema(name = "凭证日期")
    private String pzDate;

    @Schema(name = "科目名称 - 全名称")
    private String accAllName;

    @Schema(name = "行项目文本")
    private String lineText;

    @Schema(name = "借方")
    private BigDecimal md;

    @Schema(name = "贷方")
    private BigDecimal mc;

    @Schema(name = "账目月份å")
    private Integer pzDateMonth;

    @Schema(name = "科目方向")
    private String dc;

    @Schema(name = "凭证号")
    private String pzh;


    @Schema(name = "期初借方")
    private BigDecimal qcmd;

    @Schema(name = "期初贷方")
    private BigDecimal qcmc;

    @Schema(name = "期初方向 D,借 C 贷,0 平")
    private String qcdc;

    @Schema(name = "期初方向名称")
    private String qcdcName;

    @Schema(name = "期末方向 D,借 C 贷,0 平")
    private String qmdc;

    @Schema(name = "本期借方")
    private BigDecimal bqmd;

    @Schema(name = "本期贷方")
    private BigDecimal bqmc;

    @Schema(name = "累计 - 借方")
    private BigDecimal ljmd;

    @Schema(name = "累计 - 贷方")
    private BigDecimal ljmc;

    @Schema(name = "期末贷方")
    private BigDecimal qmmc;

    @Schema(name = "期末借方")
    private BigDecimal qmmd;

    @Schema(name = "分录序号")
    private BigDecimal entryId;
}
