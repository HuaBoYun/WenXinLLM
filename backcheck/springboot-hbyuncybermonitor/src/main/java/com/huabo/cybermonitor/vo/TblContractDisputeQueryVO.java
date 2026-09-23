package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 合同纠纷查询VO */
@Data
@EqualsAndHashCode(callSuper = true)
public class TblContractDisputeQueryVO extends BaseVo {
    private String contractId;
    private String companyId;
    private String disputeType;
    private String caseStatus;
    private String counterpartyName;
    private String keyword;
}

