package com.financial.sharing.enterpriseReport.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 报表任务流程表
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@TableName("TBL_REPORT_WORKFLOW")
@ApiModel(value = "TblReportWorkflow对象", description = "报表任务流程表")
public class TblReportWorkflow implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "流程ID")
    @TableId(value = "WORKFLOW_ID", type = IdType.ASSIGN_UUID)
    private String workflowId;

    @ApiModelProperty(value = "任务ID")
    @TableField("TASK_ID")
    private String taskId;

    @ApiModelProperty(value = "流程编码")
    @TableField("WORKFLOW_CODE")
    private String workflowCode;

    @ApiModelProperty(value = "流程名称")
    @TableField("WORKFLOW_NAME")
    private String workflowName;

    @ApiModelProperty(value = "流程类型：PREPARE(编制)/APPROVE(审批)")
    @TableField("WORKFLOW_TYPE")
    private String workflowType;

    @ApiModelProperty(value = "分支条件（JSON格式）")
    @TableField("BRANCH_CONDITION")
    private String branchCondition;

    @ApiModelProperty(value = "参与人（JSON数组）")
    @TableField("PARTICIPANTS")
    private String participants;

    @ApiModelProperty(value = "抄送人（JSON数组）")
    @TableField("CC_USERS")
    private String ccUsers;

    @ApiModelProperty(value = "消息提醒方式：IM/EMAIL")
    @TableField("MESSAGE_TYPE")
    private String messageType;

    @ApiModelProperty(value = "消息内容模板")
    @TableField("MESSAGE_CONTENT")
    private String messageContent;

    @ApiModelProperty(value = "排序号")
    @TableField("SORT_NO")
    private Integer sortNo;

    @ApiModelProperty(value = "状态：ACTIVE(启用)/INACTIVE(停用)")
    @TableField("STATUS")
    private String status;

    @ApiModelProperty(value = "租户ID")
    @TableField("TENANT_ID")
    private String tenantId;

    @ApiModelProperty(value = "创建人")
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    @TableField("UPDATE_USER")
    private String updateUser;

    @ApiModelProperty(value = "修改时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    // 非数据库字段
    @ApiModelProperty(value = "任务名称")
    @TableField(exist = false)
    private String taskName;
}

