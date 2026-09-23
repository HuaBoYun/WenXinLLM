package com.financial.sharing.vo.param;

import lombok.Data;
import java.util.Date;

/**
 * 月结任务查询参数
 */
@Data
public class MonthEndTaskQueryParam {
    
    /**
     * 任务ID
     */
    private String taskId;
    
    /**
     * 任务名称
     */
    private String taskName;
    
    /**
     * 任务状态
     */
    private String status;
    
    /**
     * 会计期间
     */
    private String accountingPeriod;
    
    /**
     * 创建人
     */
    private String createBy;
    
    /**
     * 创建时间开始
     */
    private Date createTimeStart;
    
    /**
     * 创建时间结束
     */
    private Date createTimeEnd;
    
    /**
     * 执行时间开始
     */
    private Date executeTimeStart;
    
    /**
     * 执行时间结束
     */
    private Date executeTimeEnd;
    
    /**
     * 页码
     */
    private Integer pageNum = 1;
    
    /**
     * 页大小
     */
    private Integer pageSize = 10;
}
