package com.huabo.audit.vo.param;

import com.huabo.audit.util.PageableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSettlementCompletionQueryParam extends PageableParam {

	@Schema(name="合同编号")
	private String htbh;

}
