package com.financial.sharing.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 供应商保存参数
 * @author system
 * @since 2025-01-05
 */
@Data
@ApiModel("供应商保存参数")
public class SupplierSaveParam {

    @ApiModelProperty("供应商ID(新增时为空)")
    private String supplierId;

    @NotBlank(message = "供应商编码不能为空")
    @ApiModelProperty(value = "供应商编码", required = true)
    private String supplierCode;

    @NotBlank(message = "供应商名称不能为空")
    @ApiModelProperty(value = "供应商名称", required = true)
    private String supplierName;

    @NotNull(message = "供应商分类不能为空")
    @ApiModelProperty(value = "供应商分类(1:原材料,2:设备,3:服务,4:其他)", required = true)
    private Integer supplierCategory;

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

    @ApiModelProperty("信用等级(AAA,AA,A,BBB,BB,B)")
    private String creditLevel;

    @ApiModelProperty("信用额度")
    private BigDecimal creditLimit;

    @ApiModelProperty("付款方式(1:现金,2:银行转账,3:支票,4:承兑汇票)")
    private Integer paymentMethod;

    @ApiModelProperty("账期天数")
    private Integer paymentTerms;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("供应商状态(0:禁用,1:启用)")
    private Integer supplierStatus;
}

