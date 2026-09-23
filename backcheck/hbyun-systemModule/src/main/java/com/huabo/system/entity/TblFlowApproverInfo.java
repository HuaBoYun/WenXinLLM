package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "TBL_FLOW_APPROVER_INFO")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="流程催办 提醒 实体", description="")
public class TblFlowApproverInfo implements Serializable {
	private static final long serialVersionUID = 6581833159064362647L;

	@TableId(value="APPROVERID",type = IdType.INPUT)
	@Schema(name="主键ID,自动增长")
	private BigDecimal approverId;
	
	@TableField("APPSTATUS")
	@Column(name = "APPSTATUS")
	@Schema(name="办理状态 0-待办  1-已办")
	private Integer appStatus;
	
	@TableField("FLOWID")
	@Column(name = "FLOWID")
	@Schema(name="流程主键")
	private String flowId;
	
	@TableField("PROCESSID")
	@Column(name = "PROCESSID")
	@Schema(name="进程主键")
	private String processId;
	
	@TableField("THISSTEPID")
	@Column(name = "THISSTEPID")
	@Schema(name="当前流程审批节点")
	private String thisStepId;
	
	@TableField("CREATETIME")
	@Column(name = "CREATETIME")
	@Schema(name="创建时间")
	private Date createTime;
	
	@TableField("STAFFID")
	@Column(name = "STAFFID")
	@Schema(name="加签人主键")
	private BigDecimal staffId;
	
	@TableField("DEALTIME")
	@Column(name = "DEALTIME")
	@Schema(name="加签人办理时间")
	private Date dealTime;
	
	@TableField("APPORDER")
	@Column(name = "APPORDER")
	@Schema(name="加签人办理排序")
	private Integer apporDer;
	
}
