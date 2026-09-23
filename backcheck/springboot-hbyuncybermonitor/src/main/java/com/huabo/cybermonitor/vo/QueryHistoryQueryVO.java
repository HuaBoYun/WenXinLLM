package com.huabo.cybermonitor.vo;
import java.time.LocalDateTime;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询历史记录查询参数VO
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryHistoryQueryVO extends BaseVo {

    /**
     * 查询名称
     */
    private String queryName;

    /**
     * 查询类型
     */
    private String queryType;

    /**
     * 查询状态
     */
    private String queryStatus;

    /**
     * 查询人
     */
    private String queryBy;

    /**
     * 查询开始时间
     */
    private LocalDateTime queryTimeStart;

    /**
     * 查询结束时间
     */
    private LocalDateTime queryTimeEnd;

    /**
     * 最小执行时间（毫秒）
     */
    private Long minExecutionTime;

    /**
     * 最大执行时间（毫秒）
     */
    private Long maxExecutionTime;
}
