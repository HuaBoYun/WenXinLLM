package com.huabo.system.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeoutEntityResult {
	
	public final static String CONTRACTSTAGEIMPLTIMEOUT = "_contractImplOut"; //合同履行落实阶段rediskey 前缀用户名

	@Schema(name="涉及表单主键")
	private Integer formId;

	@Schema(name="提醒人员")
	private Integer staffId;

	@Schema(name="提示标题")
	private String title;

	@Schema(name="提示内容")
	private String content;
	
	@Schema(name="涉及单据类型来源tbl_system_sheet_table")
	private String moduletype;


}
