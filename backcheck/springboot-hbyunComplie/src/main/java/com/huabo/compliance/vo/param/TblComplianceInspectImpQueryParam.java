package com.huabo.compliance.vo.param;


import com.huabo.compliance.util.PageableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblComplianceInspectImpQueryParam extends PageableParam {

	@Schema(name = "问题类型")
	private String questionType;

	@Schema(name = "确认情况：0-未确认1-已确认")
	private Integer isConfirm;

	@Schema(name = "是否待整改问题：0-否 1-是")
	private Integer isRectification;

	@Schema(name = "疑似问题")
	private String suspectedIssue;

	@Schema(name = "状态")
	private Integer state;

	@Schema(name="创建人",hidden=true)
	private Integer creator;

	@Schema(name="工作单位",hidden=true)
	private Integer workUnit;

	@Schema(name="所属集团",hidden=true)
	private Integer belongGroup;

	@Schema(name="是否管理员 0-否 1-是")
	private Integer authorityType;

	@Schema(name="过滤已保存信息 0-否 1-是 默认 否")
	private Integer isRepeat;
}
