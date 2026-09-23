package com.huabo.legal.vo.param;

import com.huabo.legal.util.PageableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Schema(name="TblFwglLegalAdviserQueryParam")
@AllArgsConstructor
@NoArgsConstructor
public class TblFwglLegalAdviserQueryParam extends PageableParam implements Serializable {

	private static final long serialVersionUID = 1L;
	@Schema(name = "姓名")
	private String adviserName;
	@Schema(name = "职务")
	private String position;
	@Schema(name="创建人",hidden=true)
	private String creator;
	@Schema(name="工作单位",hidden=true)
	private String workUnit;
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;
}