package com.management.accountant.entity.eps;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 预算导入日志表
 * 对应表：tbl_eps_budget_import_log
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_eps_budget_import_log")
public class EpsBudgetImportLog implements Serializable {
    /** 补充字段（来源: 调用点签名反推） */
    private long fileSize;

    /** 补充字段（来源: 调用点签名反推） */
    private LocalDateTime lockedTime;



    private static final long serialVersionUID = 1L;

    /**
     * 日志ID，主键
     */
    @TableId(value = "log_id", type = IdType.AUTO)
    private Long logId;

    /**
     * 所属预算体系ID
     */
    @TableField("system_id")
    private Long systemId;

    /**
     * 预算版本ID
     */
    @TableField("version_id")
    private Long versionId;

    /**
     * 导入批次号
     */
    @TableField("batch_number")
    private String batchNumber;

    /**
     * 导入类型：EXCEL-Excel导入/CSV-CSV导入/XML-XML导入/JSON-JSON导入/API-API导入
     */
    @TableField("import_type")
    private String importType;

    /**
     * 导入模式：FULL-全量导入/INCREMENTAL-增量导入/UPDATE-更新导入/APPEND-追加导入
     */
    @TableField("import_mode")
    private String importMode;

    /**
     * 数据来源：FILE-文件上传/DATABASE-数据库/API-接口/MANUAL-手工录入
     */
    @TableField("data_source")
    private String dataSource;

    /**
     * 源文件名
     */
    @TableField("source_file_name")
    private String sourceFileName;

    /**
     * 源文件路径
     */
    @TableField("source_file_path")
    private String sourceFilePath;

    /**
     * 源文件大小（字节）
     */
    @TableField("source_file_size")
    private Long sourceFileSize;

    /**
     * 源文件MD5
     */
    @TableField("source_file_md5")
    private String sourceFileMd5;

    /**
     * 模板ID
     */
    @TableField("template_id")
    private Long templateId;

    /**
     * 模板名称
     */
    @TableField("template_name")
    private String templateName;

    /**
     * 映射配置，JSON格式存储
     */
    @TableField("mapping_config")
    private String mappingConfig;

    /**
     * 导入配置，JSON格式存储
     */
    @TableField("import_config")
    private String importConfig;

    /**
     * 验证规则，JSON格式存储
     */
    @TableField("validation_rules")
    private String validationRules;

    /**
     * 总记录数
     */
    @TableField("total_records")
    private Integer totalRecords;

    /**
     * 成功记录数
     */
    @TableField("success_records")
    private Integer successRecords;

    /**
     * 失败记录数
     */
    @TableField("failed_records")
    private Integer failedRecords;

    /**
     * 跳过记录数
     */
    @TableField("skipped_records")
    private Integer skippedRecords;

    /**
     * 重复记录数
     */
    @TableField("duplicate_records")
    private Integer duplicateRecords;

    /**
     * 导入状态：PENDING-待处理/PROCESSING-处理中/SUCCESS-成功/FAILED-失败/PARTIAL-部分成功
     */
    @TableField("import_status")
    private String importStatus;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 耗时（毫秒）
     */
    @TableField("duration_ms")
    private Long durationMs;

    /**
     * 错误信息
     */
    @TableField("error_message")
    private String errorMessage;

    /**
     * 错误详情，JSON格式存储
     */
    @TableField("error_details")
    private String errorDetails;

    /**
     * 验证结果，JSON格式存储
     */
    @TableField("validation_result")
    private String validationResult;

    /**
     * 处理日志，JSON格式存储
     */
    @TableField("process_log")
    private String processLog;

    /**
     * 结果文件路径
     */
    @TableField("result_file_path")
    private String resultFilePath;

    /**
     * 错误文件路径
     */
    @TableField("error_file_path")
    private String errorFilePath;

    /**
     * 是否可回滚：0-否/1-是
     */
    @TableField("rollbackable")
    private Integer rollbackable;

    /**
     * 回滚状态：NONE-未回滚/PENDING-待回滚/PROCESSING-回滚中/SUCCESS-回滚成功/FAILED-回滚失败
     */
    @TableField("rollback_status")
    private String rollbackStatus;

    /**
     * 回滚时间
     */
    @TableField("rollback_time")
    private LocalDateTime rollbackTime;

    /**
     * 回滚人ID
     */
    @TableField("rollback_by")
    private Long rollbackBy;

    /**
     * 回滚原因
     */
    @TableField("rollback_reason")
    private String rollbackReason;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建人ID
     */
    @TableField("created_by")
    private Long createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人ID
     */
    @TableField("updated_by")
    private Long updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 逻辑删除标志：0-未删除/1-已删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
