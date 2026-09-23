package com.huabo.system.vo.param;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblSystemHomePageQueryParam extends PageableParam implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(name="状态 1-启动 0-禁用")
	private String state;

	@Schema(name="创建人ID", hidden = true)
	private BigDecimal creator;

	@Schema(name="工作单位ID", hidden = true)
	private BigDecimal workUnit;

	@Schema(name="所属集团ID", hidden = true)
	private BigDecimal belongGroup;
}
