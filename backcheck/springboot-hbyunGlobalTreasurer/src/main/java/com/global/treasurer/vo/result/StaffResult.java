package com.global.treasurer.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel(value = "StaffResult")
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除
public class StaffResult {
	@ApiModelProperty(value = "用户id")
	private Long staffId;

	@ApiModelProperty(value = "真实名字")
	private String realName;

	@ApiModelProperty(value = "所属集团(列表)id")
	private Long belongGroupId;

	@ApiModelProperty(value = "所属集团(列表)名称")
	private String belongGroupName;

	@ApiModelProperty(value = "工作单位(列表)名称")
	private String workUnitName;

	@ApiModelProperty(value = "工作单位(列表)id")
	private Long workUnitId;

	@ApiModelProperty(value = "入职时间")
	private Date entryTime;

	@ApiModelProperty("部门id或者集团id")
	private Long id;

	@ApiModelProperty("部门名称或者集团名称")
	private String name;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public Long getBelongGroupId() { return belongGroupId; }
    public void setBelongGroupId(Long belongGroupId) { this.belongGroupId = belongGroupId; }
    public String getBelongGroupName() { return belongGroupName; }
    public void setBelongGroupName(String belongGroupName) { this.belongGroupName = belongGroupName; }
    public String getWorkUnitName() { return workUnitName; }
    public void setWorkUnitName(String workUnitName) { this.workUnitName = workUnitName; }
    public Long getWorkUnitId() { return workUnitId; }
    public void setWorkUnitId(Long workUnitId) { this.workUnitId = workUnitId; }
    public Date getEntryTime() { return entryTime; }
    public void setEntryTime(Date entryTime) { this.entryTime = entryTime; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
