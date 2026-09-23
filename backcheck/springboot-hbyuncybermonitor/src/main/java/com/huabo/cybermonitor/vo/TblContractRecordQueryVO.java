package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 合同记录查询VO */
@Data
@EqualsAndHashCode(callSuper = true)
public class TblContractRecordQueryVO extends BaseVo {
    private String companyId;
    private String companyName;
    private String contractCode;
    private String contractName;
    private String contractType;
    private String contractStatus;
    private String counterpartyName;
    private String dataSource;
    private String isMajor;
    private String keyword;
}

