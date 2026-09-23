package com.huabo.system.vo.param;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSystemAgentIssuedModuleParam {

	@Schema(name="主键ID")
	private List<Long> ids;

	@NotBlank
	@Schema(name="下发模块")
	private String moduleRoute;

	@NotNull
	@Schema(name="是否下发 1-是 0-否")
	private Integer flagSend;
}
