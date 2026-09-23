package com.huabo.finance.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "ValidationRequest", description = "数据验证请求参数")
public class ValidationRequest {
	@Schema(name = "采集任务ID")
	private String collectionTaskId;
	
	@Schema(name = "验证类型")
	private String validationType;
	
	@Schema(name = "备注")
	private String remark;
}

