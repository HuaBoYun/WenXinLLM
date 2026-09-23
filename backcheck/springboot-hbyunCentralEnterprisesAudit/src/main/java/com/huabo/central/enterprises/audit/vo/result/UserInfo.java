package com.huabo.central.enterprises.audit.vo.result;

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
	private Long staffId;
	@Schema(name="用户名称")
	private String realName;

	@Schema(name="单位ID")
	private Long workUnitId;
	@Schema(name="单位名称")
	private String workUnitName;

	@Schema(name = "所属集团ID")
	private Long belongGroupId;
	@Schema(name = "所属集团名称")
	private String belongGroupName;

	@Schema(name="部门ID或集团ID")
	private Long orgid;

	@Schema(name="部门或集团名称")
	private String name;
}
