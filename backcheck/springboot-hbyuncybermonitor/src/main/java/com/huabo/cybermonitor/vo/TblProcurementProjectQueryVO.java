package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 采购项目查询VO */
@Data
@EqualsAndHashCode(callSuper = true)
public class TblProcurementProjectQueryVO extends BaseVo {
    private String companyId;
    private String companyName;
    private String projectCode;
    private String procurementType;
    private String procurementMethod;
    private String projectStatus;
    private String keyword;
}

