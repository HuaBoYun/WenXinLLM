package com.huabo.legal.vo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Schema(name="BbsAdminParam")
@AllArgsConstructor
@NoArgsConstructor
public class BbsAdminParam {

	@Schema(name="批量删除的ID")
	private List<String> list;

}
