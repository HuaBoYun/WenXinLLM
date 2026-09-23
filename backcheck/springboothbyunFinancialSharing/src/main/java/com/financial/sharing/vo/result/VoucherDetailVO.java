package com.financial.sharing.vo.result;

import com.financial.sharing.oracle.entity.AccountingVoucherEntity;
import com.financial.sharing.oracle.entity.VoucherEntryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 凭证详情VO
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VoucherDetailVO extends AccountingVoucherEntity {

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 凭证分录列表
     */
    private List<VoucherEntryEntity> entries;

    /**
     * 制单人姓名
     */
    private String preparerName;

    /**
     * 审核人姓名
     */
    private String reviewerName;

    /**
     * 过账人姓名
     */
    private String posterName;

    /**
     * 创建人姓名
     */
    private String creatorName;

    /**
     * 更新人姓名
     */
    private String updaterName;

    /**
     * 是否可编辑
     */
    private Boolean canEdit;

    /**
     * 是否可删除
     */
    private Boolean canDelete;

    /**
     * 是否可提交
     */
    private Boolean canSubmit;

    /**
     * 是否可审核
     */
    private Boolean canReview;

    /**
     * 是否可审批
     */
    private Boolean canApprove;

    /**
     * 是否可过账
     */
    private Boolean canPost;

    /**
     * 是否可反过账
     */
    private Boolean canUnpost;
}