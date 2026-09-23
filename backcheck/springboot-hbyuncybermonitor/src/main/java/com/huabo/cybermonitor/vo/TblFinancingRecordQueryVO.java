package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 融资记录查询VO */
@Data
@EqualsAndHashCode(callSuper = true)
public class TblFinancingRecordQueryVO extends BaseVo {
    private String companyId;
    private String companyName;
    private String financingType;
    private String financingStatus;
    private String riskLevel;
    private String keyword;
}

