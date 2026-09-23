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
public class CustomizeShowExtPreviewDetailsQueryParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name="场景ID")
	private BigDecimal sceneId;

	@Schema(name = "场景唯一编码")
	private String sceneCode;

	@Schema(name="组名称")
	private String groupName;

}
