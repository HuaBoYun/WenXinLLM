package com.financial.sharing.vo.param;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 余额刷新参数
 *
 * @author system
 * @since 2024-12-19
 */
@Data
public class BalanceRefreshParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账簿ID
     */
    @NotNull(message = "账簿ID不能为空")
    private Long bookId;

    /**
     * 租户ID
     */
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    /**
     * 会计期间
     */
    @NotNull(message = "会计期间不能为空")
    private String accountingPeriod;

    /**
     * 刷新类型（full-全量, incremental-增量, smart-智能）
     */
    @NotNull(message = "刷新类型不能为空")
    private String refreshType = "smart";

    /**
     * 科目ID列表（用于特定科目刷新）
     */
    private List<Long> subjectIds;

    /**
     * 排除科目ID列表
     */
    private List<Long> excludeSubjectIds;

    /**
     * 开始日期（增量刷新使用）
     */
    private Date startDate;

    /**
     * 结束日期（增量刷新使用）
     */
    private Date endDate;

    /**
     * 上次刷新时间（智能刷新使用）
     */
    private Date lastRefreshTime;

    /**
     * 是否包含下级科目
     */
    private Boolean includeChildSubjects = true;

    /**
     * 是否只刷新末级科目
     */
    private Boolean onlyLeafSubjects = false;

    /**
     * 是否包含辅助核算
     */
    private Boolean includeAuxiliary = true;

    /**
     * 是否包含未过账凭证
     */
    private Boolean includeUnposted = false;

    /**
     * 是否包含已删除凭证
     */
    private Boolean includeDeleted = false;

    /**
     * 是否强制刷新（忽略缓存）
     */
    private Boolean forceRefresh = false;

    /**
     * 批处理大小
     */
    private Integer batchSize = 500;

    /**
     * 是否异步刷新
     */
    private Boolean asyncRefresh = true;

    /**
     * 优先级（1-10，数字越大优先级越高）
     */
    private Integer priority = 5;

    /**
     * 是否在刷新前备份数据
     */
    private Boolean backupBeforeRefresh = false;

    /**
     * 备份文件路径（可选）
     */
    private String backupPath;

    /**
     * 是否生成刷新日志
     */
    private Boolean generateLog = true;

    /**
     * 日志级别（INFO/WARN/ERROR）
     */
    private String logLevel = "INFO";

    /**
     * 是否发送刷新完成通知
     */
    private Boolean sendNotification = false;

    /**
     * 通知邮箱列表
     */
    private List<String> notificationEmails;

    /**
     * 刷新超时时间（分钟）
     */
    private Integer timeoutMinutes = 60;

    /**
     * 失败重试次数
     */
    private Integer retryCount = 3;

    /**
     * 刷新模式（realtime-实时, batch-批量, scheduled-定时）
     */
    private String refreshMode = "batch";

    /**
     * 调度表达式（定时刷新使用）
     */
    private String cronExpression;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作人
     */
    private String operator;
}