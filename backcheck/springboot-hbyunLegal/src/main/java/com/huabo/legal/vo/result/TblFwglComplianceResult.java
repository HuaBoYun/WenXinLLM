package com.huabo.legal.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Schema(name="TblFwglComplianceResult")
@AllArgsConstructor
@NoArgsConstructor
public class TblFwglComplianceResult implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name = "所属集团名称")
	private String belongGroupName;

	@Schema(name = "所属集团名称")
	private String belongGroup;

	@Schema(name = "审查数量")
	private String reviewNumber;

}