package com.financial.sharing.vo.result;

import lombok.Data;
import java.util.Date;
import java.util.Map;

/**
 * 项目数据采集VO
 */
@Data
public class ProjectDataCollectVO {
    
    /**
     * 采集记录ID
     */
    private String collectId;
    
    /**
     * 项目ID
     */
    private String projectId;
    
    /**
     * 项目名称
     */
    private String projectName;
    
    /**
     * 采集类型
     */
    private String collectType;
    
    /**
     * 采集状态
     */
    private String collectStatus;
    
    /**
     * 状态描述
     */
    private String statusDesc;
    
    /**
     * 数据源
     */
    private String dataSource;
    
    /**
     * 采集开始时间
     */
    private Date collectStartTime;
    
    /**
     * 采集结束时间
     */
    private Date collectEndTime;
    
    /**
     * 采集数据量
     */
    private Long dataCount;
    
    /**
     * 成功数量
     */
    private Long successCount;
    
    /**
     * 失败数量
     */
    private Long failCount;
    
    /**
     * 采集结果
     */
    private Map<String, Object> collectResult;
    
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
