package com.huabo.finance.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据采集任务实体类
 * 
 * @author 华博云开发团队
 * @since 2025-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_COLLECTION_TASK")
@Schema(name="CollectionTask对象", description="数据采集任务")
public class CollectionTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name =  "任务ID")
    @TableId("TASK_ID")
    private String taskId;

    @Schema(name =  "任务名称")
    @TableField("TASK_NAME")
    private String taskName;

    @Schema(name =  "采集方案ID")
    @TableField("PLAN_ID")
    private String planId;

    @Schema(name =  "数据源ID")
    @TableField("DATA_SOURCE_ID")
    private String dataSourceId;

    @Schema(name =  "采集类型(FULL-全量, INCREMENT-增量)")
    @TableField("COLLECTION_TYPE")
    private String collectionType;

    @Schema(name =  "任务状态(PENDING-待执行, RUNNING-执行中, PAUSED-已暂停, SUCCESS-成功, FAILED-失败, CANCELLED-已取消)")
    @TableField("TASK_STATUS")
    private String taskStatus;

    @Schema(name =  "采集进度(0-100)")
    @TableField("PROGRESS")
    private BigDecimal progress;

    @Schema(name =  "已采集记录数")
    @TableField("RECORD_COUNT")
    private Long recordCount;

    @Schema(name =  "总记录数")
    @TableField("TOTAL_COUNT")
    private Long totalCount;

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

    @Schema(name =  "备注")
    @TableField("REMARK")
    private String remark;

    @Schema(name =  "创建时间")
    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(name =  "更新时间")
    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Schema(name =  "创建人")
    @TableField("CREATE_USER")
    private String createUser;

    @Schema(name =  "更新人")
    @TableField("UPDATE_USER")
    private String updateUser;

    @Schema(name =  "所属公司")
    @TableField("LINK_ORG_ID")
    private BigDecimal linkOrgId;

    @Schema(name =  "所属部门")
    @TableField("LINK_DEPT_ID")
    private BigDecimal linkDeptId;

    // 任务状态常量
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_RUNNING = "RUNNING";
    public static final String STATUS_PAUSED = "PAUSED";
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_FAILED = "FAILED";
    public static final String STATUS_CANCELLED = "CANCELLED";

    // 采集类型常量
    public static final String TYPE_FULL = "FULL";
    public static final String TYPE_INCREMENT = "INCREMENT";
}

