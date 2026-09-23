package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "TBL_FLOW_INFORMINFO")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_FLOW_INFORMINFO")
@Schema(name="流程知会信息表", description="流程抄送知会信息表")
public class TblFlowInformInfo implements Serializable {

	private static final long serialVersionUID = -5386678602885448657L;

	@TableId(value="INFOID",type = IdType.INPUT)
	@Schema(name="主键ID")
	private BigDecimal infoId;
	
	@TableField("CREATETIME")
	@Schema(name="创建时间")
	private Date createTime;
	
	@TableField("CREATESTAFF")
	@Schema(name="知会发起人")
	private BigDecimal createStaff;
	
	@TableField("READTIME")
	@Schema(name="知会人阅读时间")
	private Date readTime;
	
	@TableField("INFORMSTAFFID")
	@Schema(name="知会人主键")
	private BigDecimal informStaffId;
	
	@TableField("ID")
	@Schema(name="流程任务主键")
	private String id;
	
	@TableField("FLOWID")
	@Schema(name="流程信息主键")
	private String flowId;
	
	@TableField("THISSTEPID")
	@Schema(name="流程平台当前审批节点主键")
	private String thisStepId;
	
	@TableField("FORMID")
	@Schema(name="表单信息主键")
	private String formId;
	
	@TableField("ISREAD")
	@Schema(name="是否阅读 0 未读，1-已读  默认0")
	private Integer isRead;
	
	@TableField(exist = false)
	@Schema(name="知会人员姓名")
	private String informStaffName;
	
	@TableField(exist = false)
	@Schema(name="发起人员姓名")
	private String createStaffName;
	
	@TableField(exist = false)
	@Schema(name="分页序号")
	private Integer rn;
}
