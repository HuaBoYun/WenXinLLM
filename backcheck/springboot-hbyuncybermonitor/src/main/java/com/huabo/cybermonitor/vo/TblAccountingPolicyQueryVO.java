package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 会计政策查询VO */
@Data
@EqualsAndHashCode(callSuper = true)
public class TblAccountingPolicyQueryVO extends BaseVo {
    private String companyId;
    private String companyName;
    private String policyType;
    private String reportYear;
    private String isConsistent;
    private String status;
    private String keyword;
}

