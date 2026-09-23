package com.huabo.finance.entity.caiji;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 凭证余额
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("GL_BALANCE")
@Schema(name="GlBalance对象", description="凭证余额")
public class GlBalance implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "余额主键")
      @TableField("PK_BALANCE")
    private String pkBalance;

      @Schema(name = "集团")
      @TableField("PK_GROUP")
    private String pkGroup;

      @Schema(name = "所属组织")
      @TableField("PK_ORG")
    private String pkOrg;

      @Schema(name = "财务组织版本")
      @TableField("PK_ORG_V")
    private String pkOrgV;

      @Schema(name = "业务单元")
      @TableField("PK_UNIT")
    private String pkUnit;

      @Schema(name = "帐簿")
      @TableField("PK_ACCOUNTINGBOOK")
    private String pkAccountingbook;

      @Schema(name = "核算账簿")
      @TableField("PK_SETOFBOOK")
    private String pkSetofbook;

      @Schema(name = "科目主键")
      @TableField("PK_ACCASOA")
    private String pkAccasoa;

      @Schema(name = "币种主键")
      @TableField("PK_CURRTYPE")
    private String pkCurrtype;

      @Schema(name = "会计年度")
      @TableField("YEAR")
    private String year;

      @Schema(name = "会计期间")
      @TableField("PERIOD")
    private String period;

      @Schema(name = "辅助核算标识")
      @TableField("ASSID")
    private String assid;

      @Schema(name = "借方数量")
      @TableField("DEBITQUANTITY")
    private BigDecimal debitquantity;

      @Schema(name = "原币借发生额")
      @TableField("DEBITAMOUNT")
    private BigDecimal debitamount;

      @Schema(name = "辅币借发生额")
      @TableField("FRACDEBITAMOUNT")
    private BigDecimal fracdebitamount;

      @Schema(name = "本币借发生额")
      @TableField("LOCALDEBITAMOUNT")
    private BigDecimal localdebitamount;

      @Schema(name = "贷方数量")
      @TableField("CREDITQUANTITY")
    private BigDecimal creditquantity;

      @Schema(name = "原币贷发生额")
      @TableField("CREDITAMOUNT")
    private BigDecimal creditamount;

      @Schema(name = "辅币贷发生额")
      @TableField("FRACCREDITAMOUNT")
    private BigDecimal fraccreditamount;

      @Schema(name = "本币贷发生额")
      @TableField("LOCALCREDITAMOUNT")
    private BigDecimal localcreditamount;

      @Schema(name = "集团本币借发生额")
      @TableField("GROUPDEBITAMOUNT")
    private BigDecimal groupdebitamount;

      @Schema(name = "集团本币贷发生额")
      @TableField("GROUPCREDITAMOUNT")
    private BigDecimal groupcreditamount;

      @Schema(name = "全局本币借发生额")
      @TableField("GLOBALDEBITAMOUNT")
    private BigDecimal globaldebitamount;

      @Schema(name = "全局本币贷发生额")
      @TableField("GLOBALCREDITAMOUNT")
    private BigDecimal globalcreditamount;

      @Schema(name = "调整期间")
      @TableField("ADJUSTPERIOD")
    private String adjustperiod;

      @Schema(name = "账簿类型借方发生额")
      @TableField("SETOFBOOKDEBITAMOUNT")
    private BigDecimal setofbookdebitamount;

      @Schema(name = "账簿类型贷方发生额")
      @TableField("SETOFBOOKCREDITAMOUNR")
    private BigDecimal setofbookcreditamounr;

      @Schema(name = "利润中心")
      @TableField("PK_LIABILITYCENTER")
    private BigDecimal pkLiabilitycenter;

      @Schema(name = "预留字段1")
      @TableField("FREE1")
    private String free1;

      @Schema(name = "预留字段2")
      @TableField("FREE2")
    private String free2;

      @Schema(name = "预留字段3")
      @TableField("FREE3")
    private String free3;

      @Schema(name = "预留字段4")
      @TableField("FREE4")
    private String free4;

      @Schema(name = "预留字段5")
      @TableField("FREE5")
    private String free5;

      @Schema(name = "所属采集方案")
      @TableField("FPLANID")
    private String fplanid;

      @Schema(name = "数据来源 -2系统同步 ")
      @TableField("DATAORIGINFLAG")
    private String dataoriginflag;
      
      
      @Schema(name = "本期期初金额原币")
      @TableField("FBEGINBALANCEFOR")
    private BigDecimal fbeginBalanceFor;
      
      @Schema(name = "本期期初金额本位币")
      @TableField("FBEGINBALANCELOCAL")
    private BigDecimal fbeginBalanceLocal;
      
      @Schema(name = "本期期末金额原币")
      @TableField("FENDBALANCEFOR")
    private BigDecimal fendBalanceFor;
      
      @Schema(name = "本期期末金额本位币")
      @TableField("FENDBALANCELOCAL")
    private BigDecimal fendBalanceLocal;
      
      @Schema(name = "本年累计贷方原币")
      @TableField("FYEARCREDITFOR")
    private BigDecimal fyearCreditFor;
      
      @Schema(name = "本年累计贷方本位币")
      @TableField("FYEARCREDITLOCAL")
    private BigDecimal fyearCreditLocal;
      
      @Schema(name = "本年累计借方原币")
      @TableField("FYEARDEDITFOR")
    private BigDecimal fyearDeditFor;
      
      @Schema(name = "本年累计借方本位币")
      @TableField("FYEARDEDITLOCAL")
    private BigDecimal fyearDeditLocal;
      
      


}
