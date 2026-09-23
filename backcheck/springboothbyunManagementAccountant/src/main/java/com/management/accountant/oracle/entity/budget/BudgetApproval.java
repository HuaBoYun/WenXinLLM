package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算审批实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_APPROVAL_FLOW")
public class BudgetApproval implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "FLOW_ID", type = IdType.ASSIGN_UUID)
    private String approvalId;

    @TableField("FLOW_CODE")
    private String approvalCode;

    @TableField("BUDGET_ID")
    private String budgetId;

    @TableField("BUDGET_NAME")
    private String budgetName;

    @TableField("BUDGET_AMOUNT")
    private java.math.BigDecimal budgetAmount;

    @TableField("FLOW_TYPE")
    private String approvalType;

    @TableField("STATUS")
    private String approvalStatus;

    @TableField("CURRENT_NODE")
    private String currentNode;

    @TableField("CURRENT_NODE_NAME")
    private String currentApproverName;

    @TableField("INITIATOR")
    private String submitterId;

    @TableField("INITIATOR_NAME")
    private String submitterName;

    @TableField("INITIATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitTime;

    @TableField("APPROVAL_COMMENT")
    private String approvalComment;

    @TableField("APPROVAL_RESULT")
    private String approvalStatus2;

    @TableField("COMPLETE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date approvalTime;

    @TableField("FLOW_NAME")
    private String flowName;

    @TableField("FLOW_CONFIG")
    private String flowConfig;

    @TableField("PRIORITY")
    private String priority;

    @TableField("DUE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dueDate;

    @TableField("DESCRIPTION")
    private String remark;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @TableField("CREATOR_ID")
    private String createBy;

    @TableField("UPDATER_ID")
    private String updateBy;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("DEL_FLAG")
    private Integer delFlag;
}

