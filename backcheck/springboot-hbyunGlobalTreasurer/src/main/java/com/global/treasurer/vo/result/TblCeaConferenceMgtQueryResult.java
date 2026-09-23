package com.global.treasurer.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import java.util.Date;

/**
 * 会议查询结果
 */
// @Data // 已移除,使用手动编写的getter/setter
@ApiModel(value = "TblCeaConferenceMgtQueryResult", description = "会议查询结果")
public class TblCeaConferenceMgtQueryResult {
    @ApiModelProperty(value = "会议ID")
    private String conferenceId;

    @ApiModelProperty(value = "会议主题")
    private String conferenceTheme;

    @ApiModelProperty(value = "会议时间")
    private Date conferenceTime;

    @ApiModelProperty(value = "会议地点")
    private String conferencePlace;

    @ApiModelProperty(value = "会议状态")
    private String conferenceStatus;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getConferenceId() { return conferenceId; }
    public void setConferenceId(String conferenceId) { this.conferenceId = conferenceId; }
    public String getConferenceTheme() { return conferenceTheme; }
    public void setConferenceTheme(String conferenceTheme) { this.conferenceTheme = conferenceTheme; }
    public Date getConferenceTime() { return conferenceTime; }
    public void setConferenceTime(Date conferenceTime) { this.conferenceTime = conferenceTime; }
    public String getConferencePlace() { return conferencePlace; }
    public void setConferencePlace(String conferencePlace) { this.conferencePlace = conferencePlace; }
    public String getConferenceStatus() { return conferenceStatus; }
    public void setConferenceStatus(String conferenceStatus) { this.conferenceStatus = conferenceStatus; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

}
