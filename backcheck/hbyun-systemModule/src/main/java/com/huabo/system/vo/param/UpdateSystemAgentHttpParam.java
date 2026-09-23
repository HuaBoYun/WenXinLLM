package com.huabo.system.vo.param;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSystemAgentHttpParam {

	@NotNull
	@Schema(name="智能体ID")
	private Long id;

	@NotBlank
	@Schema(name="智能体跳转地址")
	private String jumpAddress;
}
