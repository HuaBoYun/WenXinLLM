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
 * 预算文件集成实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_FILE_INTEGRATION")
public class BudgetFileIntegration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件集成ID (主键)
     */
    @TableId(value = "FILE_ID", type = IdType.ASSIGN_UUID)
    private String fileId;

    /**
     * 文件集成编码
     */
    @TableField("FILE_CODE")
    private String fileCode;

    /**
     * 文件集成名称
     */
    @TableField("FILE_NAME")
    private String fileName;

    /**
     * 文件类型 (EXCEL/CSV/XML/JSON/TXT)
     */
    @TableField("FILE_TYPE")
    private String fileType;

    /**
     * 文件格式配置 (JSON格式)
     */
    @TableField("FILE_FORMAT")
    private String fileFormat;

    /**
     * 文件路径类型 (LOCAL/FTP/SFTP/HTTP/OSS)
     */
    @TableField("PATH_TYPE")
    private String pathType;

    /**
     * 文件路径
     */
    @TableField("FILE_PATH")
    private String filePath;

    /**
     * 连接配置 (JSON格式)
     */
    @TableField("CONNECTION_CONFIG")
    private String connectionConfig;

    /**
     * 导入导出方向 (IMPORT/EXPORT/BIDIRECTIONAL)
     */
    @TableField("DIRECTION")
    private String direction;

    /**
     * 处理频率 (REALTIME/HOURLY/DAILY/WEEKLY/MONTHLY/MANUAL)
     */
    @TableField("PROCESS_FREQUENCY")
    private String processFrequency;

    /**
     * 处理时间配置 (CRON表达式)
     */
    @TableField("PROCESS_SCHEDULE")
    private String processSchedule;

    /**
     * 文件编码 (UTF-8/GBK/GB2312)
     */
    @TableField("FILE_ENCODING")
    private String fileEncoding;

    /**
     * 分隔符(CSV文件)
     */
    @TableField("DELIMITER")
    private String delimiter;

    /**
     * 是否包含表头
     */
    @TableField("HAS_HEADER")
    private Boolean hasHeader;

    /**
     * 表头行号
     */
    @TableField("HEADER_ROW")
    private Integer headerRow;

    /**
     * 数据起始行号
     */
    @TableField("DATA_START_ROW")
    private Integer dataStartRow;

    /**
     * 字段映射配置 (JSON格式)
     */
    @TableField("FIELD_MAPPING")
    private String fieldMapping;

    /**
     * 数据验证规则 (JSON格式)
     */
    @TableField("VALIDATION_RULES")
    private String validationRules;

    /**
     * 错误处理策略 (SKIP/STOP/LOG)
     */
    @TableField("ERROR_STRATEGY")
    private String errorStrategy;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 最后处理时间
     */
    @TableField("LAST_PROCESS_TIME")
    private Date lastProcessTime;

    /**
     * 下次处理时间
     */
    @TableField("NEXT_PROCESS_TIME")
    private Date nextProcessTime;

    /**
     * 处理成功次数
     */
    @TableField("SUCCESS_COUNT")
    private Integer successCount;

    /**
     * 处理失败次数
     */
    @TableField("FAILURE_COUNT")
    private Integer failureCount;

    /**
     * 最后处理记录数
     */
    @TableField("LAST_PROCESS_RECORDS")
    private Integer lastProcessRecords;

    /**
     * 集成状态 (ACTIVE/INACTIVE/ERROR/TESTING)
     */
    @TableField("INTEGRATION_STATUS")
    private String integrationStatus;

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

