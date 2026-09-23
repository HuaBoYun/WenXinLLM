package com.financial.sharing.vo.result;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 会计凭证返回对象
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
public class AccountingVoucherVO {

    /**
     * 凭证ID
     */
    private Long voucherId;

    /**
     * 凭证编号
     */
    private String voucherNo;

    /**
     * 凭证类型ID
     */
    private Long voucherTypeId;

    /**
     * 凭证类型名称
     */
    private String voucherTypeName;

    /**
     * 凭证日期
     */
    private LocalDate voucherDate;

    /**
     * 会计期间
     */
    private String accountingPeriod;

    /**
     * 凭证摘要
     */
    private String voucherDesc;

    /**
     * 借方金额合计
     */
    private BigDecimal totalDebit;

    /**
     * 贷方金额合计
     */
    private BigDecimal totalCredit;

    /**
     * 分录数量
     */
    private Integer entryCount;

    /**
     * 凭证状态(0草稿1已生成2已过账3已取消)
     */
    private Integer voucherStatus;

    /**
     * 凭证状态名称
     */
    private String voucherStatusName;

    /**
     * 审核状态(0未审核1已审核)
     */
    private Integer auditStatus;

    /**
     * 审核人
     */
    private Long auditor;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 来源事项ID
     */
    private Long sourceTransactionId;

    /**
     * 来源系统
     */
    private String sourceSystem;

    /**
     * 来源系统名称
     */
    private String sourceSystemName;

    /**
     * 制单人
     */
    private Long preparer;

    /**
     * 制单人姓名
     */
    private String preparerName;

    /**
     * 审核人
     */
    private Long reviewer;

    /**
     * 审核人姓名
     */
    private String reviewerName;

    /**
     * 审核时间
     */
    private LocalDateTime reviewTime;

    /**
     * 过账人
     */
    private Long poster;

    /**
     * 过账人姓名
     */
    private String posterName;

    /**
     * 过账时间
     */
    private LocalDateTime postTime;

    /**
     * 附件数量
     */
    private Integer attachmentCount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 账簿ID
     */
    private Long bookId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 更新人
     */
    private Long updater;

    /**
     * 凭证分录列表
     */
    private List<VoucherEntryVO> entries;

    /**
     * 统计数量（用于统计查询）
     */
    private Integer totalCount;
}
