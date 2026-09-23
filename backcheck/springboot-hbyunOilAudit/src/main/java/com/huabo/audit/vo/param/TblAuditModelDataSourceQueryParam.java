package com.huabo.audit.vo.param;

import com.huabo.audit.util.PageableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblAuditModelDataSourceQueryParam extends PageableParam implements Serializable {

	private static final long serialVersionUID = 1L;

//	@NotNull(message = "创建类型 1-数据源管理 2-excel导入默认导入  不能为空")
	@Schema(name = "创建类型 1-数据源管理 2-excel导入默认导入")
	private Integer createType;

	@Schema(name="创建人",hidden=true)
	private Integer creator;

	@Schema(name="工作单位",hidden=true)
	private Integer workUnit;

	@Schema(name="所属集团",hidden=true)
	private Integer belongGroup;
}
