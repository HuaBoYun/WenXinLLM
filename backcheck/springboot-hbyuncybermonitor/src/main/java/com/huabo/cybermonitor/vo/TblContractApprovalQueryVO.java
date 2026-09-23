package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 合同审批跟踪查询VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TblContractApprovalQueryVO extends BaseVo {

    /** 合同名称（模糊查询） */
    private String contractName;

    /** 审批状态（PENDING/IN_PROGRESS/APPROVED/REJECTED） */
    private String approvalStatus;

    /** 合同编号 */
    private String contractCode;

    /** 所属企业 */
    private String companyName;

    /** 申请人 */
    private String applicant;
}
