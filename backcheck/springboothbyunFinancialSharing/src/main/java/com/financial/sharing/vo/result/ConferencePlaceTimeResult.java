package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConferencePlaceTimeResult {

	@ApiModelProperty(value = "会议开始时间 格式：yyyy-MM-dd HH:mm:ss")
	private Date conferenceTimeStart;

	@ApiModelProperty(value = "会议结束时间 格式：yyyy-MM-dd HH:mm:ss")
	private Date conferenceTimeEnd;

	@ApiModelProperty(value = "会议地点")
	private String conferencePlace;
}
