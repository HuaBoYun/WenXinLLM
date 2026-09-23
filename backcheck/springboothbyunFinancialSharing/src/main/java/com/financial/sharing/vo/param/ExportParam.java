package com.financial.sharing.vo.param;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * 导出参数封装类
 *
 * @author system
 * @since 2024-12-19
 */
@Data
public class ExportParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 导出类型（subject_balance-科目余额, ledger_query-总账查询, trial_balance-试算平衡）
     */
    @NotNull(message = "导出类型不能为空")
    private String exportType;

    /**
     * 查询参数
     */
    private Map<String, Object> queryParams;

    /**
     * 导出格式（excel-Excel文件, pdf-PDF文件）
     */
    private String exportFormat = "excel";

    /**
     * 是否异步导出
     */
    private Boolean async = false;

    /**
     * 导出文件名（不包含后缀）
     */
    private String fileName;

    /**
     * 模板类型（用于不同的导出模板）
     */
    private String templateType;

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
    private String accountingPeriod;

    /**
     * 科目ID列表（用于特定科目导出）
     */
    private String subjectIds;

    /**
     * 是否包含明细数据
     */
    private Boolean includeDetail = false;

    /**
     * 是否包含合计行
     */
    private Boolean includeSummary = true;

    /**
     * 自定义表头映射
     */
    private Map<String, String> headerMapping;

    /**
     * 数据过滤条件
     */
    private Map<String, Object> filterConditions;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方向（asc/desc）
     */
    private String sortOrder = "asc";

    /**
     * 分页大小（大数据量导出时分批处理）
     */
    private Integer batchSize = 1000;

    /**
     * 最大导出记录数限制
     */
    private Integer maxExportLimit = 50000;

    /**
     * 是否导出零余额科目
     */
    private Boolean includeZeroBalance = false;

    /**
     * 科目级别过滤
     */
    private Integer subjectLevel;

    /**
     * 科目类型过滤（1资产2负债3权益4收入5费用）
     */
    private Integer subjectType;
}