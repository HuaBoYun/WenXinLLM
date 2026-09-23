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
 * 凭证库表
 * </p>
 *
 * @author L
 * @since 2025-03-19
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("GL_VOUCHER")
@Schema(name="GlVoucher对象", description="凭证库表")
public class GlVoucher implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "凭证主键")
        @TableId("PK_VOUCHER")
      private String pkVoucher;

      @Schema(name = "增加分类")
      @TableField("ADDCLASS")
    private String addclass;

      @Schema(name = "调整期间")
      @TableField("ADJUSTPERIOD")
    private String adjustperiod;

      @Schema(name = "附件")
      @TableField("ATTACHMENT")
    private Integer attachment;

      @Schema(name = "审核日期")
      @TableField("CHECKEDDATE")
    private Date checkeddate;

      @Schema(name = "转换标志")
      @TableField("CONVERTFLAG")
    private String convertflag;

      @Schema(name = "创建时间")
      @TableField("CREATIONTIME")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date creationtime;

      @Schema(name = "创建人")
      @TableField("CREATOR")
    private String creator;

      @Schema(name = "删除分类")
      @TableField("DELETECLASS")
    private String deleteclass;

      @Schema(name = "明细修改标志")
      @TableField("DETAILMODFLAG")
    private String detailmodflag;

      @Schema(name = "作废标志")
      @TableField("DISCARDFLAG")
    private String discardflag;

      @Schema(name = "错误消息")
      @TableField("ERRMESSAGE")
    private String errmessage;

      @Schema(name = "错误消息H")
      @TableField("ERRMESSAGEH")
    private String errmessageh;

      @Schema(name = "说明")
      @TableField("EXPLANATION")
    private String explanation;

      @Schema(name = "自定义项1")
      @TableField("FREE1")
    private String free1;

      @Schema(name = "自定义项10")
      @TableField("FREE10")
    private String free10;

      @Schema(name = "自定义项11")
      @TableField("FREE11")
    private String free11;

      @Schema(name = "自定义项12")
      @TableField("FREE12")
    private String free12;

      @Schema(name = "自定义项13")
      @TableField("FREE13")
    private String free13;

      @Schema(name = "自定义项14")
      @TableField("FREE14")
    private String free14;

      @Schema(name = "自定义项15")
      @TableField("FREE15")
    private String free15;

      @Schema(name = "自定义项16")
      @TableField("FREE16")
    private String free16;

      @Schema(name = "自定义项17")
      @TableField("FREE17")
    private String free17;

      @Schema(name = "自定义项18")
      @TableField("FREE18")
    private String free18;

      @Schema(name = "自定义项19")
      @TableField("FREE19")
    private String free19;

      @Schema(name = "自定义项2")
      @TableField("FREE2")
    private String free2;

      @Schema(name = "自定义项20")
      @TableField("FREE20")
    private String free20;

      @Schema(name = "自定义项3")
      @TableField("FREE3")
    private String free3;

      @Schema(name = "自定义项4")
      @TableField("FREE4")
    private String free4;

      @Schema(name = "自定义项5")
      @TableField("FREE5")
    private String free5;

      @Schema(name = "自定义项6")
      @TableField("FREE6")
    private String free6;

      @Schema(name = "自定义项7")
      @TableField("FREE7")
    private String free7;

      @Schema(name = "自定义项8")
      @TableField("FREE8")
    private String free8;

      @Schema(name = "自定义项9")
      @TableField("FREE9")
    private String free9;

      @Schema(name = "差异标志")
      @TableField("ISDIFFLAG")
    private String isdifflag;

      @Schema(name = "修改时间")
      @TableField("MODIFIEDTIME")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedtime;

      @Schema(name = "修改人")
      @TableField("MODIFIER")
    private String modifier;

      @Schema(name = "修改分类")
      @TableField("MODIFYCLASS")
    private String modifyclass;

      @Schema(name = "修改标志")
      @TableField("MODIFYFLAG")
    private String modifyflag;

      @Schema(name = "凭证号")
      @TableField("NUM")
    private Integer num;

      @Schema(name = "提供凭证")
      @TableField("OFFERVOUCHER")
    private String offervoucher;

      @Schema(name = "会计期间")
      @TableField("PERIOD")
    private String period;

      @Schema(name = "账簿主键")
      @TableField("PK_ACCOUNTINGBOOK")
    private String pkAccountingbook;

      @Schema(name = "出纳主键")
      @TableField("PK_CASHER")
    private String pkCasher;

      @Schema(name = "出纳人员")
      @TableField("CASHERNAME")
    private String cashername;

      @Schema(name = "审核主键")
      @TableField("PK_CHECKED")
    private String pkChecked;

      @Schema(name = "审核人员")
      @TableField("CHECKEDNAME")
    private String checkedname;

      @Schema(name = "集团主键")
      @TableField("PK_GROUP")
    private String pkGroup;

      @Schema(name = "主管主键")
      @TableField("PK_MANAGER")
    private String pkManager;

      @Schema(name = "主管人员")
      @TableField("MANAGERNAME")
    private String managername;

      @Schema(name = "组织主键")
      @TableField("PK_ORG")
    private String pkOrg;

      @Schema(name = "虚拟组织主键")
      @TableField("PK_ORG_V")
    private String pkOrgV;

      @Schema(name = "制单主键")
      @TableField("PK_PREPARED")
    private String pkPrepared;

      @Schema(name = "账套主键")
      @TableField("PK_SETOFBOOK")
    private String pkSetofbook;

      @Schema(name = "来源主键")
      @TableField("PK_SOURCEPK")
    private String pkSourcepk;

      @Schema(name = "系统主键")
      @TableField("PK_SYSTEM")
    private String pkSystem;

      @Schema(name = "凭证类型主键")
      @TableField("PK_VOUCHERTYPE")
    private String pkVouchertype;

      @Schema(name = "预记账标志")
      @TableField("PREACCOUNTFLAG")
    private String preaccountflag;

      @Schema(name = "制单日期")
      @TableField("PREPAREDDATE")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date prepareddate;

      @Schema(name = "签字日期")
      @TableField("SIGNDATE")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date signdate;

      @Schema(name = "签字标志")
      @TableField("SIGNFLAG")
    private String signflag;

      @Schema(name = "过账日期")
      @TableField("TALLYDATE")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date tallydate;

      @Schema(name = "临时保存标志")
      @TableField("TEMPSAVEFLAG")
    private String tempsaveflag;

      @Schema(name = "总贷方金额")
      @TableField("TOTALCREDIT")
    private BigDecimal totalcredit;

      @Schema(name = "总贷方金额（本位币）")
      @TableField("TOTALCREDITGLOBAL")
    private BigDecimal totalcreditglobal;

      @Schema(name = "总贷方金额（集团）")
      @TableField("TOTALCREDITGROUP")
    private BigDecimal totalcreditgroup;

      @Schema(name = "总借方金额")
      @TableField("TOTALDEBIT")
    private BigDecimal totaldebit;

      @Schema(name = "总借方金额（本位币）")
      @TableField("TOTALDEBITGLOBAL")
    private BigDecimal totaldebitglobal;

      @Schema(name = "总借方金额（集团）")
      @TableField("TOTALDEBITGROUP")
    private BigDecimal totaldebitgroup;

      @Schema(name = "凭证种类")
      @TableField("VOUCHERKIND")
    private Integer voucherkind;

      @Schema(name = "年份")
      @TableField("YEAR")
    private String year;

      @Schema(name = "所属采集方案")
      @TableField("FPLANID")
    private String fplanid;

      @Schema(name = "数据来源 -2系统同步 ")
      @TableField("DATAORIGINFLAG")
    private Integer dataoriginflag;

      @Schema(name = "凭证类别名称")
      @TableField(exist = false) 
    private String voucherTypeName;

}
