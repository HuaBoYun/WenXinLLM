package com.huabo.legal.vo.param;

import com.huabo.legal.util.PageableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblFwglExamineTopicQueryParam extends PageableParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name = "考核类型 1-外部监管考核 2-外部监管考核")
	private Integer examineType;

	@Schema(name = "事务id")
	private String scoreTransaction;

	@Schema(name="创建人",hidden=true)
	private String creator;

	@Schema(name="工作单位",hidden=true)
	private String workUnit;

	@Schema(name="所属集团",hidden=true)
	private String belongGroup;
}
