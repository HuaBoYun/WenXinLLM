package com.huabo.central.enterprises.audit.vo.result;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConferencePlaceTimeResult {

	@Schema(name = "会议开始时间 格式：yyyy-MM-dd HH:mm:ss")
	private Date conferenceTimeStart;

	@Schema(name = "会议结束时间 格式：yyyy-MM-dd HH:mm:ss")
	private Date conferenceTimeEnd;

	@Schema(name = "会议地点")
	private String conferencePlace;
}
