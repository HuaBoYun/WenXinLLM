package com.financial.sharing.vo.param;

import com.financial.sharing.util.PageableParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAllQueryParam extends PageableParam {

	private List<Long> ids;

	@ApiModelProperty("在岗状态：1-在岗闲置 2-在岗项目内 3-请假 4-外派")
	private Integer onDutyStatus;

	@ApiModelProperty(value = "真实名字")
	private String realName;

	@ApiModelProperty(value = "部门名称")
	private String applyWorkUnitName;
}
