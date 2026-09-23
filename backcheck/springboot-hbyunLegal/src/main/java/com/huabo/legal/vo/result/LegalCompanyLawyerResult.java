package com.huabo.legal.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegalCompanyLawyerResult {

	@Schema(name = "数量")
	private Integer count;

	@Schema(name = "所属集团名称")
	private String belongGroupName;

	@Schema(name = "所属集团名称")
	private String belongGroup;
}
