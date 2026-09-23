package com.huabo.audit.vo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportSqlParam {

	@Schema(name="字段数组")
	private List<String> sql;

	@NotNull(message = "tableName 表名 不为空")
	@Schema(name="表名")
	private String tableName;

	@Schema(name="数据源ID")
	private BigDecimal dataBaseId;
}
