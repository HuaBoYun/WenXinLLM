package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 供应商查询VO */
@Data
@EqualsAndHashCode(callSuper = true)
public class TblSupplierQueryVO extends BaseVo {
    private String companyId;
    private String supplierName;
    private String supplierType;
    private String qualificationLevel;
    private String cooperationStatus;
    private String keyword;
}

