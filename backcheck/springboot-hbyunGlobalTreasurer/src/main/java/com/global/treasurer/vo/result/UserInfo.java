package com.global.treasurer.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;

@ApiModel(value = "UserInfo")
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除
public class UserInfo {
	@ApiModelProperty("用户表主键ID")
	private Long staffId;
	@ApiModelProperty("用户名称")
	private String realName;

	@ApiModelProperty("单位ID")
	private Long workUnitId;
	@ApiModelProperty("单位名称")
	private String workUnitName;

	@ApiModelProperty(value = "所属集团ID")
	private Long belongGroupId;
	@ApiModelProperty(value = "所属集团名称")
	private String belongGroupName;

	@ApiModelProperty("部门ID或集团ID")
	private Long orgid;

	@ApiModelProperty("部门或集团名称")
	private String name;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public Long getWorkUnitId() { return workUnitId; }
    public void setWorkUnitId(Long workUnitId) { this.workUnitId = workUnitId; }
    public String getWorkUnitName() { return workUnitName; }
    public void setWorkUnitName(String workUnitName) { this.workUnitName = workUnitName; }
    public Long getBelongGroupId() { return belongGroupId; }
    public void setBelongGroupId(Long belongGroupId) { this.belongGroupId = belongGroupId; }
    public String getBelongGroupName() { return belongGroupName; }
    public void setBelongGroupName(String belongGroupName) { this.belongGroupName = belongGroupName; }
    public Long getOrgid() { return orgid; }
    public void setOrgid(Long orgid) { this.orgid = orgid; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

}
