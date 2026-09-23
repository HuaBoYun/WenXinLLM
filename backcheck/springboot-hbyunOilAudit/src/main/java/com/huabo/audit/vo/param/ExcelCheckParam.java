package com.huabo.audit.vo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelCheckParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "校验的工作副本名称 不能为空")
	@Schema(name="校验的工作副本名称")
	private String tableNameEn;

	@NotBlank(message = "数据库源地址 不能为空")
	@Schema(name="数据库源地址")
	private String databaseConnectionAddress;
}
