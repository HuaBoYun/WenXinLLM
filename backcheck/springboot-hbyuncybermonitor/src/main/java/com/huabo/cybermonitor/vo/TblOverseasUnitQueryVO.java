package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 境外单位查询VO */
@Data
@EqualsAndHashCode(callSuper = true)
public class TblOverseasUnitQueryVO extends BaseVo {
    private String companyId;
    private String companyName;
    private String unitName;
    private String country;
    private String region;
    private String businessType;
    private String countryRiskLevel;
    private String unitStatus;
    private String keyword;
    // 前端传递的 operationStatus 字段
    private String operationStatus;
}

