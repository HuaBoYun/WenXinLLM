package com.huabo.legal.vo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeadquartersLegalQueryParam {

	@NotBlank(message = "role 角色名称 不能为空")
	@Schema(name="角色名称 如：法务人员")
	private String role;
}
