package com.huabo.fxgl.dto;

import lombok.Data;

/**
 * 数据模型版本查询DTO
 *
 * @author AI Assistant
 * @since 2025-09-28
 */
@Data
public class DataModelVersionQueryDTO {

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 版本状态
     */
    private String status;

    /**
     * 是否启用
     */
    private String isEnabled;

    /**
     * 是否当前版本
     */
    private String isCurrent;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 页大小
     */
    private Integer pageSize = 20;

    /**
     * 排序字段
     */
    private String orderBy = "CREATE_TIME";

    /**
     * 排序方向
     */
    private String orderDirection = "DESC";
}
