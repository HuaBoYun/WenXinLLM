package com.financial.sharing.vo.param;

import com.financial.sharing.util.PageableParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 事项分录查询参数
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TransactionEntryQueryParam extends PageableParam {

    /**
     * 事项ID
     */
    private Long transactionId;

    /**
     * 分录编号
     */
    private String entryNo;

    /**
     * 科目ID
     */
    private Long subjectId;

    /**
     * 币种编码
     */
    private String currencyCode;

    /**
     * 分录摘要
     */
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
