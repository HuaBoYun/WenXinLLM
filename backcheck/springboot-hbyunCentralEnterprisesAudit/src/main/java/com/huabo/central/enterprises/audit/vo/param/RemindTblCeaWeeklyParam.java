package com.huabo.central.enterprises.audit.vo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemindTblCeaWeeklyParam {

	@Schema(name = "创建人")
	private Long creator;

	@Schema(name = "工作单位")
	private Long workUnit;

	@Schema(name = "所属集团")
	private Long belongGroup;
}
