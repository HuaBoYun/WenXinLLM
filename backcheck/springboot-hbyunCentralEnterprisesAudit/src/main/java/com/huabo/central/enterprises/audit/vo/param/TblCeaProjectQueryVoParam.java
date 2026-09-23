package com.huabo.central.enterprises.audit.vo.param;

import java.util.List;

import com.huabo.central.enterprises.audit.util.PageableParam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblCeaProjectQueryVoParam extends PageableParam {

	@Schema(name = "当用于审计工作质量评估，则把评估人ID传进来，其余场景 这个字段不要传值")
	private Long assessId;

	@Schema(name = "项目名称")
	private String projectName;

	@Schema(name="项目ID",hidden=true)
	private List<Long> ids;

	@Schema(name="创建人",hidden=true)
	private Long creator;

	@Schema(name="工作单位",hidden=true)
	private Long workUnit;

	@Schema(name="所属集团",hidden=true)
	private Long belongGroup;
}
