package com.huabo.central.enterprises.audit.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAllResult {

	private Long staffId;

	@Schema(name = "用户名（登录名）")
	private String userName;

	@Schema(name = "真实名字")
	private String realName;

	@Schema(name = "手机号码")
	private String miblePhone;

	@Schema(name = "固定电话")
	private String fixedPhone;

	@Schema(name = "邮箱")
	private String email;

	@Schema(name = "备注")
	private String memo;

	@Schema(name = "所属部门")
	private String orgname;

	@Schema(name="在岗状态：1-在岗闲置 2-在岗项目内 3-请假 4-外派")
	private Integer onDutyStatus;
}
