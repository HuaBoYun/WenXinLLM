package com.huabo.finance.vr;

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
public class GlDetailVr implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "分录主键")
      private String pkDetail;

      @Schema(name = "核算账簿")
    private String pkAccountingbook;

      @Schema(name = "财务组织")
    private String pkOrg;

      @Schema(name = "财务组织版本")
    private String pkOrgV;

      @Schema(name = "业务单元")
    private String pkUnit;

      @Schema(name = "凭证主键")
    private String pkVoucher;

      @Schema(name = "账簿类型")
    private String pkSetofbook;

      @Schema(name = "业务单元名称")
    private String unitname;

      @Schema(name = "会计科目")
    private String pkAccasoa;
      
      @Schema(name = "会计科目名称")
      private String accasoaName;

      @Schema(name = "币种")
    private String pkCurrtype;

      @Schema(name = "分录号")
    private String detailindex;

      @Schema(name = "辅助核算")
    private String assid;

      @Schema(name = "摘要内容")
    private String explanation;

      @Schema(name = "单价")
    private BigDecimal price;

      @Schema(name = "汇率1")
    private BigDecimal excrate1;

      @Schema(name = "账簿汇率类型")
    private String pkExratetype2;

      @Schema(name = "汇率2")
    private BigDecimal excrate2;

      @Schema(name = "账簿汇率来源日期")
    private Date exratedate2;

      @Schema(name = "集团汇率")
      @TableField("EXCRATE3")
    private BigDecimal excrate3;

      @Schema(name = "全局汇率")
    private BigDecimal excrate4;

      @Schema(name = "借方数量")
    private BigDecimal debitquantity;

      @Schema(name = "原币借发生额")
    private BigDecimal debitamount;

      @Schema(name = "辅币借发生额")
    private BigDecimal fracdebitamount;

      @Schema(name = "账簿本币借发生额")
    private BigDecimal localdebitamount;

      @Schema(name = "集团本币借发生额")
    private BigDecimal groupdebitamount;

      @Schema(name = "全局本币借发生额")
    private BigDecimal globaldebitamount;

      @Schema(name = "贷方数量")
    private BigDecimal creditquantity;

      @Schema(name = "原币贷发生额")
    private BigDecimal creditamount;

      @Schema(name = "辅币贷发生额")
    private BigDecimal fraccreditamount;

      @Schema(name = "账簿本币贷发生额")
    private BigDecimal localcreditamount;

      @Schema(name = "集团本币贷发生额")
    private BigDecimal groupcreditamount;

      @Schema(name = "全局本币贷发生额")
    private BigDecimal globalcreditamount;

      @Schema(name = "修改标志")
    private String modifyflag;

      @Schema(name = "单据处理类")
    private String recieptclass;

      @Schema(name = "对方科目")
    private String oppositesubj;
      
      @Schema(name = "对方科目名称")
      private String oppositesubjName;

      @Schema(name = "对账标志")
    private Integer contrastflag;

      @Schema(name = "错误信息")
    private String errmessage;

      @Schema(name = "标错的历史信息")
    private String errmessageh;

      @Schema(name = "结算方式")
    private String checkstyle;

      @Schema(name = "票据编码")
    private String checkno;

      @Schema(name = "票据日期")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date checkdate;

      @Schema(name = "核销号")
    private String verifyno;

      @Schema(name = "核销日期")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date verifydate;

      @Schema(name = "票据类型")
    private String billtype;

      @Schema(name = "银行帐户")
    private String bankaccount;

      @Schema(name = "折算来源")
    private String pkSourcepk;

      @Schema(name = "是否折算")
    private String convertflag;

      @Schema(name = "发生额方向")
    private String direction;

      @Schema(name = "凭证类别主键")
    private String pkVouchertypev;

      @Schema(name = "会计年度")
    private String yearv;

      @Schema(name = "会计期间")
    private String periodv;

      @Schema(name = "凭证编码")
    private Integer nov;

      @Schema(name = "制单日期")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date prepareddatev;

      @Schema(name = "记账人")
    private String pkManagerv;

      @Schema(name = "作废标志")
    private String discardflagv;

      @Schema(name = "凭证类型")
    private Integer voucherkindv;

      @Schema(name = "调整期间")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private String adjustperiod;

      @Schema(name = "签字日期")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date signdatev;

      @Schema(name = "制单系统")
    private String pkSystemv;

      @Schema(name = "被冲销的分录")
    private String pkOfferdetail;

      @Schema(name = "是否差异凭证")
    private String isdifflag;

    private String busireconno;

      @Schema(name = "网银对账标识码")
    private String netbankflag;

      @Schema(name = "所属集团")
    private String pkGroup;

      @Schema(name = "现金流量")
    private String cashflow;

      @Schema(name = "业务单元版本")
    private String pkUnitV;

      @Schema(name = "利润中心")
    private String pkLiabilitycenter;

      @Schema(name = "利润中心版本")
    private String pkLiabilitycenterV;

      @Schema(name = "预留字段1")
    private String free1;

      @Schema(name = "预留字段2")
    private String free2;

      @Schema(name = "预留字段3")
    private String free3;

      @Schema(name = "预留字段4")
    private String free4;

      @Schema(name = "预留字段5")
    private String free5;

      @Schema(name = "预留字段6")
    private String free6;

      @Schema(name = "预留字段7")
    private String free7;

      @Schema(name = "预留字段8")
    private String free8;

      @Schema(name = "预留字段9")
    private String free9;

      @Schema(name = "预留字段10")
    private String free10;

      @Schema(name = "预留字段11")
    private String free11;

      @Schema(name = "预留字段12")
    private String free12;

      @Schema(name = "预留字段13")
    private String free13;

      @Schema(name = "预留字段14")
    private String free14;

      @Schema(name = "预留字段15")
    private String free15;

      @Schema(name = "预留字段16")
    private String free16;

      @Schema(name = "预留字段17")
    private String free17;

      @Schema(name = "预留字段18")
    private String free18;

      @Schema(name = "预留字段19")
    private String free19;

      @Schema(name = "预留字段20")
    private String free20;

      @Schema(name = "预留字段21")
    private String free21;

      @Schema(name = "预留字段22")
    private String free22;

      @Schema(name = "预留字段23")
    private String free23;

      @Schema(name = "预留字段24")
    private String free24;

      @Schema(name = "预留字段25")
    private String free25;

      @Schema(name = "预留字段26")
    private String free26;

      @Schema(name = "预留字段27")
    private String free27;

      @Schema(name = "预留字段28")
    private String free28;

      @Schema(name = "预留字段29")
    private String free29;

      @Schema(name = "预留字段30")
    private String free30;

      @Schema(name = "所属采集方案")
    private String fplanid;

      @Schema(name = "数据来源 -2系统同步 ")
    private String dataoriginflag;

      @Schema(name = "记账人姓名")
    private String managervname;

      @Schema(name = "余额表信息")
    private GlBalanceVr glBalanceVr;
      
      @Schema(name = "凭证号")
    private Integer gvnum;
      
      @Schema(name = "制单日期")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date gvprepareddate;
}
