package com.huabo.financialdata.entity.vo.detailedBook;


import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 明细账 - 分页列表 - 返回参数
 *
 * @author Mr.xiang
 * @since 2022-10-18
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="明细账 - 分页列表 - 返回参数")
public class DetailedBookResponseVo {

    @Schema(name = "科目编码")
    private String accid;

    @Schema(name = "凭证日期")
    private String pzDate;

    @Schema(name = "抬头文本")
    private String lineText;
    
    @Schema(name = "借方金额")
    private BigDecimal md;

    @Schema(name = "贷方金额")
    private BigDecimal mc;

    @Schema(name = "科目名称")
    private String accName;

    @Schema(name = "借贷方向    d-借方    c-贷方")
    private String dc;

    @Schema(name = "凭证号")
    private String pzh;

    @Schema(name = "期初借方")
    private BigDecimal qcmd;

    @Schema(name = "期初贷方")
    private BigDecimal qcmc;

    @Schema(name = "期末方向 d-借方    c-贷方")
    private String qmdc;

    @Schema(name = "本期借方")
    private BigDecimal bqmd;

    @Schema(name = "本期贷方")
    private BigDecimal bqmc;

    @Schema(name = "累计借方")
    private BigDecimal ljmd;

    @Schema(name = "累积贷方")
    private BigDecimal ljmc;

    @Schema(name = "期末贷方")
    private BigDecimal qmmc;

    @Schema(name = "期初借方")
    private BigDecimal qmmd;

    @Schema(name = "期初方向 d-借方    c-贷方")
    private String qcdc;

    @Schema(name = "分录序号")
    private String entryId;

    @Schema(name = "会计期间月份")
    private Integer amonth;
    
    @Schema(name = "本期余额")
    private BigDecimal balance;//余额
    
    @Schema(name = "本期借贷方向")
    private String bqqmdc;
    
    @Schema(name = "本期期初余额")
    private BigDecimal qcye;//余额
    
    @Schema(name = "本期期初借贷方向")
    private String bqqcdc;
    
    private String glh;
}
