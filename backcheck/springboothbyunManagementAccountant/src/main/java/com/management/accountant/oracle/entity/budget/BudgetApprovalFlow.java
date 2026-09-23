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
 * 预算审批流程实体类
 * 
 * @author AI Agent
 * @date 2026-01-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_APPROVAL_FLOW")
public class BudgetApprovalFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程ID (主键)
     */
    @TableId(value = "FLOW_ID", type = IdType.ASSIGN_UUID)
    private String flowId;

    /**
     * 流程名称
     */
    @TableField("FLOW_NAME")
    private String flowName;

    /**
     * 流程编码
     */
    @TableField("FLOW_CODE")
    private String flowCode;

    /**
     * 流程类型
     */
    @TableField("FLOW_TYPE")
    private String flowType;

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
     * 当前节点ID
     */
    @TableField("CURRENT_NODE")
    private String currentNode;

    /**
     * 当前节点名称
     */
    @TableField("CURRENT_NODE_NAME")
    private String currentNodeName;

    /**
     * 状态 (pending/approved/rejected/cancelled)
     */
    @TableField("STATUS")
    private String status;

    /**
     * 发起人ID
     */
    @TableField("INITIATOR")
    private String initiator;

    /**
     * 发起人姓名
     */
    @TableField("INITIATOR_NAME")
    private String initiatorName;

    /**
     * 发起时间
     */
    @TableField("INITIATE_TIME")
    private Date initiateTime;

    /**
     * 审批结果
     */
    @TableField("APPROVAL_RESULT")
    private String approvalResult;

    /**
     * 审批意见
     */
    @TableField("APPROVAL_COMMENT")
    private String approvalComment;

    /**
     * 流程配置 (JSON格式)
     */
    @TableField("FLOW_CONFIG")
    private String flowConfig;

    /**
     * 优先级 (low/normal/high/urgent)
     */
    @TableField("PRIORITY")
    private String priority;

    /**
     * 截止日期
     */
    @TableField("DUE_DATE")
    private Date dueDate;

    /**
     * 完成时间
     */
    @TableField("COMPLETE_TIME")
    private Date completeTime;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

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
     * 更新人ID
     */
    @TableField("UPDATER_ID")
    private String updaterId;

    /**
     * 更新人姓名
     */
    @TableField("UPDATER_NAME")
    private String updaterName;

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
}

