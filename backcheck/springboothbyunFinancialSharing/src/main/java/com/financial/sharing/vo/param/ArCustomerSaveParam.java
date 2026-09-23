package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 客户档案保存参数
 * @author system
 * @since 2026-01-04
 */
@Data
@ApiModel(value = "ArCustomerSaveParam", description = "客户档案保存参数")
public class ArCustomerSaveParam {

    @ApiModelProperty(value = "客户ID（更新时必填）")
    private String customerId;

    @ApiModelProperty(value = "客户编码", required = true)
    @NotBlank(message = "客户编码不能为空")
    @Size(max = 50, message = "客户编码长度不能超过50个字符")
    private String customerCode;

    @ApiModelProperty(value = "客户名称", required = true)
    @NotBlank(message = "客户名称不能为空")
    @Size(max = 200, message = "客户名称长度不能超过200个字符")
    private String customerName;

    @ApiModelProperty(value = "客户分类(1重要客户 2一般客户 3潜在客户 4其他)")
    private Integer customerCategory;

    @ApiModelProperty(value = "客户状态(1正常 2暂停 3黑名单)")
    private Integer customerStatus;

    @ApiModelProperty(value = "统一社会信用代码")
    @Size(max = 50, message = "统一社会信用代码长度不能超过50个字符")
    private String creditCode;

    @ApiModelProperty(value = "法定代表人")
    @Size(max = 100, message = "法定代表人长度不能超过100个字符")
    private String legalRepresentative;

    @ApiModelProperty(value = "注册地址")
    @Size(max = 500, message = "注册地址长度不能超过500个字符")
    private String registeredAddress;

    @ApiModelProperty(value = "联系人")
    @Size(max = 100, message = "联系人长度不能超过100个字符")
    private String contactPerson;

    @ApiModelProperty(value = "联系电话")
    @Size(max = 50, message = "联系电话长度不能超过50个字符")
    private String contactPhone;

    @ApiModelProperty(value = "联系邮箱")
    @Size(max = 100, message = "联系邮箱长度不能超过100个字符")
    private String contactEmail;

    @ApiModelProperty(value = "传真号码")
    @Size(max = 50, message = "传真号码长度不能超过50个字符")
    private String faxNumber;

    @ApiModelProperty(value = "通讯地址")
    @Size(max = 500, message = "通讯地址长度不能超过500个字符")
    private String contactAddress;

    @ApiModelProperty(value = "开户银行")
    @Size(max = 200, message = "开户银行长度不能超过200个字符")
    private String bankName;

    @ApiModelProperty(value = "银行账号")
    @Size(max = 50, message = "银行账号长度不能超过50个字符")
    private String bankAccount;

    @ApiModelProperty(value = "信用等级(AAA/AA/A/BBB/BB/B)")
    @Size(max = 10, message = "信用等级长度不能超过10个字符")
    private String creditLevel;

    @ApiModelProperty(value = "信用额度")
    private BigDecimal creditLimit;

    @ApiModelProperty(value = "收款方式")
    private Integer collectionMethod;

    @ApiModelProperty(value = "账期天数")
    private Integer creditTerms;

    @ApiModelProperty(value = "备注")
    @Size(max = 1000, message = "备注长度不能超过1000个字符")
    private String remarks;

    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @ApiModelProperty(value = "组织ID")
    private String orgId;

    @ApiModelProperty(value = "操作人ID")
    private String operatorId;
}

