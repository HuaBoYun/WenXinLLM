package com.huabo.compliance.vo.param;

import com.huabo.compliance.util.PageableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblComplianceRectificationQueryParam extends PageableParam {

	@Schema(name = "整改状态：0-未完成 1-已完成")
	private Integer rectificationState;

	@Schema(name="创建人",hidden=true)
	private Integer creator;

	@Schema(name="工作单位",hidden=true)
	private Integer workUnit;

	@Schema(name="所属集团",hidden=true)
	private Integer belongGroup;

	@Schema(name="是否管理员 0-否 1-是")
	private Integer authorityType;
}
