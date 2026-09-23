package com.huabo.legal.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="LegalPersonnelCardEmploymentRateResult")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegalPersonnelCardEmploymentRateResult {

	@Schema(name="持证人数")
	private Integer positive;

	@Schema(name="非持证人数")
	private Integer reverse;
}
