package com.huabo.central.enterprises.audit.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewTeamJsonResult {

	@Schema(name="用户ID")
	private Long userId;

	@Schema(name="用户名称")
	private String userName;

	@Schema(name="用户评分")
	private String userGrade;

}
