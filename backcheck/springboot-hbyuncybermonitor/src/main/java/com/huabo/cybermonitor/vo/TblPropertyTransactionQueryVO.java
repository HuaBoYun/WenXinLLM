package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 产权交易查询VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TblPropertyTransactionQueryVO extends BaseVo {
    private String propertyId;
    private String transactionType;
    private String transactionMethod;
    private String approvalStatus;
    private String startDate;
    private String endDate;
    private String keyword;
}

