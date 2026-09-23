package com.global.treasurer.vo.param;

import io.swagger.annotations.ApiModelProperty;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;

// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除
public class UpdateUserOnDutyStatusParam {
	@ApiModelProperty("用户ID")
	private Long userId;

	@ApiModelProperty("在岗状态：1-在岗闲置 2-在岗项目内 3-请假 4-外派")
	private Integer onDutyStatus;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getOnDutyStatus() { return onDutyStatus; }
    public void setOnDutyStatus(Integer onDutyStatus) { this.onDutyStatus = onDutyStatus; }

}
