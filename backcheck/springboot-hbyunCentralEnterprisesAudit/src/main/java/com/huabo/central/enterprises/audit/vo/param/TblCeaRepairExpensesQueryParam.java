package com.huabo.central.enterprises.audit.vo.param;

import com.huabo.central.enterprises.audit.util.PageableParam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblCeaRepairExpensesQueryParam extends PageableParam {

	@Schema(name = "单位名称")
	private String repairBelongGroupName;

	@Schema(name = "经办人名称")
	private String transactorName;

	@Schema(name="创建人",hidden=true)
	private Long creator;

	@Schema(name="工作单位",hidden=true)
	private Long workUnit;

	@Schema(name="所属集团",hidden=true)
	private Long belongGroup;

	@Schema(name="数据权限查询")
	private String deptIds;
}
