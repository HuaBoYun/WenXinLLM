package com.management.accountant.oracle.entity.integration;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算数据流集成实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_DATASTREAM_INTEGRATION")
public class BudgetDataStreamIntegration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据流集成ID (主键)
     */
    @TableId(value = "STREAM_ID", type = IdType.ASSIGN_UUID)
    private String streamId;

    /**
     * 数据流编码
     */
    @TableField("STREAM_CODE")
    private String streamCode;

    /**
     * 数据流名称
     */
    @TableField("STREAM_NAME")
    private String streamName;

    /**
     * 数据流类型 (KAFKA_STREAM/FLINK/SPARK_STREAMING/STORM)
     */
    @TableField("STREAM_TYPE")
    private String streamType;

    /**
     * 数据源类型 (KAFKA/RABBITMQ/FILE/DATABASE)
     */
    @TableField("SOURCE_TYPE")
    private String sourceType;

    /**
     * 数据源配置 (JSON格式)
     */
    @TableField("SOURCE_CONFIG")
    private String sourceConfig;

    /**
     * 数据目标类型 (DATABASE/FILE/KAFKA/ELASTICSEARCH)
     */
    @TableField("TARGET_TYPE")
    private String targetType;

    /**
     * 数据目标配置 (JSON格式)
     */
    @TableField("TARGET_CONFIG")
    private String targetConfig;

    /**
     * 处理模式 (REALTIME/BATCH/MICRO_BATCH)
     */
    @TableField("PROCESS_MODE")
    private String processMode;

    /**
     * 窗口类型 (TUMBLING/SLIDING/SESSION)
     */
    @TableField("WINDOW_TYPE")
    private String windowType;

    /**
     * 窗口大小(秒)
     */
    @TableField("WINDOW_SIZE")
    private Integer windowSize;

    /**
     * 滑动间隔(秒)
     */
    @TableField("SLIDE_INTERVAL")
    private Integer slideInterval;

    /**
     * 数据转换规则 (JSON格式)
     */
    @TableField("TRANSFORM_RULES")
    private String transformRules;

    /**
     * 过滤条件 (JSON格式)
     */
    @TableField("FILTER_CONDITIONS")
    private String filterConditions;

    /**
     * 聚合规则 (JSON格式)
     */
    @TableField("AGGREGATION_RULES")
    private String aggregationRules;

    /**
     * 并行度
     */
    @TableField("PARALLELISM")
    private Integer parallelism;

    /**
     * 检查点间隔(秒)
     */
    @TableField("CHECKPOINT_INTERVAL")
    private Integer checkpointInterval;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 启动时间
     */
    @TableField("START_TIME")
    private Date startTime;

    /**
     * 停止时间
     */
    @TableField("STOP_TIME")
    private Date stopTime;

    /**
     * 处理记录总数
     */
    @TableField("TOTAL_RECORDS")
    private Long totalRecords;

    /**
     * 成功记录数
     */
    @TableField("SUCCESS_RECORDS")
    private Long successRecords;

    /**
     * 失败记录数
     */
    @TableField("FAILURE_RECORDS")
    private Long failureRecords;

    /**
     * 平均处理延迟(毫秒)
     */
    @TableField("AVG_LATENCY")
    private Integer avgLatency;

    /**
     * 吞吐量(记录/秒)
     */
    @TableField("THROUGHPUT")
    private Integer throughput;

    /**
     * 集成状态 (RUNNING/STOPPED/ERROR/TESTING)
     */
    @TableField("INTEGRATION_STATUS")
    private String integrationStatus;

    /**
     * 数据流状态
     */
    @TableField("STREAM_STATUS")
    private String streamStatus;

    /**
     * 最后启动时间
     */
    @TableField("LAST_START_TIME")
    private Date lastStartTime;

    /**
     * 最后处理时间
     */
    @TableField("LAST_PROCESS_TIME")
    private Date lastProcessTime;

    /**
     * 错误信息
     */
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    /**
     * 备注说明
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建人
     */
    @TableField("CREATED_BY")
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField("CREATED_TIME")
    private Date createdTime;

    /**
     * 更新人
     */
    @TableField("UPDATED_BY")
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField("UPDATED_TIME")
    private Date updatedTime;
}

