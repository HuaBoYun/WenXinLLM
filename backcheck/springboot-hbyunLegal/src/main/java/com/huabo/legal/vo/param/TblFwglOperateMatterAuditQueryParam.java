package com.huabo.legal.vo.param;

import com.huabo.legal.util.PageableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Schema(name="TblFwglOperateMatterAuditQueryParam")
@AllArgsConstructor
@NoArgsConstructor
public class TblFwglOperateMatterAuditQueryParam extends PageableParam implements Serializable {

	private static final long serialVersionUID = 1L;
	@Schema(name = "经营事项名称")
	private String operateMatterName;
	@Schema(name = "状态")
	private String state;
	@Schema(name="创建人",hidden=true)
	private String creator;
	@Schema(name="工作单位",hidden=true)
	private String workUnit;
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;
}