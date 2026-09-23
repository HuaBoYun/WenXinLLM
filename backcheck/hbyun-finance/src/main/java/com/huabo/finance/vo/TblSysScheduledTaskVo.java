package com.huabo.finance.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="财务版本信息请求参数")
public class TblSysScheduledTaskVo extends BaseVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	   @Schema(name = "筛选条件-任务名称")
	 private String taskName;
	
	   @Schema(name = "筛选条件-任务描述")
	 private String descrtiption;
	   
	   @Schema(name = "筛选条件-状态: 1启用, 0停用")
	 private Integer status;
	   
	   @Schema(name = "筛选条件-方案名称")
	    private String planName;
	   
	   private BigDecimal orgId;
	
}
