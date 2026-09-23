package com.huabo.finance.entity.caiji;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 凭证明细
 * </p>
 *
 * @author L
 * @since 2025-03-26
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("GL_DETAIL")
@Schema(name="GlDetail对象", description="凭证明细")
public class GlDetail implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "分录主键")
        @TableId("PK_DETAIL")
      private String pkDetail;

      @Schema(name = "核算账簿")
      @TableField("PK_ACCOUNTINGBOOK")
    private String pkAccountingbook;

      @Schema(name = "财务组织")
      @TableField("PK_ORG")
    private String pkOrg;

      @Schema(name = "财务组织版本")
      @TableField("PK_ORG_V")
    private String pkOrgV;

      @Schema(name = "业务单元")
      @TableField("PK_UNIT")
    private String pkUnit;

      @Schema(name = "凭证主键")
      @TableField("PK_VOUCHER")
    private String pkVoucher;

      @Schema(name = "账簿类型")
      @TableField("PK_SETOFBOOK")
    private String pkSetofbook;

      @Schema(name = "业务单元名称")
      @TableField("UNITNAME")
    private String unitname;

      @Schema(name = "会计科目")
      @TableField("PK_ACCASOA")
    private String pkAccasoa;

      @Schema(name = "币种")
      @TableField("PK_CURRTYPE")
    private String pkCurrtype;

      @Schema(name = "分录号")
      @TableField("DETAILINDEX")
    private String detailindex;

      @Schema(name = "辅助核算")
      @TableField("ASSID")
    private String assid;

      @Schema(name = "摘要内容")
      @TableField("EXPLANATION")
    private String explanation;

      @Schema(name = "单价")
      @TableField("PRICE")
    private BigDecimal price;

      @Schema(name = "汇率1")
      @TableField("EXCRATE1")
    private BigDecimal excrate1;

      @Schema(name = "账簿汇率类型")
      @TableField("PK_EXRATETYPE2")
    private String pkExratetype2;

      @Schema(name = "汇率2")
      @TableField("EXCRATE2")
    private BigDecimal excrate2;

      @Schema(name = "账簿汇率来源日期")
      @TableField("EXRATEDATE2")
    private Date exratedate2;

      @Schema(name = "集团汇率")
      @TableField("EXCRATE3")
    private BigDecimal excrate3;

      @Schema(name = "全局汇率")
      @TableField("EXCRATE4")
    private BigDecimal excrate4;

      @Schema(name = "借方数量")
      @TableField("DEBITQUANTITY")
    private BigDecimal debitquantity;

      @Schema(name = "原币借发生额")
      @TableField("DEBITAMOUNT")
    private BigDecimal debitamount;

      @Schema(name = "辅币借发生额")
      @TableField("FRACDEBITAMOUNT")
    private BigDecimal fracdebitamount;

      @Schema(name = "账簿本币借发生额")
      @TableField("LOCALDEBITAMOUNT")
    private BigDecimal localdebitamount;

      @Schema(name = "集团本币借发生额")
      @TableField("GROUPDEBITAMOUNT")
    private BigDecimal groupdebitamount;

      @Schema(name = "全局本币借发生额")
      @TableField("GLOBALDEBITAMOUNT")
    private BigDecimal globaldebitamount;

      @Schema(name = "贷方数量")
      @TableField("CREDITQUANTITY")
    private BigDecimal creditquantity;

      @Schema(name = "原币贷发生额")
      @TableField("CREDITAMOUNT")
    private BigDecimal creditamount;

      @Schema(name = "辅币贷发生额")
      @TableField("FRACCREDITAMOUNT")
    private BigDecimal fraccreditamount;

      @Schema(name = "账簿本币贷发生额")
      @TableField("LOCALCREDITAMOUNT")
    private BigDecimal localcreditamount;

      @Schema(name = "集团本币贷发生额")
      @TableField("GROUPCREDITAMOUNT")
    private BigDecimal groupcreditamount;

      @Schema(name = "全局本币贷发生额")
      @TableField("GLOBALCREDITAMOUNT")
    private BigDecimal globalcreditamount;

      @Schema(name = "修改标志")
      @TableField("MODIFYFLAG")
    private String modifyflag;

      @Schema(name = "单据处理类")
      @TableField("RECIEPTCLASS")
    private String recieptclass;

      @Schema(name = "对方科目")
      @TableField("OPPOSITESUBJ")
    private String oppositesubj;

      @Schema(name = "对账标志")
      @TableField("CONTRASTFLAG")
    private Integer contrastflag;

      @Schema(name = "错误信息")
      @TableField("ERRMESSAGE")
    private String errmessage;

      @Schema(name = "标错的历史信息")
      @TableField("ERRMESSAGEH")
    private String errmessageh;

      @Schema(name = "结算方式")
      @TableField("CHECKSTYLE")
    private String checkstyle;

      @Schema(name = "票据编码")
      @TableField("CHECKNO")
    private String checkno;

      @Schema(name = "票据日期")
      @TableField("CHECKDATE")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date checkdate;

      @Schema(name = "核销号")
      @TableField("VERIFYNO")
    private String verifyno;

      @Schema(name = "核销日期")
      @TableField("VERIFYDATE")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date verifydate;

      @Schema(name = "票据类型")
      @TableField("BILLTYPE")
    private String billtype;

      @Schema(name = "银行帐户")
      @TableField("BANKACCOUNT")
    private String bankaccount;

      @Schema(name = "折算来源")
      @TableField("PK_SOURCEPK")
    private String pkSourcepk;

      @Schema(name = "是否折算")
      @TableField("CONVERTFLAG")
    private String convertflag;

      @Schema(name = "发生额方向0=借方;1=贷方;")
      @TableField("DIRECTION")
    private String direction;

      @Schema(name = "凭证类别主键")
      @TableField("PK_VOUCHERTYPEV")
    private String pkVouchertypev;

      @Schema(name = "会计年度")
      @TableField("YEARV")
    private String yearv;

      @Schema(name = "会计期间")
      @TableField("PERIODV")
    private String periodv;

      @Schema(name = "凭证编码")
      @TableField("NOV")
    private Integer nov;

      @Schema(name = "制单日期")
      @TableField("PREPAREDDATEV")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date prepareddatev;

      @Schema(name = "记账人")
      @TableField("PK_MANAGERV")
    private String pkManagerv;

      @Schema(name = "作废标志")
      @TableField("DISCARDFLAGV")
    private String discardflagv;

      @Schema(name = "凭证类型")
      @TableField("VOUCHERKINDV")
    private Integer voucherkindv;

      @Schema(name = "调整期间")
      @TableField("ADJUSTPERIOD")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private String adjustperiod;

      @Schema(name = "签字日期")
      @TableField("SIGNDATEV")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date signdatev;

      @Schema(name = "制单系统")
      @TableField("PK_SYSTEMV")
    private String pkSystemv;

      @Schema(name = "被冲销的分录")
      @TableField("PK_OFFERDETAIL")
    private String pkOfferdetail;

      @Schema(name = "是否差异凭证")
      @TableField("ISDIFFLAG")
    private String isdifflag;

    @TableField("BUSIRECONNO")
    private String busireconno;

      @Schema(name = "网银对账标识码")
      @TableField("NETBANKFLAG")
    private String netbankflag;

      @Schema(name = "所属集团")
      @TableField("PK_GROUP")
    private String pkGroup;

      @Schema(name = "现金流量")
      @TableField("CASHFLOW")
    private String cashflow;

      @Schema(name = "业务单元版本")
      @TableField("PK_UNIT_V")
    private String pkUnitV;

      @Schema(name = "利润中心")
      @TableField("PK_LIABILITYCENTER")
    private String pkLiabilitycenter;

      @Schema(name = "利润中心版本")
      @TableField("PK_LIABILITYCENTER_V")
    private String pkLiabilitycenterV;

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

      @Schema(name = "预留字段6")
      @TableField("FREE6")
    private String free6;

      @Schema(name = "预留字段7")
      @TableField("FREE7")
    private String free7;

      @Schema(name = "预留字段8")
      @TableField("FREE8")
    private String free8;

      @Schema(name = "预留字段9")
      @TableField("FREE9")
    private String free9;

      @Schema(name = "预留字段10")
      @TableField("FREE10")
    private String free10;

      @Schema(name = "预留字段11")
      @TableField("FREE11")
    private String free11;

      @Schema(name = "预留字段12")
      @TableField("FREE12")
    private String free12;

      @Schema(name = "预留字段13")
      @TableField("FREE13")
    private String free13;

      @Schema(name = "预留字段14")
      @TableField("FREE14")
    private String free14;

      @Schema(name = "预留字段15")
      @TableField("FREE15")
    private String free15;

      @Schema(name = "预留字段16")
      @TableField("FREE16")
    private String free16;

      @Schema(name = "预留字段17")
      @TableField("FREE17")
    private String free17;

      @Schema(name = "预留字段18")
      @TableField("FREE18")
    private String free18;

      @Schema(name = "预留字段19")
      @TableField("FREE19")
    private String free19;

      @Schema(name = "预留字段20")
      @TableField("FREE20")
    private String free20;

      @Schema(name = "预留字段21")
      @TableField("FREE21")
    private String free21;

      @Schema(name = "预留字段22")
      @TableField("FREE22")
    private String free22;

      @Schema(name = "预留字段23")
      @TableField("FREE23")
    private String free23;

      @Schema(name = "预留字段24")
      @TableField("FREE24")
    private String free24;

      @Schema(name = "预留字段25")
      @TableField("FREE25")
    private String free25;

      @Schema(name = "预留字段26")
      @TableField("FREE26")
    private String free26;

      @Schema(name = "预留字段27")
      @TableField("FREE27")
    private String free27;

      @Schema(name = "预留字段28")
      @TableField("FREE28")
    private String free28;

      @Schema(name = "预留字段29")
      @TableField("FREE29")
    private String free29;

      @Schema(name = "预留字段30")
      @TableField("FREE30")
    private String free30;

      @Schema(name = "所属采集方案")
      @TableField("FPLANID")
    private String fplanid;

      @Schema(name = "数据来源 -2系统同步 ")
      @TableField("DATAORIGINFLAG")
    private String dataoriginflag;

      @Schema(name = "记账人姓名")
      @TableField("MANAGERVNAME")
    private String managervname;


}
