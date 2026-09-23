package com.financial.sharing.vo.param;

import lombok.Data;
import java.util.Date;
import java.util.Map;

/**
 * 项目数据采集参数
 */
@Data
public class ProjectDataCollectParam {
    
    /**
     * 项目ID
     */
    private String projectId;
    
    /**
     * 采集类型
     */
    private String collectType;
    
    /**
     * 采集日期开始
     */
    private Date collectDateStart;
    
    /**
     * 采集日期结束
     */
    private Date collectDateEnd;
    
    /**
     * 数据源
     */
    private String dataSource;
    
    /**
     * 采集参数
     */
    private Map<String, Object> collectParams;
    
    /**
     * 是否实时采集
     */
    private Boolean realTimeCollect = false;
}
