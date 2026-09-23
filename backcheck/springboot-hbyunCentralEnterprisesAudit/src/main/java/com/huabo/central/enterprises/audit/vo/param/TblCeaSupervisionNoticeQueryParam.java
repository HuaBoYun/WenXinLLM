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
public class TblCeaSupervisionNoticeQueryParam extends PageableParam {

	@Schema(name="IDS")
	private List<Long> ids;

	@Schema(name = "通知单编号")
	private String noticeNumber;

	@Schema(name = "通知单名称")
	private String noticeName;

	@Schema(name="创建人",hidden=true)
	private Long creator;

	@Schema(name="工作单位",hidden=true)
	private Long workUnit;

	@Schema(name="所属集团",hidden=true)
	private Long belongGroup;

	@Schema(name = "审批状态")
	private Integer state;

	@Schema(name = "经办人")
	private Long transactor;

	private Long id;

	@Schema(name="数据权限查询")
	private String deptIds;
}
