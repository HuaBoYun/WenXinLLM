package com.huabo.legal.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="UserInfo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

	@Schema(name="用户表主键ID")
	private String staffId;
	@Schema(name="用户名称")
	private String realName;

	@Schema(name="单位ID")
	private String workUnitId;
	@Schema(name="单位名称")
	private String workUnitName;

	@Schema(name = "所属集团ID")
	private String belongGroupId;
	@Schema(name = "所属集团名称")
	private String belongGroupName;
}
