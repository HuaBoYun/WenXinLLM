package com.financial.sharing.vo.param;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 事项分录保存参数
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
public class TransactionEntrySaveParam {

    /**
     * 分录ID（更新时必填）
     */
    private Long entryId;

    /**
     * 事项ID
     */
    @NotNull(message = "事项ID不能为空")
    private Long transactionId;

    /**
     * 分录编号
     */
    @NotBlank(message = "分录编号不能为空")
    @Size(max = 50, message = "分录编号长度不能超过50个字符")
    private String entryNo;

    /**
     * 科目ID
     */
    @NotNull(message = "科目ID不能为空")
    private Long subjectId;

    /**
     * 借方金额
     */
    @DecimalMin(value = "0", message = "借方金额不能小于0")
    @Digits(integer = 16, fraction = 2, message = "借方金额格式不正确")
    private BigDecimal debitAmount;

    /**
     * 贷方金额
     */
    @DecimalMin(value = "0", message = "贷方金额不能小于0")
    @Digits(integer = 16, fraction = 2, message = "贷方金额格式不正确")
    private BigDecimal creditAmount;

    /**
     * 币种编码
     */
    @NotBlank(message = "币种编码不能为空")
    @Size(max = 10, message = "币种编码长度不能超过10个字符")
    private String currencyCode;

    /**
     * 汇率
     */
    @DecimalMin(value = "0.000001", message = "汇率必须大于0")
    @Digits(integer = 6, fraction = 6, message = "汇率格式不正确")
    private BigDecimal exchangeRate;

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

    /**
     * 版本号
     */
    private Integer version;
}
