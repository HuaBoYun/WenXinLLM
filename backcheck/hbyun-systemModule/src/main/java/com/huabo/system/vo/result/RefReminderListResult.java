package com.huabo.system.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefReminderListResult {

	@Schema(name="业务主键ID")
	private Long businessId;

	@Schema(name="人员ID")
	private Long staffId;

	@Schema(name="时间")
	private String businessTime;

	@Schema(name="内容")
	private String content;
}
