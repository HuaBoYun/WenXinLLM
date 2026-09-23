package com.huabo.system.vo.param;


import com.huabo.system.utils.MyPageableParam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblSystemRefopmReminderQueryParam extends MyPageableParam {

	@Schema(name="关联ID")
	private Integer refopmId;

	@Schema(name="模块路由")
	private String moduleRoute;

	@Schema(name="催办类型：1-自动催办 2-手动催办")
	private Integer reminderId;

	@Schema(name="状态 0-未催办 1-已催办")
	private Integer state;

	@Schema(name="创建人", hidden = true)
	private Long creator;

	@Schema(name="工作单位", hidden = true)
	private Long workUnit;

	@Schema(name="所属集团", hidden = true)
	private Long belongGroup;
}
