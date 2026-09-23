package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算审批历史实体类
 *
 * @author AI Agent
 * @date 2026-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_APPROVAL_HISTORY")
public class BudgetApprovalHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 历史记录ID（主键）
     */
    @TableId(value = "HISTORY_ID", type = IdType.ASSIGN_ID)
    private String historyId;

    /**
     * 审批流程ID
     */
    @TableField("FLOW_ID")
    private String flowId;

    /**
     * 审批节点ID
     */
    @TableField("NODE_ID")
    private String nodeId;

    /**
     * 预算ID
     */
    @TableField("BUDGET_ID")
    private String budgetId;

    /**
     * 预算名称
     */
    @TableField("BUDGET_NAME")
    private String budgetName;

    /**
     * 审批动作（submit/approve/reject/withdraw/transfer）
     */
    @TableField("ACTION")
    private String action;

    /**
     * 审批结果（approved/rejected/pending）
     */
    @TableField("APPROVAL_RESULT")
    private String approvalResult;

    /**
     * 审批意见
     */
    @TableField("APPROVAL_COMMENT")
    private String approvalComment;

    /**
     * 审批人ID
     */
    @TableField("APPROVER_ID")
    private String approverId;

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
     * 审批时间
     */
    @TableField("APPROVAL_TIME")
    private Date approvalTime;

    /**
     * 节点名称
     */
    @TableField("NODE_NAME")
    private String nodeName;

    /**
     * 节点类型
     */
    @TableField("NODE_TYPE")
    private String nodeType;

    /**
     * 审批前状态
     */
    @TableField("BEFORE_STATUS")
    private String beforeStatus;

    /**
     * 审批后状态
     */
    @TableField("AFTER_STATUS")
    private String afterStatus;

    /**
     * 审批耗时（分钟）
     */
    @TableField("DURATION_MINUTES")
    private Integer durationMinutes;

    /**
     * 是否超时（0:否 1:是）
     */
    @TableField("IS_TIMEOUT")
    private Integer isTimeout;

    /**
     * 附件信息（JSON格式）
     */
    @TableField("ATTACHMENTS")
    private String attachments;

    /**
     * 审批设备信息
     */
    @TableField("DEVICE_INFO")
    private String deviceInfo;

    /**
     * IP地址
     */
    @TableField("IP_ADDRESS")
    private String ipAddress;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 创建人ID
     */
    @TableField("CREATOR_ID")
    private String creatorId;

    /**
     * 创建人姓名
     */
    @TableField("CREATOR_NAME")
    private String creatorName;

    /**
     * 公司ID
     */
    @TableField("COMPANY_ID")
    private String companyId;

    /**
     * 公司名称
     */
    @TableField("COMPANY_NAME")
    private String companyName;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;
}

