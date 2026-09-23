package com.huabo.audit.vo.param;

import com.huabo.audit.util.PageableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSettlementCostIntermediateQueryParam extends PageableParam {

	@Schema(name="合同编号")
	private String htbh;

	@Schema(name="工程项目造价中间表主键",hidden=true)
	private BigDecimal gcxmzjzjbid;
}
