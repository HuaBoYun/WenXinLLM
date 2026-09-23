package com.huabo.system.vo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemAgentDialogueQueryParam extends PageableParam {

	@Schema(name="主键ID")
	private Long id;

	@Schema(name="智能体类型 contract/finance/risk/compliance/legal/audit")
	private String agentType;

	@Schema(name="智能体类型集合（逗号分隔，如 contract,finance,risk）。优先级低于 agentType；agentType 为空时按此 IN 过滤")
	private String agentTypes;

	@Schema(name="创建人ID", hidden = true)
	private Long creator;

	@Schema(name="工作单位ID", hidden = true)
	private Long workUnit;

	@Schema(name="所属集团ID", hidden = true)
	private Long belongGroup;
}
