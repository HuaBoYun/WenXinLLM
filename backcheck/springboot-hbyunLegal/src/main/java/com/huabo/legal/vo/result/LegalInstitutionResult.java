package com.huabo.legal.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegalInstitutionResult {

	@Schema(name="集团")
	private Integer belongGroup;

	@Schema(name="集团名称")
	private String belongGroupName;

	@Schema(name="制度审核成功数量")
	private Integer institutionCount;

	@Schema(name="经营制度审核成功数量")
	private Integer manageCount;
}
