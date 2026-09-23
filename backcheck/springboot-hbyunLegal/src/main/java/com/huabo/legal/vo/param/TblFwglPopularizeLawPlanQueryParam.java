package com.huabo.legal.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.legal.util.PageableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

@Data
@Schema(name="TblFwglPopularizeLawPlanQueryParam")
@AllArgsConstructor
@NoArgsConstructor
public class TblFwglPopularizeLawPlanQueryParam extends PageableParam implements Serializable {

	private static final long serialVersionUID = 1L;
	@Schema(name = "计划名称")
	private String popularizeLawPlanName;
	@Schema(name = "年份开始时间")
	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy")
	private Date planYearBeginDate;
	@Schema(name = "年份结束时间")
	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy")
	private Date planYearEndDate;
	@Column(name = "CREATOR")
	@Schema(name="创建人",hidden=true)
	private String creator;
	@Column(name = "WORKUNIT")
	@Schema(name="工作单位",hidden=true)
	private String workUnit;
	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;
}