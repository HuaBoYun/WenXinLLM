package com.huabo.finance.entity.caiji;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 会计科目基本信息
 * </p>
 *
 * @author L
 * @since 2025-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("BD_ACCOUNT")
@Schema(name="BdAccount对象", description="会计科目基本信息")
public class BdAccount implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "科目主键")
      @TableField("PK_ACCOUNT")
    private String pkAccount;

      @Schema(name = "创建科目表主键")
      @TableField("PK_ACCCHART")
    private String pkAccchart;

      @Schema(name = "科目编码")
      @TableField("CODE")
    private String code;

      @Schema(name = "科目名称")
      @TableField("NAME")
    private String name;

      @Schema(name = "上级科目")
      @TableField("PID")
    private String pid;

      @Schema(name = "内部码")
      @TableField("INNERCODE")
    private String innercode;

      @Schema(name = "原始科目主键")
      @TableField("PK_ORIGINALACCOUNT")
    private String pkOriginalaccount;

      @Schema(name = "科目类型")
      @TableField("PK_ACCTYPE")
    private String pkAcctype;

      @Schema(name = "现金分类  	0=其它;1=现金科目;2=银行科目;3=现金等价物;")
      @TableField("CASHTYPE")
    private Integer cashtype;

      @Schema(name = "助记码")
      @TableField("REMCODE")
    private String remcode;

      @Schema(name = "科目方向 0=借方;1=贷方;")
      @TableField("BALANORIENT")
    private Integer balanorient;

      @Schema(name = "默认币种")
      @TableField("CURRENCY")
    private String currency;

      @Schema(name = "汇总打印级次 0=末级;1=1;2=2;3=3;4=4;5=5;6=6;7=7;")
      @TableField("SUMPRINT_LEVEL")
    private Integer sumprintLevel;

      @Schema(name = "发生额方向控制")
      @TableField("INCURFLAG")
    private String incurflag;

      @Schema(name = "余额方向控制")
      @TableField("BALANFLAG")
    private String balanflag;

      @Schema(name = "账簿余额双向性")
      @TableField("BOTHORIENT")
    private String bothorient;

      @Schema(name = "内部交易科目")
      @TableField("INNERACC")
    private String inneracc;

      @Schema(name = "默认计量单位")
      @TableField("UNIT")
    private String unit;

      @Schema(name = "表外科目")
      @TableField("OUTFLAG")
    private String outflag;

      @Schema(name = "科目级次")
      @TableField("ACCLEV")
    private Integer acclev;

      @Schema(name = "结算方式")
      @TableField("BALANCETYPE")
    private String balancetype;

      @Schema(name = "银行账号")
      @TableField("BANKACC")
    private String bankacc;

      @Schema(name = "票据日期")
      @TableField("BILLDATE")
    private String billdate;

      @Schema(name = "票据号")
      @TableField("BILLNUMBER")
    private String billnumber;

      @Schema(name = "票据类型")
      @TableField("BILLTYPE")
    private String billtype;

      @Schema(name = "内部交易信息")
      @TableField("INNERINFO")
    private String innerinfo;

      @Schema(name = "数量")
      @TableField("QUANTITY")
    private String quantity;

      @Schema(name = "单价")
      @TableField("PRICE")
    private String price;

      @Schema(name = "合并报表科目")
      @TableField("COMBINEFORM")
    private String combineform;

      @Schema(name = "启用状态 1=未启用;2=已启用;3=已停用;")
      @TableField("ENABLESTATE")
    private Integer enablestate;

      @Schema(name = "关联信息主键")
      @TableField("PK_ACCASOA")
    private String pkAccasoa;

      @Schema(name = "末级标志  1-是 0-否")
      @TableField("ENDFLAG")
    private String endflag;

      @Schema(name = "受控模块项")
      @TableField("CTRLMODULES")
    private String ctrlmodules;

      @Schema(name = "当前科目表")
      @TableField("PK_CURRENTCHART")
    private String pkCurrentchart;

      @Schema(name = "科目显示名称1")
      @TableField("DISPNAME")
    private String dispname;

      @Schema(name = "科目显示名称2")
      @TableField("DISPNAME2")
    private String dispname2;

      @Schema(name = "科目显示名称3")
      @TableField("DISPNAME3")
    private String dispname3;

      @Schema(name = "科目显示名称4")
      @TableField("DISPNAME4")
    private String dispname4;

      @Schema(name = "科目显示名称5")
      @TableField("DISPNAME5")
    private String dispname5;

      @Schema(name = "科目显示名称6")
      @TableField("DISPNAME6")
    private String dispname6;

    @TableField("ACCASS")
    private String accass;

      @Schema(name = "数据来源 0=本级产生; 1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;-2=数据采集")
      @TableField("DATAORIGINFLAG")
    private Integer dataoriginflag;

      @Schema(name = "辅助核算")
      @TableField("ACCASSNAME")
    private String accassname;

      @Schema(name = "提前关账")
      @TableField("ALLOWCLOSE")
    private String allowclose;

      @Schema(name = "受控模块")
      @TableField("CTRLMODULENAME")
    private String ctrlmodulename;

      @Schema(name = "使用说明")
      @TableField("USEDESC")
    private String usedesc;

      @Schema(name = "科目属性 0=财务会计科目;1=政府预算会计科目;")
      @TableField("ACCPROPERTY")
    private String accproperty;

      @Schema(name = "平行记账标志")
      @TableField("PARALLELACCOUNTS")
    private String parallelaccounts;

      @Schema(name = "平行记账例外科目")
      @TableField("NPARALLELACCOUNTS")
    private String nparallelaccounts;

      @Schema(name = "财管属性 0=公共;1=财务;2=管理;")
      @TableField("FIMGRPROP")
    private String fimgrprop;

      @Schema(name = "自定义项1")
      @TableField("DEF1")
    private String def1;

      @Schema(name = "自定义项2")
      @TableField("DEF2")
    private String def2;

      @Schema(name = "自定义项3")
      @TableField("DEF3")
    private String def3;

      @Schema(name = "自定义项4")
      @TableField("DEF4")
    private String def4;

      @Schema(name = "自定义项5")
      @TableField("DEF5")
    private String def5;

      @Schema(name = "创建人")
      @TableField("CREATOR")
    private BigDecimal creator;

      @Schema(name = "创建时间")
      @TableField("CREATIONTIME")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date creationtime;

      @Schema(name = "最后修改时间")
      @TableField("MODIFIEDTIME")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedtime;

      @Schema(name = "最后修改人")
      @TableField("MODIFIER")
    private BigDecimal modifier;

      @Schema(name = "所属采集方案")
      @TableField("FPLANID")
    private String fplanid;
      
      @Schema(name = "创建组织")
      @TableField("PK_ORG")
    private String pkorg;
      
      @Schema(name = "创建集团")
      @TableField("PK_GROUP")
    private String pkgroup;


}
