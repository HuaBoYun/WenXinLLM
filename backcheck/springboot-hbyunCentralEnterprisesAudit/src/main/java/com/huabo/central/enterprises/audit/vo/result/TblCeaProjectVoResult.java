package com.huabo.central.enterprises.audit.vo.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblCeaProjectVoResult {

	@Schema(name="主键ID")
	private Long id;

	@Schema(name = "计划项目ID")
	private Long planProjectId;

	@Schema(name = "项目名称")
	private String projectName;

	@Schema(name = "项目类别")
	private String projectType;

	@Schema(name = "计划名称")
	private String planName;

	@Schema(name = "计划项目名称")
	private String planProjectName;

	@Schema(name = "项目负责人")
	private String projectOrderName;

	@Schema(name = "计划年度")
	private Long planYear;

	@Schema(name = "主审")
	private String zsname;

	@Schema(name = "助审")
	private String fzname;

	@Schema(name = "小组组长ID")
	private Long teamLeaderId;

	@Schema(name = "小组组长名称")
	private String teamLeaderName;

	@Schema(name = "副组长ID")
	private Long fzzstaffid;

	@Schema(name = "副组长ID")
	private String fzzstaffName;

	@Schema(name="组员IDS,多个用逗号隔开")
	private String teamMembersIds;

	@Schema(name="组员数组")
	private List<UserInfo> teamMembersList;

	@Schema(name = "编号")
	private String qdcode;

	@Schema(name="审计类型名称")
	private String sjlxName;

	@Schema(name="项目计划起始时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date planStarttime;

	@Schema(name="项目计划结束时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date planEndtime;
}
