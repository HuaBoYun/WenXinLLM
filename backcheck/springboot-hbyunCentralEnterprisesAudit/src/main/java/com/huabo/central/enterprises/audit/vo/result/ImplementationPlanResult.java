package com.huabo.central.enterprises.audit.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImplementationPlanResult {

	@Schema(name="项目Id")
	private Long implementationPlanId;

	@Schema(name="项目名称")
	private String implementationProjectName;

	@Schema(name = "关联ID-项目管理实施方案-主审")
	private String implementationPlanMainReviewer;

	@Schema(name = "关联ID-项目管理实施方案-项目类型")
	private String implementationProjectType;

//	@Schema(name="项目管理实施方案-审计项目类型") //
//	private String auditProjectName;
//
//	@Schema(name="项目管理实施方案-立项单位") //
//	private String projectBelongGroupName;
//
//	@Schema(name="项目管理实施方案-实施审计机构") //
//	private String implementingAuditBodies;
//
//	@Schema(name="项目管理实施方案-被审计单位") //
//	private String beAuditBelongGroupName;

	@Schema(name = "关联ID-项目管理实施方案-项目负责人ID")
	private Long implementationProjectOrderId;

	@Schema(name = "关联ID-项目管理实施方案-项目负责人名称")
	private String implementationProjectOrderName;
}
