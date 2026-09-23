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
public class TblCeaSealFormQueryParam extends PageableParam {

	private List<Long> ids;

	@Schema(name = "用印名称")
	private String sealName;

	@Schema(name = "用印枚数")
	private Integer sealNum;

	@Schema(name="状态")
	private Integer state;

	@Schema(name="用印台账排序 1-是 0-否")
	private Integer orderBy = 0;

	@Schema(name="创建人",hidden=true)
	private Long creator;

	@Schema(name="工作单位",hidden=true)
	private Long workUnit;

	@Schema(name="所属集团",hidden=true)
	private Long belongGroup;
	
	@Schema(name = "id主键")
	private Long id;

	@Schema(name="数据权限查询")
	private String deptIds;
}
