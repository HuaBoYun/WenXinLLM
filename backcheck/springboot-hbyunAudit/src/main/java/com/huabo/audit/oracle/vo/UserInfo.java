package com.huabo.audit.oracle.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

	@Schema(name="用户表主键ID")
	private Integer staffId;
	@Schema(name="用户名称")
	private String realName;

	@Schema(name="单位ID")
	private Integer WorkUnitId;
	@Schema(name="单位名称")
	private String WorkUnitName;

	@Schema(name = "所属集团ID")
	private Integer belongGroupId;
	@Schema(name = "所属集团名称")
	private String belongGroupName;
}
