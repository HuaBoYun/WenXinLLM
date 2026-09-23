package com.financial.sharing.vo.result;

import lombok.Data;
import java.util.Date;
import java.util.Map;

/**
 * 异构数据同步VO
 */
@Data
public class HeterogeneousDataSyncVO {
    
    /**
     * 同步记录ID
     */
    private String syncId;
    
    /**
     * 系统编码
     */
    private String systemCode;
    
    /**
     * 系统名称
     */
    private String systemName;
    
    /**
     * 同步类型
     */
    private String syncType;
    
    /**
     * 数据类型
     */
    private String dataType;
    
    /**
     * 同步状态
     */
    private String syncStatus;
    
    /**
     * 状态描述
     */
    private String statusDesc;
    
    /**
     * 同步开始时间
     */
    private Date syncStartTime;
    
    /**
     * 同步结束时间
     */
    private Date syncEndTime;
    
    /**
     * 同步耗时（秒）
     */
    private Long syncDuration;
    
    /**
     * 总数据量
     */
    private Long totalDataCount;
    
    /**
     * 成功数量
     */
    private Long successCount;
    
    /**
     * 失败数量
     */
    private Long failCount;
    
    /**
     * 跳过数量
     */
    private Long skipCount;
    
    /**
     * 同步进度
     */
    private Integer syncProgress;
    
    /**
     * 同步结果
     */
    private Map<String, Object> syncResult;
    
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
}
