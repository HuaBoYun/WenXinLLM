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
public class TblSystemProjectQueryParam implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(name="唯一标识")
	private String uniqueIdentification;

	@Schema(name="名称")
	private String projectName;

	@Schema(name="创建人ID", hidden = true)
	private BigDecimal creator;

	@Schema(name="创建人名称", hidden = true)
	private String creatorName;

	@Schema(name="工作单位ID", hidden = true)
	private BigDecimal workUnit;

	@Schema(name="所属集团ID", hidden = true)
	private BigDecimal belongGroup;
	
	@Schema(name="用户角色")
	private String roleIdStrs;


}
