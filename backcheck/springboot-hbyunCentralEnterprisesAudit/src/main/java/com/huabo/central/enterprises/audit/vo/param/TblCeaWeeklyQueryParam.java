package com.huabo.central.enterprises.audit.vo.param;

import com.huabo.central.enterprises.audit.util.PageableParam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblCeaWeeklyQueryParam extends PageableParam {

	@Schema(name = "人员名称")
	private String staffName;

	@Schema(name = "上报部门明名称")
	private String reportWorkUnitName;

	@Schema(name="是否过滤关联 1-是 0-否")
	private Integer flagRelation = 0;

	@Schema(name="是否过滤关联-联动字段 当前周报ID")
	private Long weeklyId;

	@Schema(name="创建人",hidden=true)
	private Long creator;

	@Schema(name="工作单位",hidden=true)
	private Long workUnit;

	@Schema(name="所属集团",hidden=true)
	private Long belongGroup;

	@Schema(name="数据权限查询")
	private String deptIds;
}
