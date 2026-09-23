package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="流程待办信息表", description="")
@TableName(value = "TBL_FLOW_TASKINFO")
public class TblFlowTaskInfo implements Serializable {

	private static final long serialVersionUID = -5386678602885448657L;

	@TableId(value="TAKSID",type = IdType.INPUT)
	@Schema(name="主键ID,自动增长")
	private BigDecimal taksId;
	
	@TableField("TASKNO")
	@Schema(name="流程编号")
	private String taskNo;
	
	@TableField("TASKTITLE")
	@Schema(name="流程标题")
	private String taskTitle;
	
	@TableField("FROMID")
	@Schema(name="表单ID")
	private String fromId;
	
	@TableField("FLOWID")
	@Schema(name="流程平台流程主键")
	private String flowId;
	
	@TableField("PROCESSID")
	@Schema(name="processId")
	private String processId;
	
	@TableField("FLOWTASKID")
	@Schema(name="流程平台 流程任务主键")
	private String flowTaskId;
	
	@TableField("THISSTEPID")
	@Schema(name="流程平台当前审批节点主键")
	private String thisStepId;
	
	@TableField("OPERATORID")
	@Schema(name="operatorId")
	private String operatorId;
	
	@TableField("CURRENTSTAFFID")
	@Schema(name="当前办理人")
	private BigDecimal currentStaffId;
	
	@TableField("CURRENTROLE")
	@Schema(name="当前办理人角色")
	private String currenRole;
	
	@TableField("OPERATION")
	@Schema(name="操作")
	private String operation;
	
	@TableField("COMMONT")
	@Schema(name="审批意见")
	private String commont;
	
	@TableField("CREATETIME")
	@Schema(name="创建时间")
	private Date createTime;
	
	@TableField("NEXTSTAFFID")
	@Schema(name="下一步办理人")
	private BigDecimal nextStaffId;
	
	@TableField("NEXTROLE")
	@Schema(name="下一步办理人角色")
	private String nextRole;
	
	@TableField("MODULETYPE")
	@Schema(name="所属模块类型")
	private String moduleType;

	@TableField("TASKNODEID")
	@Schema(name="当前任务节点Id")
	private String taskNodeId;

	@TableField("TASKSTATUS")
	@Schema(name="当前节点状态 0-未审批，1-已审批，2-即将审批 ，3 - 已被撤回")
	private Integer taskStatus;
	
	@TableField("TASKGROUPID")
	@Schema(name="当前任务节点组")
	private Integer taskGroupId;
	
	@Transient
	private String currentName;
}
