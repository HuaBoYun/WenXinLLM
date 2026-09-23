package com.financial.sharing.vo.param;

import com.financial.sharing.util.PageableParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 会计凭证查询参数
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountingVoucherQueryParam extends PageableParam {

    /**
     * 凭证编号
     */
    private String voucherNo;

    /**
     * 凭证类型ID
     */
    private Long voucherTypeId;

    /**
     * 凭证日期开始
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate voucherDateStart;

    /**
     * 凭证日期结束
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate voucherDateEnd;

    /**
     * 会计期间
     */
    private String accountingPeriod;

    /**
     * 凭证状态(0草稿1已生成2已过账3已取消)
     */
    private Integer voucherStatus;

    /**
     * 来源事项ID
     */
    private Long sourceTransactionId;

    /**
     * 来源系统
     */
    private String sourceSystem;

    /**
     * 制单人
     */
    private Long preparer;

    /**
     * 审核人
     */
    private Long reviewer;

    /**
     * 过账人
     */
    private Long poster;

    /**
     * 审核状态(0未审核1已审核)
     */
    private Integer auditStatus;

    /**
     * 审核人
     */
    private Long auditor;

    /**
     * 账簿ID
     */
    private Long bookId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 结束日期
     */
    private String endDate;
}
