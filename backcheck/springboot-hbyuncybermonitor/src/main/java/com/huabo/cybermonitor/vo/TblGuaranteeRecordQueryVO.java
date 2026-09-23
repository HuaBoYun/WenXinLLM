package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 担保记录查询VO */
@Data
@EqualsAndHashCode(callSuper = true)
public class TblGuaranteeRecordQueryVO extends BaseVo {
    private String companyId;
    private String companyName;
    private String guaranteedName;
    private String guaranteeType;
    private String guaranteeStatus;
    private String approvalStatus;
    private String riskLevel;
    private String keyword;
}

