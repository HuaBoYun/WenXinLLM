package com.financial.sharing.vo.result;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 事项分录返回对象
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
public class TransactionEntryVO {

    /**
     * 分录ID
     */
    private Long entryId;

    /**
     * 事项ID
     */
    private Long transactionId;

    /**
     * 分录编号
     */
    private String entryNo;

    /**
     * 科目ID
     */
    private Long subjectId;

    /**
     * 科目编码
     */
    private String subjectCode;

    /**
     * 科目名称
     */
    private String subjectName;

    /**
     * 借方金额
     */
    private BigDecimal debitAmount;

    /**
     * 贷方金额
     */
    private BigDecimal creditAmount;

    /**
     * 币种编码
     */
    private String currencyCode;

    /**
     * 汇率
     */
    private BigDecimal exchangeRate;

    /**
     * 辅助核算信息(JSON格式)
     */
    private String auxiliaryInfo;

    /**
     * 分录摘要
     */
    private String entryDesc;

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
