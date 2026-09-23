package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据协同记录实体类
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("DATA_COLLABORATION_RECORD")
public class DataCollaborationRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 协同记录ID
     */
    @TableId(value = "COLLABORATION_ID", type = IdType.ASSIGN_UUID)
    private String collaborationId;

    /**
     * 协同类型
     */
    @TableField("COLLABORATION_TYPE")
    private String collaborationType;

    /**
     * 源系统
     */
    @TableField("SOURCE_SYSTEM")
    private String sourceSystem;

    /**
     * 目标系统
     */
    @TableField("TARGET_SYSTEM")
    private String targetSystem;

    /**
     * 数据类型
     */
    @TableField("DATA_TYPE")
    private String dataType;

    /**
     * 数据内容
     */
    @TableField("DATA_CONTENT")
    private String dataContent;

    /**
     * 协同状态
     */
    @TableField("COLLABORATION_STATUS")
    private String collaborationStatus;

    /**
     * 开始时间
     */
    @TableField("START_TIME")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @TableField("END_TIME")
    private LocalDateTime endTime;

    /**
     * 处理时长（秒）
     */
    @TableField("PROCESSING_DURATION")
    private Integer processingDuration;

    /**
     * 数据量
     */
    @TableField("DATA_VOLUME")
    private Long dataVolume;

    /**
     * 成功记录数
     */
    @TableField("SUCCESS_COUNT")
    private Integer successCount;

    /**
     * 失败记录数
     */
    @TableField("FAILURE_COUNT")
    private Integer failureCount;

    /**
     * 错误信息
     */
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    /**
     * 协同配置
     */
    @TableField("COLLABORATION_CONFIG")
    private String collaborationConfig;

    /**
     * 触发方式
     */
    @TableField("TRIGGER_TYPE")
    private String triggerType;

    /**
     * 触发人
     */
    @TableField("TRIGGER_USER")
    private String triggerUser;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    // 协同类型常量
    public static final String TYPE_DATA_SYNC = "DATA_SYNC";         // 数据同步
    public static final String TYPE_DATA_EXCHANGE = "DATA_EXCHANGE"; // 数据交换
    public static final String TYPE_DATA_SHARING = "DATA_SHARING";   // 数据共享

    // 协同状态常量
    public static final String STATUS_PENDING = "PENDING";          // 待处理
    public static final String STATUS_PROCESSING = "PROCESSING";    // 处理中
    public static final String STATUS_SUCCESS = "SUCCESS";          // 成功
    public static final String STATUS_FAILED = "FAILED";            // 失败
    public static final String STATUS_PARTIAL = "PARTIAL";          // 部分成功
    public static final String STATUS_CANCELLED = "CANCELLED";      // 已取消

    // 触发方式常量
    public static final String TRIGGER_MANUAL = "MANUAL";           // 手动触发
    public static final String TRIGGER_SCHEDULED = "SCHEDULED";     // 定时触发
    public static final String TRIGGER_EVENT = "EVENT";             // 事件触发
    public static final String TRIGGER_API = "API";                 // API触发

    // 数据类型常量
    public static final String DATA_TYPE_ENTERPRISE = "ENTERPRISE"; // 企业数据
    public static final String DATA_TYPE_FINANCIAL = "FINANCIAL";   // 财务数据
    public static final String DATA_TYPE_OPERATIONAL = "OPERATIONAL"; // 经营数据
    public static final String DATA_TYPE_GOVERNANCE = "GOVERNANCE"; // 治理数据
    public static final String DATA_TYPE_RISK = "RISK";             // 风险数据
    public static final String DATA_TYPE_COMPLIANCE = "COMPLIANCE"; // 合规数据
}
