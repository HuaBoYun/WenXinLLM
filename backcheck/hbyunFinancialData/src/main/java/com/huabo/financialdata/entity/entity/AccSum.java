package com.huabo.financialdata.entity.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 科目余额实体
 *
 * @author lee
 * @version 1.0.0
 **/
@Data
@TableName("TBL_ACCOUNT")
@Schema(name=" 科目余额表 TBL_ACCOUNT 实体类")
public class AccSum {

    /**
     * 科目编码
     */
    @TableId("ACCID")
    @Schema(name = "科目编码")
    private String ACCID;

    /**
     * 科目名称 - 额外扩展
     */
    @Schema(name = "科目名称 - 额外扩展")
    private String accName;

    /**
     * 期初借方
     */
    @Schema(name = "期初借方")
    private BigDecimal QCMD;

    /**
     * 期初贷方
     */
    @Schema(name = "期初贷方")
    private BigDecimal QCMC;

    /**
     * 本期借方
     */
    @Schema(name = "本期借方")
    private BigDecimal BQMD;

    /**
     * 本期贷方
     */
    @Schema(name = "本期贷方")
    private BigDecimal BQMC;

    /**
     * 期末借方
     */
    @Schema(name = "期末借方")
    private BigDecimal QMMD;

    /**
     * 期末贷方
     */
    @Schema(name = "期末贷方")
    private BigDecimal QMMC;

    /**
     * 外币名称
     */
    @Schema(name = "外币名称")
    private String FNAME;

    /**
     * 外币期初借
     */
    @Schema(name = "外币期初借")
    private BigDecimal QCMD_F;

    /**
     * 外币期初贷方
     */
    @Schema(name = "外币期初贷方")
    private BigDecimal QCMC_F;

    /**
     * 外币本期借方
     */
    @Schema(name = "外币本期借方")
    private BigDecimal BQMD_F;

    /**
     * 外币本期贷方
     */
    @Schema(name = "外币本期贷方")
    private BigDecimal BQMC_F;

    /**
     * 外币期末借方
     */
    @Schema(name = "外币期末借方")
    private BigDecimal QMMD_F;

    /**
     * 外币期末贷方
     */
    @Schema(name = "外币期末贷方")
    private BigDecimal QMMC_F;

    /**
     * 凭证数量
     */
    @Schema(name = "凭证数量")
    private Integer NUM;

    /**
     * 会计年度
     */
    @Schema(name = "会计年度")
    private Integer AYEAR;

    /**
     * 会计期间
     */
    @Schema(name = "会计期间")
    private Integer AMONTH;

    /**
     * 累计借方
     */
    @Schema(name = "累计借方")
    private BigDecimal LJMD;

    /**
     * 累计贷方
     */
    @Schema(name = "累计贷方")
    private BigDecimal LJMC;

    /**
     * 外币累计借方
     */
    @Schema(name = "外币累计借方")
    private BigDecimal LJMD_F;

    /**
     * 外币累计贷方
     */
    @Schema(name = "外币累计贷方")
    private BigDecimal LJMC_F;

    /**
     * 期初方向 D,借 C 贷,0 平
     */
    @Schema(name = "期初方向 D,借 C 贷,0 平")
    private String QCDC;

    /**
     * 期末方向 D,借 C 贷,0 平
     */
    @Schema(name = "期末方向 D,借 C 贷,0 平")
    private String QMDC;


}
