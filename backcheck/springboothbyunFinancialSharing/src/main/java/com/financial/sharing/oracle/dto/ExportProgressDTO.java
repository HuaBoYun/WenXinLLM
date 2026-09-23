package com.financial.sharing.oracle.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 导出进度DTO
 *
 * @author 赵工
 * @since 2025-12-07
 */
@Data
public class ExportProgressDTO {

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 总记录数
     */
    private Long totalRecords;

    /**
     * 已处理记录数
     */
    private Long processedRecords;

    /**
     * 进度百分比
     */
    private BigDecimal progressPercent;

    /**
     * 当前批次
     */
    private Integer currentBatch;

    /**
     * 总批次数
     */
    private Integer totalBatches;

    /**
     * 处理速度(记录/秒)
     */
    private Double processingRate;

    /**
     * 预估剩余时间(秒)
     */
    private Long estimatedRemainingTime;

    /**
     * 已用时间(秒)
     */
    private Long elapsedTime;

    /**
     * 当前阶段
     */
    private String currentPhase;

    /**
     * 阶段进度
     */
    private Map<String, BigDecimal> phaseProgress;

    /**
     * 错误数量
     */
    private Integer errorCount;

    /**
     * 警告数量
     */
    private Integer warningCount;

    /**
     * 内存使用量(MB)
     */
    private Double memoryUsage;

    /**
     * CPU使用率(%)
     */
    private Double cpuUsage;

    /**
     * 磁盘I/O速度(MB/s)
     */
    private Double ioRate;

    /**
     * 网络传输速度(MB/s)
     */
    private Double networkRate;

    /**
     * 临时文件大小(MB)
     */
    private Double tempFileSize;

    /**
     * 压缩率(%)
     */
    private BigDecimal compressionRate;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 状态信息
     */
    private String statusMessage;

    /**
     * 附加信息
     */
    private Map<String, Object> additionalInfo;

    /**
     * 是否阻塞状态
     */
    private Boolean isBlocked = false;

    /**
     * 阻塞原因
     */
    private String blockReason;

    /**
     * 重试次数
     */
    private Integer retryCount = 0;

    /**
     * 最大重试次数
     */
    private Integer maxRetries = 3;
}