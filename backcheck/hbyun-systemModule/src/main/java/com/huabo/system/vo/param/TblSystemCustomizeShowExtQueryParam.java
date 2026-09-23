package com.huabo.system.vo.param;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TblSystemCustomizeShowExtQueryParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name="关联ID-自定义场景关联ID")
	private BigDecimal sceneId;

	@Schema(name="场景唯一编码")
	private String sceneCode;

	@Schema(name="创建人ID", hidden = true)
	private BigDecimal creator;

	@Schema(name="工作单位ID", hidden = true)
	private BigDecimal workUnit;

	@Schema(name="所属集团ID", hidden = true)
	private BigDecimal belongGroup;
}
