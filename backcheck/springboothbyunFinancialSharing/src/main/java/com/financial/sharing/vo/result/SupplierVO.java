package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 供应商VO
 * @author system
 * @since 2025-01-05
 */
@Data
@ApiModel("供应商VO")
public class SupplierVO {

    @ApiModelProperty("供应商ID")
    private String supplierId;

    @ApiModelProperty("供应商编码")
    private String supplierCode;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("供应商分类")
    private Integer supplierCategory;

    @ApiModelProperty("供应商分类名称")
    private String supplierCategoryName;

    @ApiModelProperty("供应商状态")
    private Integer supplierStatus;

    @ApiModelProperty("供应商状态名称")
    private String supplierStatusName;

    @ApiModelProperty("统一社会信用代码")
    private String creditCode;

    @ApiModelProperty("法定代表人")
    private String legalRepresentative;

    @ApiModelProperty("注册地址")
    private String registeredAddress;

    @ApiModelProperty("联系人")
    private String contactPerson;

    @ApiModelProperty("联系电话")
    private String contactPhone;

    @ApiModelProperty("联系邮箱")
    private String contactEmail;

    @ApiModelProperty("传真号码")
    private String faxNumber;

    @ApiModelProperty("通讯地址")
    private String contactAddress;

    @ApiModelProperty("开户银行")
    private String bankName;

    @ApiModelProperty("银行账号")
    private String bankAccount;

    @ApiModelProperty("信用等级")
    private String creditLevel;

    @ApiModelProperty("信用额度")
    private BigDecimal creditLimit;

    @ApiModelProperty("付款方式")
    private Integer paymentMethod;

    @ApiModelProperty("付款方式名称")
    private String paymentMethodName;

    @ApiModelProperty("账期天数")
    private Integer paymentTerms;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("应付余额")
    private BigDecimal payableAmount;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    private String createBy;
}

