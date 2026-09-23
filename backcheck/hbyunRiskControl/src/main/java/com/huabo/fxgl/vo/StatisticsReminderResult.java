package com.huabo.fxgl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsReminderResult {

	@Schema(name="月度评估-催办总数量")
	private Integer monthlyTotal = 0;

	@Schema(name="一体化管控措施-催办总数量")
	private Integer integratedControlMeasuresTotal = 0;

	@Schema(name="月度评估-完成数量")
	private Integer monthlyCompleteCount = 0;

	@Schema(name="一体化管控措施-完成数量")
	private Integer integratedControlMeasuresCompleteCount = 0;

	@Schema(name="月度评估-未完成数量")
	private Integer monthlyNoCompleteCount = 0;

	@Schema(name="一体化管控措施-未完成数量")
	private Integer integratedControlMeasuresNoCompleteCount = 0;

	@Schema(name="月度评估-超期数量")
	private Integer monthlyOverdueCount = 0;

	@Schema(name="一体化管控措施-超期数量")
	private Integer integratedControlMeasuresOverdueCount = 0;
}
