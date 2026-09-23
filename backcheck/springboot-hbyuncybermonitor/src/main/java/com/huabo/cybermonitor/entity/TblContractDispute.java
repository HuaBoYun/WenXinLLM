package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 合同纠纷记录实体 - 合同穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_CONTRACT_DISPUTE")
public class TblContractDispute {

    @TableId(value = "DISPUTE_ID", type = IdType.ASSIGN_UUID)
    private String disputeId;

    @TableField("CONTRACT_ID")
    private String contractId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("DISPUTE_TYPE")
    private String disputeType;

    @TableField("DISPUTE_AMOUNT")
    private BigDecimal disputeAmount;

    @TableField("COUNTERPARTY_NAME")
    private String counterpartyName;

    @TableField("FILING_DATE")
    private LocalDate filingDate;

    @TableField("COURT_NAME")
    private String courtName;

    @TableField("CASE_STATUS")
    private String caseStatus;

    @TableField("JUDGMENT_RESULT")
    private String judgmentResult;

    @TableField("SETTLEMENT_AMOUNT")
    private BigDecimal settlementAmount;

    @TableField("DISPUTE_REASON")
    private String disputeReason;

    @TableField("LESSONS_LEARNED")
    private String lessonsLearned;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}

