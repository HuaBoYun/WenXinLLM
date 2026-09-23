package com.huabo.system.vo.param;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblSystemLoginPageQueryParam extends PageableParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name="登录页名称")
	private String loginName;

	@Schema(name="状态 1-启动 0-禁用")
	private String state;

	@Schema(name="创建人ID", hidden = true)
	private Integer creator;

	@Schema(name="工作单位ID", hidden = true)
	private Integer workUnit;

	@Schema(name="所属集团ID", hidden = true)
	private Integer belongGroup;
}
