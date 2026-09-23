package com.huabo.audit.oracle.dto;

import com.huabo.audit.util.BaseVo;
import lombok.Data;

/**
 * @author lyz
 * @description
 */
@Data
public class AuditPlanListSearchDto extends BaseVo {
    private String planYear;
    private String jhName;
    private String token;
    private String choiceSearch;
}
