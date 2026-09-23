package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 关联交易查询VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TblRelatedTransactionQueryVO extends BaseVo {
    private String companyId;
    private String companyName;
    private String counterpartyName;
    private String relationType;
    private String transactionType;
    private String approvalStatus;
    private String reportPeriod;
    private String keyword;
}

