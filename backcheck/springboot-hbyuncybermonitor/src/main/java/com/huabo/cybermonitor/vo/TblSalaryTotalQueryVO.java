package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 工资总额查询VO */
@Data
@EqualsAndHashCode(callSuper = true)
public class TblSalaryTotalQueryVO extends BaseVo {
    private String companyId;
    private String companyName;
    private String reportYear;
    private String reportMonth;
    private String keyword;
}

