package com.huabo.central.enterprises.audit.vo.param;

import java.util.List;

import com.huabo.central.enterprises.audit.util.PageableParam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAllQueryParam extends PageableParam {

	private List<Long> ids;

	@Schema(name="在岗状态：1-在岗闲置 2-在岗项目内 3-请假 4-外派")
	private Integer onDutyStatus;

	@Schema(name = "真实名字")
	private String realName;

	@Schema(name = "部门名称")
	private String applyWorkUnitName;
}
