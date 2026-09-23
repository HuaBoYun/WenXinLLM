package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 财务报表查询VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TblFinanceStatementQueryVO extends BaseVo {
    private String companyId;
    private String companyName;
    private String reportPeriod;
    private String reportType;
    private String isConsolidated;
    private String keyword;
}

