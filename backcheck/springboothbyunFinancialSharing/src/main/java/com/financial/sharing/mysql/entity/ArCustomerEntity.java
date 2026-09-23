package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户档案实体类
 * @author system
 * @since 2026-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_AR_CUSTOMER")
@ApiModel(value = "ArCustomerEntity对象", description = "客户档案表")
public class ArCustomerEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "客户ID")
    @TableId(value = "CUSTOMER_ID", type = IdType.ASSIGN_UUID)
    private String customerId;

    @ApiModelProperty(value = "客户编码")
    @TableField("CUSTOMER_CODE")
    private String customerCode;

    @ApiModelProperty(value = "客户名称")
    @TableField("CUSTOMER_NAME")
    private String customerName;

    @ApiModelProperty(value = "客户分类(1重要客户 2一般客户 3潜在客户 4其他)")
    @TableField("CUSTOMER_CATEGORY")
    private Integer customerCategory;

    @ApiModelProperty(value = "客户状态(1正常 2暂停 3黑名单)")
    @TableField("CUSTOMER_STATUS")
    private Integer customerStatus;

    @ApiModelProperty(value = "统一社会信用代码")
    @TableField("CREDIT_CODE")
    private String creditCode;

    @ApiModelProperty(value = "法定代表人")
    @TableField("LEGAL_REPRESENTATIVE")
    private String legalRepresentative;

    @ApiModelProperty(value = "注册地址")
    @TableField("REGISTERED_ADDRESS")
    private String registeredAddress;

    @ApiModelProperty(value = "联系人")
    @TableField("CONTACT_PERSON")
    private String contactPerson;

    @ApiModelProperty(value = "联系电话")
    @TableField("CONTACT_PHONE")
    private String contactPhone;

    @ApiModelProperty(value = "联系邮箱")
    @TableField("CONTACT_EMAIL")
    private String contactEmail;

    @ApiModelProperty(value = "传真号码")
    @TableField("FAX_NUMBER")
    private String faxNumber;

    @ApiModelProperty(value = "通讯地址")
    @TableField("CONTACT_ADDRESS")
    private String contactAddress;

    @ApiModelProperty(value = "开户银行")
    @TableField("BANK_NAME")
    private String bankName;

    @ApiModelProperty(value = "银行账号")
    @TableField("BANK_ACCOUNT")
    private String bankAccount;

    @ApiModelProperty(value = "信用等级(AAA/AA/A/BBB/BB/B)")
    @TableField("CREDIT_LEVEL")
    private String creditLevel;

    @ApiModelProperty(value = "信用额度")
    @TableField("CREDIT_LIMIT")
    private BigDecimal creditLimit;

    @ApiModelProperty(value = "收款方式")
    @TableField("COLLECTION_METHOD")
    private Integer collectionMethod;

    @ApiModelProperty(value = "账期天数")
    @TableField("CREDIT_TERMS")
    private Integer creditTerms;

    @ApiModelProperty(value = "备注")
    @TableField("REMARKS")
    private String remarks;

    @ApiModelProperty(value = "租户ID")
    @TableField("TENANT_ID")
    private Long tenantId;

    @ApiModelProperty(value = "组织ID")
    @TableField("ORG_ID")
    private String orgId;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "删除标识(0否 1是)")
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    // ========== 扩展字段（不映射到数据库）==========

    @ApiModelProperty(value = "客户分类名称")
    @TableField(exist = false)
    private String customerCategoryName;

    @ApiModelProperty(value = "客户状态名称")
    @TableField(exist = false)
    private String customerStatusName;

    @ApiModelProperty(value = "应收金额")
    @TableField(exist = false)
    private BigDecimal receivableAmount;
}

