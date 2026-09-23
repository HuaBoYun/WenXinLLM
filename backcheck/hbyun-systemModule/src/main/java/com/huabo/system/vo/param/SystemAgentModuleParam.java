package com.huabo.system.vo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemAgentModuleParam extends PageableParam {

	@Schema(name="模块路由")
	private String moduleRoute;

	@Schema(name="角色ID", hidden = true)
	private Long roleId;

	@Schema(name="创建人ID", hidden = true)
	private Long creator;

	@Schema(name="工作单位ID", hidden = true)
	private Long workUnit;

	@Schema(name="所属集团ID", hidden = true)
	private Long belongGroup;
}
