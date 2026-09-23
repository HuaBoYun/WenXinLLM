package com.huabo.compliance.vo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInfoParam {

	@Schema(name = "创建人ID")
	private Integer creator;

	@Schema(name = "创建人名称")
	private String creatorName;

	@Schema(name = "工作单位ID")
	private Integer workUnit;

	@Schema(name = "所属集团ID")
	private Integer belongGroup;

}
