package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserOnDutyStatusParam {

	@ApiModelProperty("用户ID")
	private Long userId;

	@ApiModelProperty("在岗状态：1-在岗闲置 2-在岗项目内 3-请假 4-外派")
	private Integer onDutyStatus;
}
