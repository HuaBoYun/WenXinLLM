package com.huabo.finance.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="财务数据源请求参数")
public class BdFinancedateVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = 1L;

	   @Schema(name = "数据源名称")
	 private String fintext;
	
	   @Schema(name = "备注")
	 private String finmemo;
	
	   @Schema(name = "源数据库类型")
	 private String financedbtype;
	
	   @Schema(name = "状态 1-启用，0-未启用，2弃用")
	 private Integer status;
	   
	   @Schema(name = "采集方案主键")
	 private String planid;
	
}
