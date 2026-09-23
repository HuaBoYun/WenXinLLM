package com.financial.sharing.mysql.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 供应商实体类
 * @author system
 * @since 2025-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "TBL_SUPPLIER")
public class TblSupplier implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 供应商ID */
    @Id
    @Column(name = "SUPPLIER_ID")
    private String supplierId;

    /** 供应商编码 */
    @Column(name = "SUPPLIER_CODE")
    private String supplierCode;

    /** 供应商名称 */
    @Column(name = "SUPPLIER_NAME")
    private String supplierName;

    /** 供应商分类(1:原材料,2:设备,3:服务,4:其他) */
    @Column(name = "SUPPLIER_CATEGORY")
    private Integer supplierCategory;

    /** 供应商状态(1:正常,2:暂停,3:黑名单) */
    @Column(name = "SUPPLIER_STATUS")
    private Integer supplierStatus;

    /** 统一社会信用代码 */
    @Column(name = "CREDIT_CODE")
    private String creditCode;

    /** 税号(匹配现有表字段) */
    @Column(name = "TAX_NO")
    private String taxNo;

    /** 是否核心供应商(匹配现有表字段) */
    @Column(name = "IS_CORE_SUPPLIER")
    private String isCoreSupplier;

    /** 法定代表人 */
    @Column(name = "LEGAL_REPRESENTATIVE")
    private String legalRepresentative;

    /** 注册地址 */
    @Column(name = "REGISTERED_ADDRESS")
    private String registeredAddress;

    /** 联系人 */
    @Column(name = "CONTACT_PERSON")
    private String contactPerson;

    /** 联系电话 */
    @Column(name = "CONTACT_PHONE")
    private String contactPhone;

    /** 联系邮箱 */
    @Column(name = "CONTACT_EMAIL")
    private String contactEmail;

    /** 传真号码 */
    @Column(name = "FAX_NUMBER")
    private String faxNumber;

    /** 通讯地址 */
    @Column(name = "CONTACT_ADDRESS")
    private String contactAddress;

    /** 开户银行 */
    @Column(name = "BANK_NAME")
    private String bankName;

    /** 银行账号 */
    @Column(name = "BANK_ACCOUNT")
    private String bankAccount;

    /** 信用等级(AAA,AA,A,BBB,BB,B) */
    @Column(name = "CREDIT_LEVEL")
    private String creditLevel;

    /** 信用额度 */
    @Column(name = "CREDIT_LIMIT")
    private BigDecimal creditLimit;

    /** 付款方式(1:现金,2:银行转账,3:支票,4:承兑汇票) */
    @Column(name = "PAYMENT_METHOD")
    private Integer paymentMethod;

    /** 账期天数 */
    @Column(name = "PAYMENT_TERMS")
    private Integer paymentTerms;

    /** 备注 */
    @Column(name = "REMARKS")
    private String remarks;

    /** 创建时间 */
    @Column(name = "CREATE_TIME")
    private Date createTime;

    /** 更新时间 */
    @Column(name = "UPDATE_TIME")
    private Date updateTime;

    /** 创建人 */
    @Column(name = "CREATE_BY")
    private String createBy;

    /** 更新人 */
    @Column(name = "UPDATE_BY")
    private String updateBy;

    /** 租户ID */
    @Column(name = "TENANT_ID")
    private String tenantId;

    /** 逻辑删除标记 */
    @Column(name = "IS_DELETED")
    private Integer isDeleted;

    // ========== 非持久化字段 ==========

    /** 供应商分类名称 */
    @Transient
    private String supplierCategoryName;

    /** 应付余额 */
    @Transient
    private BigDecimal payableAmount;
}

