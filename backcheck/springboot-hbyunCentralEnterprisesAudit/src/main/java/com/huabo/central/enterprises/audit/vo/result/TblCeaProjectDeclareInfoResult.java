package com.huabo.central.enterprises.audit.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblCeaProjectDeclareInfoResult {

	@Schema(name = "主键ID")
	private Long id;

	@Schema(name = "关联ID-项目管理实施方案ID")
	private Long implementationPlanId;

	@Schema(name = "关联ID-项目管理实施方案-名称")
	private String implementationProjectName;

	@Schema(name = "审批单位")
	private Long approvalBelongGroup;

	@Schema(name = "审批单位名称")
	private String approvalBelongGroupName;

	@Schema(name = "完成单位")
	private Long completeBelongGroup;

	@Schema(name = "完成单位名称")
	private String completeBelongGroupName;

	@Schema(name = "主要特点")
	private String mainfeatures;

	@Schema(name = "结果排序")
	private Integer resultSort;

	@Schema(name = "最终排序")
	private Integer finalSort;

	@Schema(name="申报项目-评审组评分")
	private List<ReviewTeamJsonResult> reviewTeamList;

	@Schema(name = "排序")
	private Long sort;
}
