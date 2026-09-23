package com.financial.sharing.business.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账单表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_CWGX_BILL")
@ApiModel(value = "TblBill", description = "账单表")
public class TblBill implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "BILL_ID")
    @ApiModelProperty(value = "账单ID")
    private String billId;

    @TableField("BILL_NUMBER")
    @ApiModelProperty(value = "账单号码")
    private String billNumber;

    @TableField("BILL_TYPE")
    @ApiModelProperty(value = "账单类型(INVOICE-发票,RECEIPT-收据,OTHER-其他)")
    private String billType;

    @TableField("SUPPLIER_NAME")
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @TableField("SUPPLIER_TAX_NUMBER")
    @ApiModelProperty(value = "供应商税号")
    private String supplierTaxNumber;

    @TableField("BILL_AMOUNT")
    @ApiModelProperty(value = "账单金额")
    private BigDecimal billAmount;

    @TableField("TAX_AMOUNT")
    @ApiModelProperty(value = "税额")
    private BigDecimal taxAmount;

    @TableField("BILL_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "账单日期")
    private LocalDateTime billDate;

    @TableField("STATUS")
    @ApiModelProperty(value = "状态(COLLECTED-已采集,OCR_PROCESSING-识别中,OCR_SUCCESS-识别成功,OCR_FAILED-识别失败,AUDITING-稽核中,AUDIT_PASSED-稽核通过,AUDIT_FAILED-稽核失败,APPLIED-已应用)")
    private String status;

    @TableField("OCR_STATUS")
    @ApiModelProperty(value = "OCR状态")
    private String ocrStatus;

    @TableField("AUDIT_STATUS")
    @ApiModelProperty(value = "稽核状态")
    private String auditStatus;

    @TableField("COLLECT_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "采集时间")
    private LocalDateTime collectTime;

    @TableField("COLLECT_METHOD")
    @ApiModelProperty(value = "采集方式(SCAN-扫描,MOBILE-移动,UPLOAD-上传)")
    private String collectMethod;

    @TableField("COLLECT_USER")
    @ApiModelProperty(value = "采集人ID")
    private String collectUser;

    @TableField("IMAGE_URL")
    @ApiModelProperty(value = "账单图片地址")
    private String imageUrl;

    @TableField("TARGET_TYPE")
    @ApiModelProperty(value = "应用目标类型(EXPENSE_REPORT/PREPAYMENT/LOAN/PROVISION)")
    private String targetType;

    @TableField("TARGET_ID")
    @ApiModelProperty(value = "应用目标单据ID")
    private String targetId;

    @TableField("APPLY_AMOUNT")
    @ApiModelProperty(value = "应用金额")
    private BigDecimal applyAmount;

    @TableField("APPLY_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "应用时间")
    private LocalDateTime applyTime;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @TableField("IS_ENABLED")
    @ApiModelProperty(value = "是否启用(1-启用,0-停用)")
    private Integer isEnabled;
}
