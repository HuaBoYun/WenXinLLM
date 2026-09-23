package com.huabo.legal.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="StaffResult")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffResult {

	@Schema(name = "用户id")
	private Long staffId;

	@Schema(name = "真实名字")
	private String realName;

	@Schema(name = "所属集团(列表)id")
	private Long belongGroupId;

	@Schema(name = "所属集团(列表)名称")
	private String belongGroupName;

	@Schema(name = "工作单位(列表)名称")
	private String workUnitName;

	@Schema(name = "工作单位(列表)id")
	private Long workUnitId;


}
