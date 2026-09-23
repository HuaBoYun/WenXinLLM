package com.huabo.contract.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-03-14
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_ORG_BANKACCOUNT")
@Schema(name="TblOrgBankaccount对象")
public class TblOrgBankaccount implements Serializable {
    //合同管理-财务管理-收款管理-新增-收款银行账号
    //合同管理-财务管理-付款管理-新增-付款银行账号
    private static final long serialVersionUID = 1L;

      @Schema(name = "主键Id 自增")
        @TableId(value = "BANKID" , type = IdType.INPUT)
      private BigDecimal bankid;

      @Schema(name = "账号")
      @TableField("BANKACCNUM")
    private String bankaccnum;

      @Schema(name = "户名")
      @TableField("BANKACCNAME")
    private String bankaccname;

      @Schema(name = "账户编码")
      @TableField("BANKCODE")
    private String bankcode;

      @Schema(name = "账户名称")
      @TableField("BANKNAME")
    private String bankname;

      @Schema(name = "开户银行")
      @TableField("BANKKHYH")
    private String bankkhyh;

      @Schema(name = "银行类别")
      @TableField("BANKYHLB")
    private String bankyhlb;

      @Schema(name = "账户状态(0=正常，1=冻结，2=部分冻结，3=销户)")
      @TableField("BANKSTATE")
    private BigDecimal bankstate;

      @Schema(name = "启用状态(1=启用, 0=非启用)")
      @TableField("BANKSTATUS")
    private BigDecimal bankstatus;

      @Schema(name = "所属组织")
      @TableField("ORGID")
    private BigDecimal orgid;

      @Schema(name = "创建时间")
      @TableField("CREATEDATE")
      @JSONField(format = "yyyy-MM-dd")
      @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createdate;

      @Schema(name = "外部银行账户主键ID")
      @TableField("OUTSIDEID")
    private String outsideid;


}
