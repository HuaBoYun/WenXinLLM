package com.financial.sharing.vo.result;

import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 月结进度VO
 */
@Data
public class MonthEndProgressVO {
    
    /**
     * 任务ID
     */
    private String taskId;
    
    /**
     * 任务名称
     */
    private String taskName;
    
    /**
     * 总进度
     */
    private Integer totalProgress;
    
    /**
     * 当前步骤
     */
    private String currentStep;
    
    /**
     * 当前步骤进度
     */
    private Integer currentStepProgress;
    
    /**
     * 步骤详情
     */
    private List<StepProgress> stepProgressList;
    
    /**
     * 开始时间
     */
    private Date startTime;
    
    /**
     * 预计完成时间
     */
    private Date estimatedCompleteTime;
    
    /**
     * 已用时间（秒）
     */
    private Long elapsedTime;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 状态描述
     */
    private String statusDesc;
    
    /**
     * 步骤进度内部类
     */
    @Data
    public static class StepProgress {
        /**
         * 步骤名称
         */
        private String stepName;
        
        /**
         * 步骤状态
         */
        private String stepStatus;
        
        /**
         * 步骤进度
         */
        private Integer stepProgress;
        
        /**
         * 开始时间
         */
        private Date startTime;
        
        /**
         * 完成时间
         */
        private Date completeTime;
        
        /**
         * 错误信息
         */
        private String errorMessage;
    }
}
