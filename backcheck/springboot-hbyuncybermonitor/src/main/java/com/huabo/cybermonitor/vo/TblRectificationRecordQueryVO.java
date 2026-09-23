package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 整改记录查询VO */
@Data
@EqualsAndHashCode(callSuper = true)
public class TblRectificationRecordQueryVO extends BaseVo {
    private String taskId;
    private String rectStatus;
    private String responsibleUser;
    private String verifyResult;
    private String keyword;
}

