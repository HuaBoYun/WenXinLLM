package com.global.treasurer.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.global.treasurer.util.PageableParam;
import io.swagger.annotations.ApiModelProperty;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除
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

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getConferenceName() { return conferenceName; }
    public void setConferenceName(String conferenceName) { this.conferenceName = conferenceName; }
    public Date getConferenceTimeStartStart() { return conferenceTimeStartStart; }
    public void setConferenceTimeStartStart(Date conferenceTimeStartStart) { this.conferenceTimeStartStart = conferenceTimeStartStart; }
    public Date getConferenceTimeStartEnd() { return conferenceTimeStartEnd; }
    public void setConferenceTimeStartEnd(Date conferenceTimeStartEnd) { this.conferenceTimeStartEnd = conferenceTimeStartEnd; }
    public Date getConferenceTimeEndStart() { return conferenceTimeEndStart; }
    public void setConferenceTimeEndStart(Date conferenceTimeEndStart) { this.conferenceTimeEndStart = conferenceTimeEndStart; }
    public Date getConferenceTimeEndEnd() { return conferenceTimeEndEnd; }
    public void setConferenceTimeEndEnd(Date conferenceTimeEndEnd) { this.conferenceTimeEndEnd = conferenceTimeEndEnd; }
    public Long getCreator() { return creator; }
    public void setCreator(Long creator) { this.creator = creator; }
    public Long getWorkUnit() { return workUnit; }
    public void setWorkUnit(Long workUnit) { this.workUnit = workUnit; }
    public Long getBelongGroup() { return belongGroup; }
    public void setBelongGroup(Long belongGroup) { this.belongGroup = belongGroup; }

}
