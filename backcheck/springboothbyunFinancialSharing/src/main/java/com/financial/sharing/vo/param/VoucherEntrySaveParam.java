package com.financial.sharing.vo.param;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 凭证分录保存参数
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
public class VoucherEntrySaveParam {

    /**
     * 分录ID（更新时必填）
     */
    private Long entryId;

    /**
     * 分录序号
     */
    @NotNull(message = "分录序号不能为空")
    private Integer entrySeq;

    /**
     * 科目ID
     */
    @NotNull(message = "科目ID不能为空")
    private Long subjectId;

    /**
     * 借方金额
     */
    @DecimalMin(value = "0", message = "借方金额不能小于0")
    private BigDecimal debitAmount;

    /**
     * 贷方金额
     */
    @DecimalMin(value = "0", message = "贷方金额不能小于0")
    private BigDecimal creditAmount;

    /**
     * 币种编码
     */
    @Size(max = 10, message = "币种编码长度不能超过10个字符")
    private String currencyCode;

    /**
     * 汇率
     */
    @DecimalMin(value = "0", message = "汇率不能小于0")
    private BigDecimal exchangeRate;

    /**
     * 原币借方金额
     */
    @DecimalMin(value = "0", message = "原币借方金额不能小于0")
    private BigDecimal originalDebit;

    /**
     * 原币贷方金额
     */
    @DecimalMin(value = "0", message = "原币贷方金额不能小于0")
    private BigDecimal originalCredit;

    /**
     * 辅助核算信息(JSON格式)
     */
    private String auxiliaryInfo;

    /**
     * 分录摘要
     */
    @Size(max = 500, message = "分录摘要长度不能超过500个字符")
    private String entryDesc;

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
}
