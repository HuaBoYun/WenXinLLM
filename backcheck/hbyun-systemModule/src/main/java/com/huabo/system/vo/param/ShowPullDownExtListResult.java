package com.huabo.system.vo.param;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowPullDownExtListResult implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name="枚举")
	private String label;

	@Schema(name="枚举值")
	private String value;

	@Schema(name="附属字段")
	private String attachedField;

}
