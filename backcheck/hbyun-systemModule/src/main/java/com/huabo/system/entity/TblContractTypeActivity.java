package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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
@Schema(name="合同类型流程设计关联表单", description="")
@TableName(value = "TBL_CONTRACTTYPE_ACTIVITY")
public class TblContractTypeActivity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final Integer NO = 0;//未启用
	public static final Integer YES = 1; //启用
	
	@TableId(value="ACTIVITYID",type = IdType.INPUT)
	@Schema(name= "主键ID")
	private String activityId;
	
	@TableField("TYPEID")
	@Schema(name= "合同类型主键 tbl_contract_typeof(typeId)")
	private BigDecimal typeId;
	
	@TableField("TABLEID")
	@Schema(name= "系统表单主键 tbl_system_sheet(tableId)")
	private BigDecimal tableId;
	
	@TableField("ORGID")
	@Schema(name= "组织主键Id tbl_organization(orgId)")
	private BigDecimal orgId;
	
	@TableField("YMWORKFROM")
	@Schema(name= "流程主键， 业务中台base_flow(flowid)")
	private String ymWorkFrom;
	
	@TableField("FLOWTEMPLATEID")
	@Schema(name= "流程基础信息主键 业务中台work_template(fid)")
	private String flowTemplateId;
	
	@TableField("YMWORKNAME")
	@Schema(name= "流程名称")
	private String ymWorkName;
	
	@TableField("QYSTATS")
	@Schema(name= "启用状态 ，0-未启用 1-启用")
	private Integer qyStats;
	
	@TableField("VERSION")
	@Schema(name= "版本号")
	private Integer version;

	@TableField("FLOWTYPE")
	@Schema(name= "流程类型：0-标准流程 1-简单流程 2-任务流程")
	private Integer flowType;
}
