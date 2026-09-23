package com.global.treasurer.vo.result;

import io.swagger.annotations.ApiModelProperty;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

import java.util.Date;

// @Data
// @AllArgsConstructor
// @NoArgsConstructor
public class ConferencePlaceTimeResult {
	@ApiModelProperty(value = "会议开始时间 格式：yyyy-MM-dd HH:mm:ss")
	private Date conferenceTimeStart;

	@ApiModelProperty(value = "会议结束时间 格式：yyyy-MM-dd HH:mm:ss")
	private Date conferenceTimeEnd;

	@ApiModelProperty(value = "会议地点")
	private String conferencePlace;

    // 完整的getter和setter方法
    public Date getConferenceTimeStart() { return conferenceTimeStart; }
    public void setConferenceTimeStart(Date conferenceTimeStart) { this.conferenceTimeStart = conferenceTimeStart; }
    public Date getConferenceTimeEnd() { return conferenceTimeEnd; }
    public void setConferenceTimeEnd(Date conferenceTimeEnd) { this.conferenceTimeEnd = conferenceTimeEnd; }
    public String getConferencePlace() { return conferencePlace; }
    public void setConferencePlace(String conferencePlace) { this.conferencePlace = conferencePlace; }
}
