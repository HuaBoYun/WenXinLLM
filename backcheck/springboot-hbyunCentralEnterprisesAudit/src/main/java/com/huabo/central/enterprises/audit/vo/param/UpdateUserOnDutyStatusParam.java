package com.huabo.central.enterprises.audit.vo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserOnDutyStatusParam {

	@Schema(name="用户ID")
	private Long userId;

	@Schema(name="在岗状态：1-在岗闲置 2-在岗项目内 3-请假 4-外派")
	private Integer onDutyStatus;
}
