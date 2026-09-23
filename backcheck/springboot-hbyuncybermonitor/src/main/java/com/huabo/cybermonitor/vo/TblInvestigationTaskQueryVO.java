package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 核查任务查询VO */
@Data
@EqualsAndHashCode(callSuper = true)
public class TblInvestigationTaskQueryVO extends BaseVo {
    private String companyId;
    private String companyName;
    private String domainType;
    private String taskStatus;
    private String assignUser;
    private String conclusion;
    private String keyword;
}

