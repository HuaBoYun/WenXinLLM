package com.financial.sharing.oracle.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 导出任务DTO
 *
 * @author 赵工
 * @since 2025-12-07
 */
@Data
public class ExportTaskDTO {

    /**
     * 任务名称
     */
    @NotBlank(message = "任务名称不能为空")
    private String taskName;

    /**
     * 任务类型(cost_allocation/voucher_entry/financial_report)
     */
    @NotBlank(message = "任务类型不能为空")
    private String taskType;

    /**
     * 导出格式(excel/csv/pdf)
     */
    @NotBlank(message = "导出格式不能为空")
    private String exportFormat;

    /**
     * 查询条件
     */
    private Map<String, Object> queryCondition;

    /**
     * 导出配置
     */
    private Map<String, Object> exportConfig;

    /**
     * 分片大小(每批处理的记录数)
     */
    private Integer batchSize = 1000;

    /**
     * 并行度
     */
    private Integer parallelism = 1;

    /**
     * 是否包含表头
     */
    private Boolean includeHeader = true;

    /**
     * 是否压缩
     */
    private Boolean compress = false;

    /**
     * 是否分页导出
     */
    private Boolean paginate = true;

    /**
     * 文件名前缀
     */
    private String filePrefix;

    /**
     * 密码(用于压缩文件)
     */
    private String password;

    /**
     * 导出字段
     */
    private String[] exportFields;

    /**
     * 排除字段
     */
    private String[] excludeFields;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方向
     */
    private String sortDirection;

    /**
     * 时间字段
     */
    private String timeField;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 最大导出记录数限制
     */
    private Integer maxRecords;

    /**
     * 是否导出汇总行
     */
    private Boolean includeSummary = false;

    /**
     * 自定义表头映射
     */
    private Map<String, String> headerMapping;

    /**
     * 数据转换规则
     */
    private Map<String, String> dataTransformRules;

    /**
     * 过期时间(小时)
     */
    private Integer expireHours = 24;

    /**
     * 邮件通知地址
     */
    private String notificationEmail;

    /**
     * 导出完成后是否自动下载
     */
    private Boolean autoDownload = false;
}