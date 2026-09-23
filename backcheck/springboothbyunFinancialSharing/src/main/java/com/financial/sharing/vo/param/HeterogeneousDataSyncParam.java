package com.financial.sharing.vo.param;

import lombok.Data;
import java.util.Date;
import java.util.Map;

/**
 * 异构数据同步参数
 */
@Data
public class HeterogeneousDataSyncParam {
    
    /**
     * 系统编码
     */
    private String systemCode;
    
    /**
     * 同步类型
     */
    private String syncType;
    
    /**
     * 数据类型
     */
    private String dataType;
    
    /**
     * 同步开始时间
     */
    private Date syncStartTime;
    
    /**
     * 同步结束时间
     */
    private Date syncEndTime;
    
    /**
     * 同步参数
     */
    private Map<String, Object> syncParams;
    
    /**
     * 是否增量同步
     */
    private Boolean incrementalSync = true;
    
    /**
     * 批次大小
     */
    private Integer batchSize = 1000;
}
