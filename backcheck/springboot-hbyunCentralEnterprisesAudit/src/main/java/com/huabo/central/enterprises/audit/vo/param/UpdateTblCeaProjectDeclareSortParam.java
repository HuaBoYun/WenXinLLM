package com.huabo.central.enterprises.audit.vo.param;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTblCeaProjectDeclareSortParam {

	@NotNull(message = "主键ID不能为空")
	@Schema(name="主键ID")
	private Long id;

	@Schema(name = "是否废弃 0-否 1-是")
	private Integer flagAbandoned;

	@Schema(name = "排序")
	private Integer resultSort;
}
