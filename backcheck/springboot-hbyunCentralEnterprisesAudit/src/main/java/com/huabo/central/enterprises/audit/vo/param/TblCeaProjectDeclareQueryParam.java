package com.huabo.central.enterprises.audit.vo.param;

import java.util.List;

import com.huabo.central.enterprises.audit.util.PageableParam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblCeaProjectDeclareQueryParam extends PageableParam {

	@Schema(name = "关联ID-项目管理实施方案-名称")
	private String implementationProjectName;

	@Schema(name="过滤ID")
	private List<Long> noIds;

	@Schema(name="IDS")
	private List<Long> ids;

	@Schema(name="分组ID")
	private Long groupId;

	@Schema(name="是否分组 1-已分组 0-未分组")
	private Integer noGroup;

	@Schema(name="是否集团评优未选过的申报 1-已被申报 0-未被申报")
	private Integer noAppraising;

	@Schema(name = "状态")
	private Integer state;

	@Schema(name = "是否废弃 0-否 1-是")
	private Integer flagAbandoned;

	@Schema(name="是否按照结果排序 0-否 1-是")
	private Integer flagSort = 0;

	@Schema(name="创建人",hidden=true)
	private Long creator;

	@Schema(name="工作单位",hidden=true)
	private Long workUnit;

	@Schema(name="所属集团",hidden=true)
	private Long belongGroup;

	@Schema(name="数据权限查询")
	private String deptIds;
}
