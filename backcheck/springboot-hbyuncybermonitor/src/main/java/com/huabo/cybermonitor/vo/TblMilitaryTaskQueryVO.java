package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 军品任务查询VO */
@Data
@EqualsAndHashCode(callSuper = true)
public class TblMilitaryTaskQueryVO extends BaseVo {
    private String companyId;
    private String companyName;
    private String taskCode;
    private String taskType;
    private String securityLevel;
    private String taskStatus;
    private String keyword;
}

