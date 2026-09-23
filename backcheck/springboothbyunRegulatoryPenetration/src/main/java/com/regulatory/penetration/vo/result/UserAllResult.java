package com.regulatory.penetration.vo.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAllResult {

	private Long staffId;

	@ApiModelProperty(value = "用户名（登录名）")
	private String userName;

	@ApiModelProperty(value = "真实名字")
	private String realName;

	@ApiModelProperty(value = "手机号码")
	private String miblePhone;

	@ApiModelProperty(value = "固定电话")
	private String fixedPhone;

	@ApiModelProperty(value = "邮箱")
	private String email;

	@ApiModelProperty(value = "备注")
	private String memo;

	@ApiModelProperty(value = "所属部门")
	private String orgname;

	@ApiModelProperty("在岗状态：1-在岗闲置 2-在岗项目内 3-请假 4-外派")
	private Integer onDutyStatus;
}
