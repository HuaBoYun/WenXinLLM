package com.huabo.system.vo.param;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblSystemCustomizeSceneParam implements Serializable {

	@NotNull(message = "id 主键ID 不能为空")
	@Schema(name="主键ID")
	private BigDecimal id;

	@NotNull(message = "state 状态 0-未启用 1-启用 不能为空")
	@Schema(name="状态 0-未启用 1-启用")
	private Integer state;

	private static final long serialVersionUID = 1L;
}