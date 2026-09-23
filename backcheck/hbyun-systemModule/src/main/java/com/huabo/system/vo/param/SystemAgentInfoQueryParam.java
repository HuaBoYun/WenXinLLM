package com.huabo.system.vo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemAgentInfoQueryParam extends PageableParam {

	@Schema(name="是否下发 1-下发 0-未下发")
	private Integer flagIssued;

	@Schema(name="智能体名称")
	private String agentName;

	@Schema(name="智能体分类")
	private String agentType;

	@Schema(name="创建人ID", hidden = true)
	private Long creator;

	@Schema(name="工作单位ID", hidden = true)
	private Long workUnit;

	@Schema(name="所属集团ID", hidden = true)
	private Long belongGroup;
}
