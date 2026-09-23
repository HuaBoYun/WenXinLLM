package com.regulatory.penetration.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel(value = "StaffResult")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffResult {

	@ApiModelProperty(value = "用户id")
	private Long staffId;

	@ApiModelProperty(value = "真实名字")
	private String realName;

	@ApiModelProperty(value = "所属集团(列表)id")
	private Long belongGroupId;

	@ApiModelProperty(value = "所属集团(列表)名称")
	private String belongGroupName;

	@ApiModelProperty(value = "工作单位(列表)名称")
	private String workUnitName;

	@ApiModelProperty(value = "工作单位(列表)id")
	private Long workUnitId;

	@ApiModelProperty(value = "入职时间")
	private Date entryTime;

	@ApiModelProperty("部门id或者集团id")
	private Long id;

	@ApiModelProperty("部门名称或者集团名称")
	private String name;
}
