package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算数据导入任务实体类
 * 
 * @description 预算数据导入任务管理实体，支持Excel、CSV等格式导入
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_IMPORT_TASK")
public class BudgetImportTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 导入任务编码
     */
    @TableField("TASK_CODE")
    private String taskCode;

    /**
     * 导入任务名称
     */
    @TableField("TASK_NAME")
    private String taskName;

    /**
     * 导入类型：budget_data-预算数据，actual_data-实际数据，dimension_data-维度数据，indicator_data-指标数据
     */
    @TableField("IMPORT_TYPE")
    private String importType;

    /**
     * 文件类型：excel-Excel文件，csv-CSV文件，txt-文本文件
     */
    @TableField("FILE_TYPE")
    private String fileType;

    /**
     * 原始文件名
     */
    @TableField("ORIGINAL_FILE_NAME")
    private String originalFileName;

    /**
     * 存储文件名
     */
    @TableField("STORED_FILE_NAME")
    private String storedFileName;

    /**
     * 文件路径
     */
    @TableField("FILE_PATH")
    private String filePath;

    /**
     * 文件大小（字节）
     */
    @TableField("FILE_SIZE")
    private Long fileSize;

    /**
     * 文件MD5
     */
    @TableField("FILE_MD5")
    private String fileMd5;

    /**
     * 导入模板ID
     */
    @TableField("TEMPLATE_ID")
    private String templateId;

    /**
     * 导入配置（JSON格式）
     */
    @TableField("IMPORT_CONFIG")
    private String importConfig;

    /**
     * 字段映射配置（JSON格式）
     */
    @TableField("FIELD_MAPPING")
    private String fieldMapping;

    /**
     * 数据验证规则（JSON格式）
     */
    @TableField("VALIDATION_RULES")
    private String validationRules;

    /**
     * 任务状态：pending-待处理，processing-处理中，completed-已完成，failed-失败，cancelled-已取消
     */
    @TableField("TASK_STATUS")
    private String taskStatus;

    /**
     * 执行进度（百分比）
     */
    @TableField("PROGRESS")
    private Integer progress;

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
     * 执行耗时（毫秒）
     */
    @TableField("EXECUTION_TIME")
    private Long executionTime;

    /**
     * 总记录数
     */
    @TableField("TOTAL_RECORDS")
    private Integer totalRecords;

    /**
     * 成功记录数
     */
    @TableField("SUCCESS_RECORDS")
    private Integer successRecords;

    /**
     * 失败记录数
     */
    @TableField("FAILED_RECORDS")
    private Integer failedRecords;

    /**
     * 跳过记录数
     */
    @TableField("SKIPPED_RECORDS")
    private Integer skippedRecords;

    /**
     * 错误信息
     */
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    /**
     * 错误详情文件路径
     */
    @TableField("ERROR_FILE_PATH")
    private String errorFilePath;

    /**
     * 导入结果摘要（JSON格式）
     */
    @TableField("RESULT_SUMMARY")
    private String resultSummary;

    /**
     * 预算年度
     */
    @TableField("BUDGET_YEAR")
    private Integer budgetYear;

    /**
     * 预算期间
     */
    @TableField("BUDGET_PERIOD")
    private String budgetPeriod;

    /**
     * 组织体系ID
     */
    @TableField("STRUCTURE_ID")
    private String structureId;

    /**
     * 目标表名
     */
    @TableField("TARGET_TABLE")
    private String targetTable;

    /**
     * 导入模式：insert-插入，update-更新，upsert-插入或更新
     */
    @TableField("IMPORT_MODE")
    private String importMode;

    /**
     * 是否覆盖已有数据
     */
    @TableField("IS_OVERWRITE")
    private Boolean isOverwrite;

    /**
     * 是否备份原数据
     */
    @TableField("IS_BACKUP")
    private Boolean isBackup;

    /**
     * 备份文件路径
     */
    @TableField("BACKUP_FILE_PATH")
    private String backupFilePath;

    /**
     * 创建人ID
     */
    @TableField("CREATOR_ID")
    private String creatorId;

    /**
     * 创建人姓名
     */
    @TableField("CREATOR_NAME")
    private String creatorName;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 状态：active-激活，inactive-停用
     */
    @TableField("STATUS")
    private String status;

    /**
     * 创建人ID
     */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标志：0-未删除，1-已删除
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号（乐观锁）
     */
    @TableField("VERSION")
    @Version
    private Integer version;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 扩展字段1
     */
    @TableField("EXT_FIELD1")
    private String extField1;

    /**
     * 扩展字段2
     */
    @TableField("EXT_FIELD2")
    private String extField2;

    /**
     * 扩展字段3
     */
    @TableField("EXT_FIELD3")
    private String extField3;

    /**
     * 扩展字段4
     */
    @TableField("EXT_FIELD4")
    private String extField4;

    /**
     * 扩展字段5
     */
    @TableField("EXT_FIELD5")
    private String extField5;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    // 导入类型常量
    public static final String IMPORT_TYPE_BUDGET_DATA = "budget_data";
    public static final String IMPORT_TYPE_ACTUAL_DATA = "actual_data";
    public static final String IMPORT_TYPE_DIMENSION_DATA = "dimension_data";
    public static final String IMPORT_TYPE_INDICATOR_DATA = "indicator_data";

    // 文件类型常量
    public static final String FILE_TYPE_EXCEL = "excel";
    public static final String FILE_TYPE_CSV = "csv";
    public static final String FILE_TYPE_TXT = "txt";

    // 任务状态常量
    public static final String TASK_STATUS_PENDING = "pending";
    public static final String TASK_STATUS_PROCESSING = "processing";
    public static final String TASK_STATUS_COMPLETED = "completed";
    public static final String TASK_STATUS_FAILED = "failed";
    public static final String TASK_STATUS_CANCELLED = "cancelled";

    // 导入模式常量
    public static final String IMPORT_MODE_INSERT = "insert";
    public static final String IMPORT_MODE_UPDATE = "update";
    public static final String IMPORT_MODE_UPSERT = "upsert";

    // 状态常量
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
}
