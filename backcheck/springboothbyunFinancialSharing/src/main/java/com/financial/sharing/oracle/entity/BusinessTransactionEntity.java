package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 业务事项表 - Oracle/达梦版本
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_BUSINESS_TRANSACTION")
public class BusinessTransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事项ID
     */
    @TableId(value = "TRANSACTION_ID", type = IdType.ASSIGN_ID)
    private Long transactionId;

    /**
     * 事项编号
     */
    @TableField("TRANSACTION_NO")
    private String transactionNo;

    /**
     * 事项类型
     */
    @TableField("TRANSACTION_TYPE")
    private String transactionType;

    /**
     * 事项日期
     */
    @TableField("TRANSACTION_DATE")
    private LocalDate transactionDate;

    /**
     * 业务数据(JSON格式)
     */
    @TableField("BUSINESS_DATA")
    private String businessData;

    /**
     * 来源系统
     */
    @TableField("SOURCE_SYSTEM")
    private String sourceSystem;

    /**
     * 事项状态(1待处理2已处理3已取消)
     */
    @TableField("TRANSACTION_STATUS")
    private Integer transactionStatus;

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
