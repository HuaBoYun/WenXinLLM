package com.financial.sharing.budgetPlanning.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算数据审批记录实体类
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Data
@TableName("TBL_BUDGET_APPROVAL")
public class TblBudgetApproval implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 审批ID
     */
    @TableId("APPROVAL_ID")
    private String approvalId;

    /**
     * 数据ID
     */
    @TableField("DATA_ID")
    private String dataId;

    /**
     * 审批流程ID
     */
    @TableField("WORKFLOW_ID")
    private String workflowId;

    /**
     * 审批节点
     */
    @TableField("NODE_CODE")
    private String nodeCode;

    /**
     * 审批节点名称
     */
    @TableField("NODE_NAME")
    private String nodeName;

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
     * 审批状态(PENDING-待审批/APPROVED-已通过/REJECTED-已驳回/CANCELLED-已撤销)
     */
    @TableField("APPROVAL_STATUS")
    private String approvalStatus;

    /**
     * 审批意见
     */
    @TableField("APPROVAL_COMMENT")
    private String approvalComment;

    /**
     * 审批时间
     */
    @TableField("APPROVAL_TIME")
    private Date approvalTime;

    /**
     * 租户ID
     */
    @TableField("ORG_ID")
    private String orgId;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 修改人
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 修改时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 提交人姓名(非数据库字段,用于关联查询)
     */
    @TableField(exist = false)
    private String submitUserName;

    /**
     * 预算期间(非数据库字段,用于关联查询)
     */
    @TableField(exist = false)
    private String period;

    /**
     * 预算版本(非数据库字段,用于关联查询)
     */
    @TableField(exist = false)
    private String version;

    /**
     * 模型名称(非数据库字段,用于关联查询)
     */
    @TableField(exist = false)
    private String modelName;
}

