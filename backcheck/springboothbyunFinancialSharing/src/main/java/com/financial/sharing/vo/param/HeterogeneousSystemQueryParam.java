package com.financial.sharing.vo.param;

import lombok.Data;
import java.util.Date;

/**
 * 异构系统查询参数
 */
@Data
public class HeterogeneousSystemQueryParam {
    
    /**
     * 系统编码
     */
    private String systemCode;
    
    /**
     * 系统名称
     */
    private String systemName;
    
    /**
     * 系统类型
     */
    private String systemType;
    
    /**
     * 系统状态
     */
    private String systemStatus;
    
    /**
     * 注册时间开始
     */
    private Date registerTimeStart;
    
    /**
     * 注册时间结束
     */
    private Date registerTimeEnd;
    
    /**
     * 最后同步时间开始
     */
    private Date lastSyncTimeStart;
    
    /**
     * 最后同步时间结束
     */
    private Date lastSyncTimeEnd;
    
    /**
     * 页码
     */
    private Integer pageNum = 1;
    
    /**
     * 页大小
     */
    private Integer pageSize = 10;
}
