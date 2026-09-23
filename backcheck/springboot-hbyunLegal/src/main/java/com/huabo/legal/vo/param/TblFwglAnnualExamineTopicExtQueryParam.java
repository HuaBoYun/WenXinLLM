package com.huabo.legal.vo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Schema(name="TblFwglAnnualExamineTopicExtQueryParam")
@AllArgsConstructor
@NoArgsConstructor
public class TblFwglAnnualExamineTopicExtQueryParam implements Serializable {

	private static final long serialVersionUID = 1L;
	@Schema(name = "考核类型 1-外部监管考核 2-子单位考核")
	private Integer examineType;
	@Schema(name="创建人",hidden=true)
	private String creator;
	@Schema(name="工作单位",hidden=true)
	private String workUnit;
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;
}