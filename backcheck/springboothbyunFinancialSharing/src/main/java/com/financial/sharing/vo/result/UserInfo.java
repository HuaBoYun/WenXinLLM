package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "UserInfo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

	@ApiModelProperty("用户表主键ID")
	private Long staffId;
	@ApiModelProperty("用户名称")
	private String realName;

	@ApiModelProperty("单位ID")
	private Long workUnitId;
	@ApiModelProperty("单位名称")
	private String workUnitName;

	@ApiModelProperty(value = "所属集团ID")
	private Long belongGroupId;
	@ApiModelProperty(value = "所属集团名称")
	private String belongGroupName;

	@ApiModelProperty("部门ID或集团ID")
	private Long orgid;

	@ApiModelProperty("部门或集团名称")
	private String name;
}
