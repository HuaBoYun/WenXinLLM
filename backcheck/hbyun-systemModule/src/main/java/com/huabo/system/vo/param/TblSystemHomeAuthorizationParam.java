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
public class TblSystemHomeAuthorizationParam implements Serializable {

	@NotNull(message = "系统首页配置ID不能为空")
	@Schema(name="系统首页配置ID")
	private BigDecimal homePageId;

	@Schema(name = "保存 授权数组")
	private List<BigDecimal> list;
}
