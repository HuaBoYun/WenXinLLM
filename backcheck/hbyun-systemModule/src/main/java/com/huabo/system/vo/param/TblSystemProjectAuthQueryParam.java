package com.huabo.system.vo.param;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblSystemProjectAuthQueryParam extends PageableParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "关联项目ID，不能为空")
	@Schema(name="关联项目ID")
	private Integer projectId;

	@Schema(name="创建人ID", hidden = true)
	private Integer creator;

	@Schema(name="工作单位ID", hidden = true)
	private Integer workUnit;

	@Schema(name="所属集团ID", hidden = true)
	private Integer belongGroup;
}
