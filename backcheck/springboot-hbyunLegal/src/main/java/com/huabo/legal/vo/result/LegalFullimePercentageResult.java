package com.huabo.legal.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegalFullimePercentageResult {

	@Schema(name = "专职人员数量")
	private Integer fullimeCount;

	@Schema(name = "兼职人员数量")
	private Integer count;
}
