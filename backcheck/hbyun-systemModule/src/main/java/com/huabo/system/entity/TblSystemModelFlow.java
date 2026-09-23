package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_SYSTEM_MODELFLOW")
@Schema(name="TblSystemModelFlowId", description="")
public class TblSystemModelFlow implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@TableField("MODELID")
	@Schema(name="模块主键")
	private BigDecimal modelId;
	@TableField("FLOWID")
	@Schema(name="流程主键")
	private BigDecimal flowId;
	@TableField("MODELNO")
	@Schema(name="排序")
	private Integer modelNo;
//
//	@Transient
//	private TblSystemModelFlow id;
//	@Transient
//	private TblFlow flow;
//	@Transient
//	private TblSystemModule module;

}
