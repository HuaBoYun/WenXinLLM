package com.global.treasurer.vo.param;

import com.global.treasurer.util.PageableParam;
import io.swagger.annotations.ApiModelProperty;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;

import java.util.List;

// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除
public class UserAllQueryParam extends PageableParam {
	private List<Long> ids;

	@ApiModelProperty("在岗状态：1-在岗闲置 2-在岗项目内 3-请假 4-外派")
	private Integer onDutyStatus;

	@ApiModelProperty(value = "真实名字")
	private String realName;

	@ApiModelProperty(value = "部门名称")
	private String applyWorkUnitName;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public List<Long> getIds() { return ids; }
    public void setIds(List<Long> ids) { this.ids = ids; }
    public Integer getOnDutyStatus() { return onDutyStatus; }
    public void setOnDutyStatus(Integer onDutyStatus) { this.onDutyStatus = onDutyStatus; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getApplyWorkUnitName() { return applyWorkUnitName; }
    public void setApplyWorkUnitName(String applyWorkUnitName) { this.applyWorkUnitName = applyWorkUnitName; }
}
