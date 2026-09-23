package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据报送任务查询VO
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DataSubmitTaskQueryVO extends BaseVo {

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 任务分类
     */
    private String taskCategory;

    /**
     * 任务状态
     */
    private String taskStatus;

    /**
     * 报送周期
     */
    private String submitCycle;

    /**
     * 开始日期-起始
     */
    private String startDateBegin;

    /**
     * 开始日期-结束
     */
    private String startDateEnd;

    /**
     * 结束日期-起始
     */
    private String endDateBegin;

    /**
     * 结束日期-结束
     */
    private String endDateEnd;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 发布时间-起始
     */
    private String publishTimeBegin;

    /**
     * 发布时间-结束
     */
    private String publishTimeEnd;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向
     */
    private String orderDirection;
}
