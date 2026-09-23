package com.financial.sharing.vo.param;

import lombok.Data;
import java.util.Map;

/**
 * 月结任务执行参数
 */
@Data
public class MonthEndTaskExecuteParam {
    
    /**
     * 任务ID
     */
    private String taskId;
    
    /**
     * 执行模式（手动/自动）
     */
    private String executeMode;
    
    /**
     * 执行参数
     */
    private Map<String, Object> executeParams;
    
    /**
     * 是否强制执行
     */
    private Boolean forceExecute = false;
    
    /**
     * 执行备注
     */
    private String executeRemark;
}
