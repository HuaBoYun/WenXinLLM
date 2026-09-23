package com.financial.sharing.mysql.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 导出任务实体类
 *
 * @author 赵工
 * @since 2025-12-07
 */
@Data
public class ExportTaskEntity {

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务类型(cost_allocation/voucher_entry/financial_report)
     */
    private String taskType;

    /**
     * 导出格式(excel/csv/pdf)
     */
    private String exportFormat;

    /**
     * 查询条件(JSON格式)
     */
    private String queryCondition;

    /**
     * 总记录数
     */
    private Long totalRecords;

    /**
     * 已处理记录数
     */
    private Long processedRecords;

    /**
     * 任务状态(PENDING/RUNNING/COMPLETED/FAILED/CANCELLED)
     */
    private Integer status;

    /**
     * 进度百分比
     */
    private BigDecimal progressPercent;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 耗时(秒)
     */
    private Long duration;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小(字节)
     */
    private Long fileSize;

    /**
     * 下载URL
     */
    private String downloadUrl;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 账套ID
     */
    private String bookId;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 更新人
     */
    private Long updater;

    /**
     * 导出配置(JSON格式)
     */
    private String exportConfig;

    /**
     * 分片大小(每批处理的记录数)
     */
    private Integer batchSize;

    /**
     * 当前批次
     */
    private Integer currentBatch;

    /**
     * 总批次数
     */
    private Integer totalBatches;

    /**
     * 并行度
     */
    private Integer parallelism;

    /**
     * 临时文件路径
     */
    private String tempPath;

    /**
     * 压缩文件大小(字节)
     */
    private Long compressedSize;

    /**
     * 下载次数
     */
    private Integer downloadCount;

    /**
     * 最后下载时间
     */
    private Date lastDownloadTime;

    /**
     * 过期时间
     */
    private Date expireTime;
}