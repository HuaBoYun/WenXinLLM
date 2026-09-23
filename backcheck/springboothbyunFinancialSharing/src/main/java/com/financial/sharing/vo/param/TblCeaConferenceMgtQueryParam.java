package com.financial.sharing.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.financial.sharing.util.PageableParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblCeaConferenceMgtQueryParam extends PageableParam {

	@ApiModelProperty(value = "会议名称")
	private String conferenceName;

	@ApiModelProperty(value = "会议开始时间-开始 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date conferenceTimeStartStart;

	@ApiModelProperty(value = "会议开始时间-结束 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date conferenceTimeStartEnd;

	@ApiModelProperty(value = "会议结束时间-开始 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date conferenceTimeEndStart;

	@ApiModelProperty(value = "会议结束时间-结束 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date conferenceTimeEndEnd;

	@ApiModelProperty(value = "创建人", hidden = true)
	private Long creator;

	@ApiModelProperty(value = "工作单位", hidden = true)
	private Long workUnit;

	@ApiModelProperty(value = "所属集团", hidden = true)
	private Long belongGroup;
}
