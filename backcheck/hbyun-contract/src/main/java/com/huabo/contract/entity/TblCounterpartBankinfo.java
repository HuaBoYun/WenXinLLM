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
import lombok.Data;
import lombok.EqualsAndHashCode;


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
@TableName("TBL_COUNTERPART_BANKINFO")
@Schema(name="TblCounterpartBankinfo对象")
public class TblCounterpartBankinfo implements Serializable {
//收款银行账号
    //合同管理-财务管理-收款管理-新增-付款银行账号
//合同管理-财务管理-付款管理-新增-收款银行账号
    //相对方预警-相对方名称-银行账户
    private static final long serialVersionUID = 1L;

      @TableId(value = "BANKID",type = IdType.INPUT)
      private BigDecimal bankid;

    @TableField("BANKACCOUNT")
    private String bankaccount;//银行账号

    @TableField("BANKACCNAME")
    private String bankaccname;//账户

    @TableField("BANKKHYH")
    private String bankkhyh;//开户银行

    @TableField("BANKTYPE")
    private String banktype;//银行类别

    @TableField("BANKNATURE")
    private BigDecimal banknature;//账户性质(0=公司，1=个人)

    @TableField("BANKSTATUS")
    private BigDecimal bankstatus;//账户类型 0弃用  1启用

    @TableField("CREATESTAFF")
    private BigDecimal createstaff;//创建人

    @TableField("CREATETIME")
    @JSONField(format = "yyyy-MM-dd")
    private Date createtime;//创建时间

    @TableField("OUTSIDEID")
    private String outsideid;//外部来源主键

    @TableField("BUDGETID")
    private BigDecimal budgetid;//所属相对方

}
