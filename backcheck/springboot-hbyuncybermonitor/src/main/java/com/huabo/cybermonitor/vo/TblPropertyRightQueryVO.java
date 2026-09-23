package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 产权登记查询VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TblPropertyRightQueryVO extends BaseVo {
    private String companyId;
    private String companyName;
    private String parentCompanyId;
    private String parentCompanyName;
    /** 产权状态(NORMAL/CHANGING/FROZEN) */
    private String propertyStatus;
    /** 产权类型(SOLE/HOLDING/PARTICIPATING) */
    private String rightType;
    /** 登记状态(REGISTERED/PENDING/CHANGING) */
    private String registrationStatus;
    /** 经营状态(NORMAL/LOSS/LIQUIDATION/CANCELLED) */
    private String businessStatus;
    /** 工商比对结果(CONSISTENT/DIFFERENT) */
    private String registrationConsistency;
    /** 是否并表(Y/N) */
    private String isConsolidated;
    private String industry;
    private String region;
    private Integer equityLevel;
    private String keyword;
}

