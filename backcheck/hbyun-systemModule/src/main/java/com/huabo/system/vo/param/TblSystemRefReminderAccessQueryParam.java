package com.huabo.system.vo.param;

import com.huabo.system.utils.MyPageableParam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblSystemRefReminderAccessQueryParam extends MyPageableParam {

	@Schema(name="模块路由")
	private String moduleRoute;

	@Schema(name="催办人员名称")
	private String reminderStaffName;

	@Schema(name="创建人", hidden = true)
	private Long creator;

	@Schema(name="工作单位", hidden = true)
	private Long workUnit;

	@Schema(name="所属集团", hidden = true)
	private Long belongGroup;

}
