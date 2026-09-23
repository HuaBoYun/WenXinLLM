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
 * 充值密钥表
 * 存储充值密钥信息，用于离线充值场景
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FEE_LICENSE")
@Schema(name="TblFeeLicense对象", description="充值密钥表")
public class TblFeeLicense implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="主键")
    @TableId(value="ID", type = IdType.INPUT)
    private BigDecimal id;

    @Schema(name="密钥HMAC-SHA256摘要")
    @TableField("LICENSE_KEY_HASH")
    private String licenseKeyHash;

    @Schema(name="授权公司ID")
    @TableField("COMPANY_ORG_ID")
    private BigDecimal companyOrgId;

    @Schema(name="充值金额")
    @TableField("RECHARGE_AMOUNT")
    private BigDecimal rechargeAmount;

    @Schema(name="使用状态 0=未使用 1=已使用")
    @TableField("USE_STATUS")
    private Integer useStatus;

    @Schema(name="使用时间")
    @TableField("USE_TIME")
    private Date useTime;

    @Schema(name="密钥有效期")
    @TableField("EXPIRE_TIME")
    private Date expireTime;

    @Schema(name="生成时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @Schema(name="操作人")
    @TableField("OPERATOR")
    private String operator;

    @Schema(name="备注")
    @TableField("REMARK")
    private String remark;
}
