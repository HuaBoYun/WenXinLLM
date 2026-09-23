package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 合同审批追踪实体
 * 对应表: TBL_CONTRACT_APPROVAL_TRACK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_CONTRACT_APPROVAL_TRACK")
public class TblContractApprovalTrack {

    /**
     * 审批追踪ID
     */
    @TableId(value = "TRACK_ID", type = IdType.ASSIGN_UUID)
    private String trackId;

    /**
     * 关联合同ID
     */
    @TableField("CONTRACT_ID")
    private String contractId;

    /**
     * 审批步骤序号
     */
    @TableField("STEP_ORDER")
    private Integer stepOrder;

    /**
     * 步骤名称（如: 部门审批、法务审核、财务审核、总经理审批）
     */
    @TableField("STEP_NAME")
    private String stepName;

    /**
     * 审批人姓名
     */
    @TableField("APPROVER_NAME")
    private String approverName;

    /**
     * 审批人部门
     */
    @TableField("APPROVER_DEPT")
    private String approverDept;

    /**
     * 审批动作（APPROVE/REJECT/PENDING）
     */
    @TableField("APPROVAL_ACTION")
    private String approvalAction;

    /**
     * 审批意见
     */
    @TableField("APPROVAL_COMMENT")
    private String approvalComment;

    /**
     * 审批时间
     */
    @TableField("APPROVAL_TIME")
    private LocalDateTime approvalTime;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
