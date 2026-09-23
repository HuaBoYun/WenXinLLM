package com.huabo.legal.vo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblFwglAnnualExamineScoreExtFileParam {

	@Schema(name = "年度考核评分ID")
	private Long annualExamineScoreExtId;

	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

	@Schema(name = "关联ID-年度考核题目ID")
	private Long annualExamineTopicExtId;

	@Schema(name="创建人",hidden=true)
	private String creator;

	@Schema(name="工作单位",hidden=true)
	private String workUnit;

	@Schema(name="所属集团",hidden=true)
	private String belongGroup;
}
