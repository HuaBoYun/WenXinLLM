package com.huabo.legal.vo.param;

import com.huabo.legal.util.PageableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Schema(name="TblFwglPracticeExamineQueryParam")
@AllArgsConstructor
@NoArgsConstructor
public class TblFwglPracticeExamineQueryParam extends PageableParam implements Serializable {

	private static final long serialVersionUID = 1L;
	@Schema(name = "姓名")
	private String practiceExamineName;
	@Schema(name = "考核结果")
	private Integer examineGrade;
	@Schema(name = "所属集团(列表)")
	private String practiceExamineBelongGroup;
	@Schema(name="创建人",hidden=true)
	private String creator;
	@Schema(name="工作单位",hidden=true)
	private String workUnit;
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;
}