package com.huabo.system.vo.param;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchUpdateCustomizeShowExtParam implements Serializable {

	@NotNull(message = "ids 主键ID数组 不能为空")
	@Schema(name="主键ID数组")
	private List<BigDecimal> ids;

	@Schema(name="状态 1-启用 0-禁用")
	private Integer status;

}
