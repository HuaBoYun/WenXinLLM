package com.huabo.system.vo.param;

import com.huabo.system.utils.MyPageableParam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblSystemRefReminderQueryParam extends MyPageableParam {

	@Schema(name="ID")
	private Integer id;

	@Schema(name="模块路由")
	private String moduleRoute;

	@Schema(name="是否阅读 0-未阅读 1-已阅读")
	private Integer isRead;

	@Schema(name="创建人", hidden = true)
	private Long creator;

	@Schema(name="工作单位", hidden = true)
	private Long workUnit;

	@Schema(name="所属集团", hidden = true)
	private Long belongGroup;
}
