package com.huabo.audit.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSettlementCostIntermediateResult {

	@Schema(name="工程项目造价中间表主键")
	private BigDecimal gcxmzjzjbid;

	@Schema(name="合同编号")
	private String htbh;

	@Schema(name="工程名称")
	private String gcmc;

	@Schema(name="建设单位")
	private String jsdw;

	@Schema(name="二审审查金额")
	private BigDecimal esscje;

	@Schema(name="施工单位")
	private String sgdw;
}
