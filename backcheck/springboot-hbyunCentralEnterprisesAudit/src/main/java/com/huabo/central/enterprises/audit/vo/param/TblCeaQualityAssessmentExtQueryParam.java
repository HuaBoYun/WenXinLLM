package com.huabo.central.enterprises.audit.vo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblCeaQualityAssessmentExtQueryParam {

	@Schema(name="主键ID")
	private Long id;

	@Schema(name = "审计工质量评估主键ID")
	private Long qualityAssessmentId;

	@Schema(name="创建人",hidden=true)
	private Long creator;

	@Schema(name="工作单位",hidden=true)
	private Long workUnit;

	@Schema(name="所属集团",hidden=true)
	private Long belongGroup;
}
