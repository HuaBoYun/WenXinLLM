package com.huabo.legal.vo.param;

import com.huabo.legal.util.PageableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Schema(name="TblFwglInstitutionAuditQueryParam")
@AllArgsConstructor
@NoArgsConstructor
public class TblFwglInstitutionAuditQueryParam extends PageableParam implements Serializable {

	private static final long serialVersionUID = 1L;
	@Schema(name = "名称")
	private String institutionName;
//	@Schema(name = "类型")
//	private Integer type;
	@Schema(name = "状态")
	private String state;
	@Schema(name = "审核类型 1-制度审核 2-经营事项审核 全量为忽略该字段")
	private Integer auditType;
	@Schema(name="创建人",hidden=true)
	private String creator;
	@Schema(name="工作单位",hidden=true)
	private String workUnit;
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;
}