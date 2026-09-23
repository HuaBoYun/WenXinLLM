package com.huabo.finance.entity.caiji;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * 会计科目
 * </p>
 *
 * @author L
 * @since 2025-03-31
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("BD_ACCASOA")
@Schema(name="BdAccasoa对象", description="会计科目")
public class BdAccasoa implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "科目主键")
        @TableId("PK_ACCASOA")
      private String pkAccasoa;

      @Schema(name = "科目主键")
      @TableField("PK_ACCOUNT")
    private String pkAccount;

      @Schema(name = "所属科目录")
      @TableField("PK_ACCCCHART")
    private String pkAcccchart;

      @Schema(name = "末级标志")
      @TableField("ENDFLAG")
    private String endflag;

      @Schema(name = "受控模块")
      @TableField("CTRLMODULES")
    private String ctrlmodules;

      @Schema(name = "科目显示名称")
      @TableField("DISPNAME")
    private String dispname;

      @Schema(name = "提前关账")
      @TableField("ALLOWCLOSE")
    private String allowclose;

      @Schema(name = "辅助核算")
      @TableField("ACCASS")
    private String accass;

      @Schema(name = "启用状态 1=未启用;2=已启用;3=已停用;")
      @TableField("ENABLESTATE")
    private String enablestate;

      @Schema(name = "数据来源  	0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;")
      @TableField("DATAORIGINFLAG")
    private Integer dataoriginflag;

      @Schema(name = "科目名称")
      @TableField("NAME")
    private String name;

      @Schema(name = "发生额方向控制")
      @TableField("INCURFLAG")
    private String incurflag;

      @Schema(name = "余额方向控制")
      @TableField("BALANFLAG")
    private String balanflag;

      @Schema(name = "账户余额双向显示")
      @TableField("BOTHORIENT")
    private String bothorient;

      @Schema(name = "默认计量单位")
      @TableField("UNIT")
    private String unit;

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

      @Schema(name = "助记码")
      @TableField("REMCODE")
    private String remcode;

      @Schema(name = "默认币种")
      @TableField("CURRENCY")
    private String currency;

      @Schema(name = "汇总打印级次 0=末级;1=1;2=2;3=3;4=4;5=5;6=6;7=7;")
      @TableField("SUMPRINT_LEVEL")
    private Integer sumprintLevel;

      @Schema(name = "使用说明")
      @TableField("USEDESC")
    private String usedesc;

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
    private String creator;

      @Schema(name = "创建时间")
      @TableField("CREATIONTIME")
    private Date creationtime;

      @Schema(name = "最后修改人")
      @TableField("MODIFIER")
    private String modifier;

      @Schema(name = "最后修改时间")
      @TableField("MODIFIEDTIME")
    private Date modifiedtime;


}
