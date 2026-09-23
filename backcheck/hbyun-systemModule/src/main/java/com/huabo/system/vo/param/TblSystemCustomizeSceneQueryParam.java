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
public class TblSystemCustomizeSceneQueryParam extends PageableParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name="所属模块,智能监控 - znjk 合同管理-htgl 内控合规 -nkhg 系统设置-xtsz 智能审计-znsj 智能分析-znfx 法务-fwgl")
	private String moduleType;

	@Schema(name="目录名称（父类ID）")
	private BigDecimal parentCatalogueName;

	@Schema(name="目录名称（目录最下级ID）")
	private BigDecimal catalogueName;

	@Schema(name="状态 0-未启用 1-启用")
	private Integer state;

	@Schema(name="场景唯一编码")
	private String sceneCode;

	@Schema(name="创建人ID", hidden = true)
	private BigDecimal creator;

	@Schema(name="工作单位ID", hidden = true)
	private BigDecimal workUnit;

	@Schema(name="所属集团ID", hidden = true)
	private BigDecimal belongGroup;

	@Schema(name="目录ID（父类ID）")
	private BigDecimal parentCatalogueId;

	@Schema(name="目录ID（目录最下级ID）")
	private BigDecimal catalogueId;
}
