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
 * 密钥购买订单表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FEE_LICENSE_ORDER")
@Schema(name = "TblFeeLicenseOrder对象", description = "密钥购买订单表")
public class TblFeeLicenseOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键")
    @TableId(value = "ID", type = IdType.INPUT)
    private BigDecimal id;

    @Schema(name = "订单编号")
    @TableField("ORDER_NO")
    private String orderNo;

    @Schema(name = "购买公司ID")
    @TableField("COMPANY_ORG_ID")
    private BigDecimal companyOrgId;

    @Schema(name = "公司名称")
    @TableField("COMPANY_NAME")
    private String companyName;

    @Schema(name = "购买金额")
    @TableField("PURCHASE_AMOUNT")
    private BigDecimal purchaseAmount;

    @Schema(name = "状态 0=待审批 1=已审批 2=已生成密钥 3=已驳回")
    @TableField("STATUS")
    private Integer status;

    @Schema(name = "生成的充值密钥")
    @TableField("LICENSE_KEY")
    private String licenseKey;

    @Schema(name = "申请人ID")
    @TableField("APPLICANT_ID")
    private String applicantId;

    @Schema(name = "申请人姓名")
    @TableField("APPLICANT_NAME")
    private String applicantName;

    @Schema(name = "审批人ID")
    @TableField("APPROVER_ID")
    private String approverId;

    @Schema(name = "审批人姓名")
    @TableField("APPROVER_NAME")
    private String approverName;

    @Schema(name = "审批时间")
    @TableField("APPROVE_TIME")
    private Date approveTime;

    @Schema(name = "审批备注")
    @TableField("APPROVE_REMARK")
    private String approveRemark;

    @Schema(name = "申请时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @Schema(name = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;
}
