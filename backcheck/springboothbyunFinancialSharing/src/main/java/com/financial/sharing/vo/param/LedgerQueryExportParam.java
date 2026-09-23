package com.financial.sharing.vo.param;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

/**
 * 总账查询导出参数
 *
 * @author system
 * @since 2024-12-19
 */
@Data
public class LedgerQueryExportParam implements Serializable {

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
     * 开始日期
     */
    @NotNull(message = "开始日期不能为空")
    private Date startDate;

    /**
     * 结束日期
     */
    @NotNull(message = "结束日期不能为空")
    private Date endDate;

    /**
     * 科目编码（可选）
     */
    private String subjectCode;

    /**
     * 科目ID列表（可选）
     */
    private List<Long> subjectIds;

    /**
     * 科目名称（模糊搜索）
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
     * 凭证编号
     */
    private String voucherNo;

    /**
     * 凭证日期范围
     */
    private Date voucherStartDate;
    private Date voucherEndDate;

    /**
     * 摘要关键词
     */
    private String summaryKeyword;

    /**
     * 金额范围
     */
    private Double minAmount;
    private Double maxAmount;

    /**
     * 辅助核算项
     */
    private String auxiliaryType;
    private String auxiliaryValue;

    /**
     * 是否包含未过账凭证
     */
    private Boolean includeUnposted = false;

    /**
     * 是否包含已删除凭证
     */
    private Boolean includeDeleted = false;

    /**
     * 是否按科目汇总
     */
    private Boolean groupBySubject = false;

    /**
     * 是否按日期汇总
     */
    private Boolean groupByDate = false;

    /**
     * 是否按凭证汇总
     */
    private Boolean groupByVoucher = false;

    /**
     * 是否包含期初余额
     */
    private Boolean includeOpeningBalance = true;

    /**
     * 是否包含本日合计
     */
    private Boolean includeDailySummary = true;

    /**
     * 是否包含本月累计
     */
    private Boolean includeMonthlySummary = true;

    /**
     * 是否包含本年累计
     */
    private Boolean includeYearlySummary = false;

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
     * 导出模板类型
     */
    private String templateType = "standard";

    /**
     * 排序字段（date/voucherNo/subjectCode/amount）
     */
    private String sortBy = "date";

    /**
     * 排序方向（asc/desc）
     */
    private String sortOrder = "asc";

    /**
     * 每页显示行数
     */
    private Integer pageSize = 50;

    /**
     * 是否显示分页
     */
    private Boolean showPageBreak = true;

    /**
     * 是否显示公式
     */
    private Boolean showFormula = true;

    /**
     * 是否显示网格线
     */
    private Boolean showGridLines = true;

    /**
     * 金额显示格式
     */
    private String amountFormat = "amount"; // amount/rate

    /**
     * 日期显示格式
     */
    private String dateFormat = "yyyy-MM-dd";

    /**
     * 是否包含表头
     */
    private Boolean includeHeader = true;

    /**
     * 是否包含表尾
     */
    private Boolean includeFooter = true;

    /**
     * 自定义列配置
     */
    private List<String> customColumns;

    /**
     * 隐藏列配置
     */
    private List<String> hiddenColumns;
}