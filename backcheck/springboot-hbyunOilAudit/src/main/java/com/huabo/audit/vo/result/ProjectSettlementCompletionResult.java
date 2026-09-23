package com.huabo.audit.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSettlementCompletionResult {

	@Schema(name="建设项目投资完成情况主键")
	private Long jsxmtzwcqkid;

	@Schema(name="合同编号")
	private String htbh;

	@Schema(name="工程或费用名称")
	private String gchfymc;

	@Schema(name="实施单位")
	private String ssdw;

	@Schema(name="批复概算投资")
	private BigDecimal pfgstzje;

	@Schema(name="合同金额")
	private BigDecimal htje;

	@Schema(name = "结算金额")
	private BigDecimal jsje;

	@Schema(name = "投资节超（概算-实际完成）")
	private String tzjc;

	@Schema(name = "投资节超情况说明")
	private String tzjcqksm;
}
