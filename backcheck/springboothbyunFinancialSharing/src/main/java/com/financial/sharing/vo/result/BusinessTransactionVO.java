package com.financial.sharing.vo.result;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 业务事项返回对象
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
public class BusinessTransactionVO {

    /**
     * 事项ID
     */
    private Long transactionId;

    /**
     * 事项编号
     */
    private String transactionNo;

    /**
     * 事项类型
     */
    private String transactionType;

    /**
     * 事项类型名称
     */
    private String transactionTypeName;

    /**
     * 事项日期
     */
    private Date transactionDate;

    /**
     * 业务数据(JSON格式)
     */
    private String businessData;

    /**
     * 事项金额
     */
    private BigDecimal transactionAmount;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 币种代码
     */
    private String currencyCode;

    /**
     * 来源系统
     */
    private String sourceSystem;

    /**
     * 事项状态(1待处理2已处理3已取消)
     */
    private Integer transactionStatus;

    /**
     * 事项状态名称
     */
    private String transactionStatusName;

    /**
     * 账簿ID
     */
    private Long bookId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 更新人
     */
    private Long updater;
}
