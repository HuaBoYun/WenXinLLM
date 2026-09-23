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
public class GeneratingTableParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "表名称英文 不能为空")
	@Schema(name="表名称英文")
	private String tableNameEn;

	@NotBlank(message = "excel关联ID 不能为空")
	@Schema(name="excel关联ID")
	private Integer excelId;

	//--------------------------借用字段------------------------------------

	@Schema(name="异常信息",hidden=true)
	private String errorMsg;
}
