package com.huabo.finance.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 采集任务详情实体类
 * 
 * @author 华博云开发团队
 * @since 2025-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_COLLECTION_TASK_DETAIL")
@Schema(name = "CollectionTaskDetail对象", description = "采集任务详情")
public class CollectionTaskDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name =  "详情ID")
    @TableId("DETAIL_ID")
    private String detailId;

    @Schema(name =  "任务ID")
    @TableField("TASK_ID")
    private String taskId;

    @Schema(name =  "表配置ID")
    @TableField("TABLE_CONFIG_ID")
    private String tableConfigId;

    @Schema(name =  "源表名")
    @TableField("SOURCE_TABLE_NAME")
    private String sourceTableName;

    @Schema(name =  "目标表名")
    @TableField("TARGET_TABLE_NAME")
    private String targetTableName;

    @Schema(name =  "详情状态(PENDING-待执行, RUNNING-执行中, SUCCESS-成功, FAILED-失败)")
    @TableField("DETAIL_STATUS")
    private String detailStatus;

    @Schema(name =  "采集记录数")
    @TableField("RECORD_COUNT")
    private Long recordCount;

    @Schema(name =  "成功记录数")
    @TableField("SUCCESS_COUNT")
    private Long successCount;

    @Schema(name =  "失败记录数")
    @TableField("FAILED_COUNT")
    private Long failedCount;

    @Schema(name =  "开始时间")
    @TableField("START_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Schema(name =  "结束时间")
    @TableField("END_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Schema(name =  "耗时(毫秒)")
    @TableField("ELAPSED_TIME")
    private Long elapsedTime;

    @Schema(name =  "错误信息")
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    @Schema(name =  "创建时间")
    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(name =  "更新时间")
    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}

