package com.huabo.compliance.vo.param;


import com.huabo.compliance.util.PageableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblhgglAretQueryParam extends PageableParam implements Serializable {

	@Schema(name = "问题")
	private String isuue;

	@Schema(name = "类型")
	private String type;

	@Schema(name = "业务领域")
	private String business;

	@Schema(name="创建人",hidden=true)
	private Integer creator;

	@Schema(name="工作单位",hidden=true)
	private Integer workUnit;

	@Schema(name="所属集团",hidden=true)
	private Integer belongGroup;
}
