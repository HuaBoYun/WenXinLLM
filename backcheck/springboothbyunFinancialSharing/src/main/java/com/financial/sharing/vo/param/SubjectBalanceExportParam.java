package com.financial.sharing.vo.param;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 科目余额导出参数
 *
 * @author system
 * @since 2024-12-19
 */
@Data
public class SubjectBalanceExportParam implements Serializable {

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
     * 科目编码（可选，用于过滤）
     */
    private String subjectCode;

    /**
     * 科目名称（可选，用于模糊搜索）
     */
    private String subjectName;

    /**
     * 科目类型（1资产2负债3权益4收入5费用）
     */
    private Integer subjectType;

    /**
     * 科目级次
     */
    private Integer subjectLevel;

    /**
     * 上级科目ID
     */
    private Long parentSubjectId;

    /**
     * 是否包含下级科目
     */
    private Boolean includeChildren = true;

    /**
     * 是否只导出末级科目
     */
    private Boolean onlyLeaf = false;

    /**
     * 是否包含零余额科目
     */
    private Boolean includeZeroBalance = false;

    /**
     * 余额方向（1借方2贷方）
     */
    private Integer balanceDirection;

    /**
     * 科目ID列表（用于指定导出特定科目）
     */
    private List<Long> subjectIds;

    /**
     * 是否导出期初余额
     */
    private Boolean includeOpeningBalance = true;

    /**
     * 是否导出发生额
     */
    private Boolean includeTransactionAmount = true;

    /**
     * 是否导出期末余额
     */
    private Boolean includeClosingBalance = true;

    /**
     * 是否导出累计发生额
     */
    private Boolean includeAccumulatedAmount = false;

    /**
     * 导出格式（excel/pdf）
     */
    private String exportFormat = "excel";

    /**
     * 是否异步导出
     */
    private Boolean asyncExport = false;

    /**
     * 导出文件名（不包含后缀）
     */
    private String exportFileName;

    /**
     * 是否包含合计行
     */
    private Boolean includeSummaryRow = true;

    /**
     * 排序字段（subjectCode/subjectName/balance）
     */
    private String sortBy = "subjectCode";

    /**
     * 排序方向（asc/desc）
     */
    private String sortOrder = "asc";

    /**
     * 金额显示格式（amount-金额千分位/rate-比率百分比）
     */
    private String amountFormat = "amount";

    /**
     * 是否显示科目编码
     */
    private Boolean showSubjectCode = true;

    /**
     * 是否显示科目全路径
     */
    private Boolean showSubjectFullPath = false;
}