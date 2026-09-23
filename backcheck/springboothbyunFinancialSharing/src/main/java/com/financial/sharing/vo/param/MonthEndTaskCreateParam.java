package com.financial.sharing.vo.param;

import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 月结任务创建参数
 */
@Data
public class MonthEndTaskCreateParam {
    
    /**
     * 任务名称
     */
    private String taskName;
    
    /**
     * 任务描述
     */
    private String taskDescription;
    
    /**
     * 会计期间
     */
    private String accountingPeriod;
    
    /**
     * 计划执行时间
     */
    private Date plannedExecuteTime;
    
    /**
     * 任务类型
     */
    private String taskType;
    
    /**
     * 任务优先级
     */
    private Integer priority;
    
    /**
     * 执行步骤
     */
    private List<String> executeSteps;
    
    /**
     * 备注
     */
    private String remark;
}
