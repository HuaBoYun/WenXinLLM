package com.huabo.audit.vo.param;

import com.huabo.audit.util.PageableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblAuditModelExcelQueryParam extends PageableParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name = "数据源主键ID")
	private Long dataBaseId;

	@Schema(name="创建人",hidden=true)
	private BigDecimal creator;

	@Schema(name="工作单位",hidden=true)
	private BigDecimal workUnit;

	@Schema(name="所属集团",hidden=true)
	private BigDecimal belongGroup;
}
