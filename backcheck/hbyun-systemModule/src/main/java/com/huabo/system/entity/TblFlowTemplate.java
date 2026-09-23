package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import com.hbfk.entity.TblStaffUtil;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.format.annotation.DateTimeFormat;

@Table(name = "TBL_FLOW_TEMPLATE")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="流程审批意见保存模板", description="")
public class TblFlowTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(value="TEMPID",type = IdType.INPUT)
	@Schema(name="主键ID,自动增长")
	private BigDecimal tempId;
	
	@TableField("LINKSTAFF")
	@Column(name = "LINKSTAFF")
	@Schema(name="所属用户")
	private BigDecimal linkStaff;
	
	@TableField("CREATETIME")
	@Column(name = "CREATETIME")
	@Schema(name="创建时间")
	private Date createTime;
	
	@TableField("FLOWID")
	@Column(name = "FLOWID")
	@Schema(name="流程信息主键")
	private String flowId;
	
	@TableField("TASKNODEID")
	@Column(name = "TASKNODEID")
	@Schema(name="审批任务节点主键")
	private String taskNodeId;
	
	@TableField("TEMPMEMO")
	@Column(name = "TEMPMEMO")
	@Schema(name="模板内容")
	private String tempMemo;
	
	@TableField("TEMPTITLE")
	@Column(name = "TEMPTITLE")
	@Schema(name="模板标题")
	private String tempTitle;

}
