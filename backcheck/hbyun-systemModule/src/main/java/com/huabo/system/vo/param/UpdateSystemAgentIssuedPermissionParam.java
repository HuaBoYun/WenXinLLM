package com.huabo.system.vo.param;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSystemAgentIssuedPermissionParam {

	@Schema(name="主键ID")
	private List<Long> ids;

	@NotNull
	@Schema(name="权限类型：1-公司 2-角色 3-个人")
	private Integer authorityType;

	@Schema(name="业务ID")
	private List<Long> authorityIds;

	@NotNull
	@Schema(name="是否下发 1-是 0-否")
	private Integer flagSend;
}
