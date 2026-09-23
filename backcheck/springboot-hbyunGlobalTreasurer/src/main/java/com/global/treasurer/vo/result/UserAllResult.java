package com.global.treasurer.vo.result;

import io.swagger.annotations.ApiModelProperty;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;

// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除
public class UserAllResult {
	private Long staffId;

	@ApiModelProperty(value = "用户名（登录名）")
	private String userName;

	@ApiModelProperty(value = "真实名字")
	private String realName;

	@ApiModelProperty(value = "手机号码")
	private String miblePhone;

	@ApiModelProperty(value = "固定电话")
	private String fixedPhone;

	@ApiModelProperty(value = "邮箱")
	private String email;

	@ApiModelProperty(value = "备注")
	private String memo;

	@ApiModelProperty(value = "所属部门")
	private String orgname;

	@ApiModelProperty("在岗状态：1-在岗闲置 2-在岗项目内 3-请假 4-外派")
	private Integer onDutyStatus;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getMiblePhone() { return miblePhone; }
    public void setMiblePhone(String miblePhone) { this.miblePhone = miblePhone; }
    public String getFixedPhone() { return fixedPhone; }
    public void setFixedPhone(String fixedPhone) { this.fixedPhone = fixedPhone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }
    public String getOrgname() { return orgname; }
    public void setOrgname(String orgname) { this.orgname = orgname; }
    public Integer getOnDutyStatus() { return onDutyStatus; }
    public void setOnDutyStatus(Integer onDutyStatus) { this.onDutyStatus = onDutyStatus; }
}
