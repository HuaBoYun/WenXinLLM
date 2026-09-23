package com.huabo.finance.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据采集任务VO类
 * 用于前端展示,字段名与前端保持一致
 * 
 * @author 华博云开发团队
 * @since 2025-10-24
 */
@Data
@Schema(name = "CollectionTaskVO对象", description = "数据采集任务视图对象")
public class CollectionTaskVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name =  "任务ID")
    private String taskId;

    @Schema(name =  "任务名称")
    private String taskName;

    @Schema(name =  "采集方案ID")
    private String planId;

    @Schema(name =  "数据源ID")
    private String dataSourceId;

    @Schema(name =  "数据源名称")
    private String dataSourceName;

    @Schema(name =  "采集类型(FULL-全量, INCREMENT-增量)")
    private String collectionType;

    @Schema(name =  "任务状态(PENDING-待执行, RUNNING-执行中, PAUSED-已暂停, SUCCESS-成功, FAILED-失败, CANCELLED-已取消)")
    private String status;  // 前端使用status,而不是taskStatus

    @Schema(name =  "采集进度(0-100)")
    private Integer progress;  // 转换为Integer,前端更好处理

    @Schema(name =  "已采集记录数")
    private Long recordCount;

    @Schema(name =  "总记录数")
    private Long totalCount;

    @Schema(name =  "成功记录数")
    private Long successCount;

    @Schema(name =  "失败记录数")
    private Long failedCount;

    @Schema(name =  "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @Schema(name =  "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @Schema(name =  "耗时(毫秒)")
    private Long elapsedTime;

    @Schema(name =  "错误信息")
    private String errorMessage;

    @Schema(name =  "备注")
    private String remark;

    @Schema(name =  "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Schema(name =  "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}

