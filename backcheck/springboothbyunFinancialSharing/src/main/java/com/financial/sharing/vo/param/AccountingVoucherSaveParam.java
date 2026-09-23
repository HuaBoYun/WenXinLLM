package com.financial.sharing.vo.param;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

/**
 * 会计凭证保存参数
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
public class AccountingVoucherSaveParam {

    /**
     * 凭证ID（更新时必填）
     */
    private Long voucherId;

    /**
     * 凭证编号
     */
    @NotBlank(message = "凭证编号不能为空")
    @Size(max = 50, message = "凭证编号长度不能超过50个字符")
    private String voucherNo;

    /**
     * 凭证类型ID
     */
    @NotNull(message = "凭证类型不能为空")
    private Long voucherTypeId;

    /**
     * 凭证日期
     */
    @NotNull(message = "凭证日期不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate voucherDate;

    /**
     * 会计期间
     */
    @NotBlank(message = "会计期间不能为空")
    @Size(max = 10, message = "会计期间长度不能超过10个字符")
    private String accountingPeriod;

    /**
     * 凭证摘要
     */
    @Size(max = 500, message = "凭证摘要长度不能超过500个字符")
    private String voucherDesc;

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
    @Size(max = 50, message = "来源系统长度不能超过50个字符")
    private String sourceSystem;

    /**
     * 制单人
     */
    private Long preparer;

    /**
     * 附件数量
     */
    private Integer attachmentCount;

    /**
     * 备注
     */
    @Size(max = 1000, message = "备注长度不能超过1000个字符")
    private String remark;

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
     * 凭证分录列表
     */
    @NotEmpty(message = "凭证分录不能为空")
    @Valid
    private List<VoucherEntrySaveParam> entries;
}
