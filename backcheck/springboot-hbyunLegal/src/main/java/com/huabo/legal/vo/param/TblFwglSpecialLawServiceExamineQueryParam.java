package com.huabo.legal.vo.param;

import com.huabo.legal.util.PageableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@Schema(name="TblFwglSpecialLawServiceExamineQueryParam")
@AllArgsConstructor
@NoArgsConstructor
public class TblFwglSpecialLawServiceExamineQueryParam extends PageableParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name = "团队名称")
	private String teamName;

	@Schema(name = "有无合作")
	private String isCollaboration;

	@Schema(name = "服务项目")
	private String serviceProject;

	@Schema(name="创建人",hidden=true)
	private String creator;
	@Schema(name="工作单位",hidden=true)
	private String workUnit;
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;
}