package com.financial.sharing.vo.result;

import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 月结任务VO
 */
@Data
public class MonthEndTaskVO {
    
    /**
     * 任务ID
     */
    private String taskId;
    
    /**
     * 任务名称
     */
    private String taskName;
    
    /**
     * 任务描述
     */
    private String taskDescription;
    
    /**
     * 任务状态
     */
    private String status;
    
    /**
     * 状态描述
     */
    private String statusDesc;
    
    /**
     * 会计期间
     */
    private String accountingPeriod;
    
    /**
     * 任务类型
     */
    private String taskType;
    
    /**
     * 任务优先级
     */
    private Integer priority;
    
    /**
     * 计划执行时间
     */
    private Date plannedExecuteTime;
    
    /**
     * 实际执行时间
     */
    private Date actualExecuteTime;
    
    /**
     * 完成时间
     */
    private Date completeTime;
    
    /**
     * 执行进度
     */
    private Integer progress;
    
    /**
     * 执行步骤
     */
    private List<String> executeSteps;
    
    /**
     * 当前步骤
     */
    private String currentStep;
    
    /**
     * 执行结果
     */
    private String executeResult;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 创建人
     */
    private String createBy;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新人
     */
    private String updateBy;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    /**
     * 备注
     */
    private String remark;
}
