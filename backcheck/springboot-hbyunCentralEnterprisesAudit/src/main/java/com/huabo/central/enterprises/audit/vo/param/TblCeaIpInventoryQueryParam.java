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
public class TblCeaIpInventoryQueryParam extends PageableParam {

	private List<Long> ids;

	@Schema(name = "使用人名称")
	private String usePeopleName;

	@Schema(name="是否有使用人 0-否 1-是")
	private Integer flagUsePeople;

	@Schema(name="创建人",hidden=true)
	private Long creator;

	@Schema(name="工作单位",hidden=true)
	private Long workUnit;

	@Schema(name="所属集团",hidden=true)
	private Long belongGroup;
	@Schema(name="数据权限查询")
	private String deptIds;
}
