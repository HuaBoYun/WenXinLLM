package com.huabo.contract.entity;


import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 合同相对方关联表
 */
@Data
@TableName("TBL_CONTRACT_BUDGET")
@Schema(name="TblContractBudget对象")
public class TblContractBudget {
	
	@TableId("ID")
	@Schema(name = "主键")
	private BigDecimal id;

	@TableField("CONTRACTID")
	@Schema(name = "合同id")
	private String contractId;

	@TableField("BUDGETID")
	@Schema(name = "相对方id")
	private String bugetId;
	
	@TableField("BUDGETTYPE")
	@Schema(name = "相对方类型")
	private String bugetType;

	@TableField("CONTRACTNAME")
	@Schema(name = "对应合同")
	private String contractname;

	@TableField("ORDERBY")
	@Schema(name = "排序")
	private Integer ORDERBY;

}
