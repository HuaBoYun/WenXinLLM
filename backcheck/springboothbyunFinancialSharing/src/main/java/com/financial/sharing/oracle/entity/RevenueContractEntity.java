package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 收入合同实体类 - Oracle/达梦版本
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_REVENUE_CONTRACT")
public class RevenueContractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 合同ID
     */
    @TableId(value = "CONTRACT_ID", type = IdType.ASSIGN_ID)
    private Long contractId;

    /**
     * 合同编号
     */
    @TableField("CONTRACT_NO")
    private String contractNo;

    /**
     * 合同名称
     */
    @TableField("CONTRACT_NAME")
    private String contractName;

    /**
     * 客户ID
     */
    @TableField("CUSTOMER_ID")
    private Long customerId;

    /**
     * 合同金额
     */
    @TableField("CONTRACT_AMOUNT")
    private BigDecimal contractAmount;

    /**
     * 合同状态(1待生效2履行中3已完成4已终止)
     */
    @TableField("CONTRACT_STATUS")
    private Integer contractStatus;

    /**
     * 签订日期
     */
    @TableField("SIGN_DATE")
    private LocalDate signDate;

    /**
     * 生效日期
     */
    @TableField("EFFECTIVE_DATE")
    private LocalDate effectiveDate;

    /**
     * 到期日期
     */
    @TableField("EXPIRY_DATE")
    private LocalDate expiryDate;

    /**
     * 履约义务
     */
    @TableField("PERFORMANCE_OBLIGATIONS")
    private String performanceObligations;

    /**
     * 确认方法(1时点法2时段法)
     */
    @TableField("RECOGNITION_METHOD")
    private Integer recognitionMethod;

    /**
     * 合同描述
     */
    @TableField("CONTRACT_DESC")
    private String contractDesc;

    /**
     * 账簿ID
     */
    @TableField("BOOK_ID")
    private Long bookId;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private Long tenantId;

    /**
     * 版本号
     */
    @TableField("VERSION")
    @Version
    private Integer version;

    /**
     * 删除标识(0否1是)
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField(value = "CREATOR", fill = FieldFill.INSERT)
    private Long creator;

    /**
     * 更新人
     */
    @TableField(value = "UPDATER", fill = FieldFill.INSERT_UPDATE)
    private Long updater;
}

