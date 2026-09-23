package com.huabo.system.vo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UniqueIdentificationParam {

	@Schema(name="唯一编码")
	private String uniqueIdentification;

	@Schema(name="token")
	private String token;
}
