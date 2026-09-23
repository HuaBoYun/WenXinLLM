package com.huabo.legal.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.legal.util.PageableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamineAllQueryParam extends PageableParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name = "考核开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date examineBeginDate;
	@Schema(name = "考核结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date examineEndDate;

	@Schema(name="创建人",hidden=true)
	private String creator;

	@Schema(name="工作单位",hidden=true)
	private String workUnit;

	@Schema(name="所属集团",hidden=true)
	private String belongGroup;
}
