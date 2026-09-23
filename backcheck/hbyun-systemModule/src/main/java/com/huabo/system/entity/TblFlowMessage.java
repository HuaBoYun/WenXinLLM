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

@Table(name = "TBL_FLOW_MESSAGE")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="流程催办 提醒 实体", description="")
public class TblFlowMessage implements Serializable {
	private static final long serialVersionUID = -210994052050147252L;
	@TableId(value="MESSAGEID",type = IdType.INPUT)
	@Schema(name="主键ID,自动增长")
	private BigDecimal messageId;
	
	@TableField("MESSAGETIEL")
	@Column(name = "MESSAGETIEL")
	@Schema(name="消息标题")
	private String messageTiel;
	
	@TableField("SPONSOR")
	@Column(name = "SPONSOR")
	@Schema(name="发起人")
	private BigDecimal sponsor;
	
	@TableField("RECIPIENT")
	@Column(name = "RECIPIENT")
	@Schema(name="接收人")
	private BigDecimal recipient;
	
	@TableField("ISREAD")
	@Column(name = "ISREAD")
	@Schema(name="是否已读 1-已读  0-未读  默认为0 为0提示")
	private Integer isRead;
	
	@TableField("CREATETIME")
	@Column(name = "CREATETIME")
	@Schema(name="创建时间")
	private Date createTime;
	
	@TableField("PROCESSID")
	@Column(name = "PROCESSID")
	@Schema(name="processId")
	private String processId;
	
	@TableField("OPERATORID")
	@Column(name = "OPERATORID")
	@Schema(name="operatorId")
	private String operatorId;
	
	@TableField("FLOWID")
	@Column(name = "FLOWID")
	@Schema(name="流程平台流程主键")
	private String flowId;
	
	@TableField("FLOWTASKID")
	@Column(name = "FLOWTASKID")
	@Schema(name="流程平台 流程任务主键")
	private String flowTaskId;
	
	@TableField("THISSTEPID")
	@Column(name = "THISSTEPID")
	@Schema(name="流程平台当前审批节点主键")
	private String thisStepId;
	
	@TableField("TASKNODEID")
	@Column(name = "TASKNODEID")
	@Schema(name="当前任务节点Id")
	private String taskNodeId;
}
