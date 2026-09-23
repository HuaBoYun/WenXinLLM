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
 * 预算审批节点实体类
 * 
 * @author AI Agent
 * @date 2026-01-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_APPROVAL_NODE")
public class BudgetApprovalNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 节点ID (主键)
     */
    @TableId(value = "NODE_ID", type = IdType.ASSIGN_UUID)
    private String nodeId;

    /**
     * 流程ID
     */
    @TableField("FLOW_ID")
    private String flowId;

    /**
     * 节点名称
     */
    @TableField("NODE_NAME")
    private String nodeName;

    /**
     * 节点编码
     */
    @TableField("NODE_CODE")
    private String nodeCode;

    /**
     * 节点类型 (approval/review/notify)
     */
    @TableField("NODE_TYPE")
    private String nodeType;

    /**
     * 审批人列表 (JSON格式)
     */
    @TableField("APPROVERS")
    private String approvers;

    /**
     * 审批方式 (sequential/parallel/any)
     */
    @TableField("APPROVAL_MODE")
    private String approvalMode;

    /**
     * 状态 (pending/approved/rejected/skipped)
     */
    @TableField("STATUS")
    private String status;

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
     * 审批时间
     */
    @TableField("APPROVAL_TIME")
    private Date approvalTime;

    /**
     * 排序序号
     */
    @TableField("ORDER_NUM")
    private Integer orderNum;

    /**
     * 节点配置 (JSON格式)
     */
    @TableField("NODE_CONFIG")
    private String nodeConfig;

    /**
     * 超时小时数
     */
    @TableField("TIMEOUT_HOURS")
    private Integer timeoutHours;

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

