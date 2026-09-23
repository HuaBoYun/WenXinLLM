package com.huabo.system.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 公司余额表
 * 存储各公司的账户余额及充值消费汇总信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FEE_BALANCE")
@Schema(name="TblFeeBalance对象", description="公司余额表")
public class TblFeeBalance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="主键")
    @TableId(value="ID", type = IdType.INPUT)
    private BigDecimal id;

    @Schema(name="公司ID")
    @TableField("COMPANY_ORG_ID")
    private BigDecimal companyOrgId;

    @Schema(name="余额（AES加密存储）")
    @TableField("BALANCE")
    private String balance;

    @Schema(name="累计消费")
    @TableField("TOTAL_CONSUMED")
    private BigDecimal totalConsumed;

    @Schema(name="累计充值")
    @TableField("TOTAL_RECHARGED")
    private BigDecimal totalRecharged;

    @Schema(name="初始赠送额度")
    @TableField("INIT_AMOUNT")
    private BigDecimal initAmount;

    @Schema(name="初始化时间")
    @TableField("INIT_TIME")
    private Date initTime;

    @Schema(name="最后充值时间")
    @TableField("LAST_RECHARGE_TIME")
    private Date lastRechargeTime;

    @Schema(name="更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;
}
