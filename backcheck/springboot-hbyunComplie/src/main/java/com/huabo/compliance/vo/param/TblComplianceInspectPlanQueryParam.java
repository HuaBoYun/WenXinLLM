package com.huabo.compliance.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.compliance.util.PageableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblComplianceInspectPlanQueryParam extends PageableParam {

	@Schema(name = "方案编号")
	private String planCode;

	@Schema(name = "方案名称")
	private String planName;

	@Schema(name = "方案年度-开始")
	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy")
	private Date planYearStart;

	@Schema(name = "方案年度-开始")
	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy")
	private Date planYearEnd;

	@Schema(name="创建人",hidden=true)
	private Integer creator;

	@Schema(name="工作单位",hidden=true)
	private Integer workUnit;

	@Schema(name="所属集团",hidden=true)
	private Integer belongGroup;

	@Schema(name="是否管理员 0-否 1-是")
	private Integer authorityType;

	@Schema(name="状态")
	private Integer status;

	@Schema(name = "方案开始时间-开始 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date planTimeStartStart;

	@Schema(name = "方案开始时间-结束 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date planTimeStartEnd;

	@Schema(name = "方案结束时间-结束 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date planTimeEndStart;

	@Schema(name = "方案结束时间-结束 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date planTimeEndEnd;
}
