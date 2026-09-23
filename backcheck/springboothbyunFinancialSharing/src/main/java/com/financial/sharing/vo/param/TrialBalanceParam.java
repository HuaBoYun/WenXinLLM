package com.financial.sharing.vo.param;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 试算平衡参数
 *
 * @author system
 * @since 2024-12-19
 */
@Data
public class TrialBalanceParam implements Serializable {

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
     * 开始期间（用于多期间试算）
     */
    private String startPeriod;

    /**
     * 结束期间（用于多期间试算）
     */
    private String endPeriod;

    /**
     * 科目类型（1资产2负债3权益4收入5费用，0表示全部）
     */
    private Integer subjectType = 0;

    /**
     * 科目级次（0表示全部级次）
     */
    private Integer subjectLevel = 0;

    /**
     * 是否包含未过账凭证
     */
    private Boolean includeUnposted = false;

    /**
     * 是否包含零余额科目
     */
    private Boolean includeZeroBalance = true;

    /**
     * 是否包含明细科目
     */
    private Boolean includeDetailSubjects = true;

    /**
     * 是否包含辅助核算
     */
    private Boolean includeAuxiliary = false;

    /**
     * 科目ID列表（用于指定特定科目）
     */
    private List<Long> subjectIds;

    /**
     * 排除科目ID列表
     */
    private List<Long> excludeSubjectIds;

    /**
     * 是否检查期初余额
     */
    private Boolean checkOpeningBalance = true;

    /**
     * 是否检查本期发生额
     */
    private Boolean checkTransactionAmount = true;

    /**
     * 是否检查期末余额
     */
    private Boolean checkClosingBalance = true;

    /**
     * 是否检查累计发生额
     */
    private Boolean checkAccumulatedAmount = false;

    /**
     * 金额精度（小数位数）
     */
    private Integer amountPrecision = 2;

    /**
     * 允许的误差范围
     */
    private Double tolerance = 0.01;

    /**
     * 是否显示平衡差异
     */
    private Boolean showDifference = true;

    /**
     * 是否显示平衡率
     */
    private Boolean showBalanceRate = true;

    /**
     * 报告格式（simple/standard/detail）
     */
    private String reportFormat = "standard";

    /**
     * 是否生成HTML报告
     */
    private Boolean generateHtmlReport = false;

    /**
     * 是否生成Excel报告
     */
    private Boolean generateExcelReport = true;

    /**
     * 是否生成PDF报告
     */
    private Boolean generatePdfReport = false;

    /**
     * 报告模板
     */
    private String reportTemplate = "default";

    /**
     * 验证级别（basic/strict/comprehensive）
     */
    private String validationLevel = "basic";

    /**
     * 是否进行连续性验证
     */
    private Boolean checkContinuity = true;

    /**
     * 是否进行勾稽关系验证
     */
    private Boolean checkCrossReference = false;

    /**
     * 是否进行逻辑性验证
     */
    private Boolean checkLogic = true;

    /**
     * 备注
     */
    private String remark;
}