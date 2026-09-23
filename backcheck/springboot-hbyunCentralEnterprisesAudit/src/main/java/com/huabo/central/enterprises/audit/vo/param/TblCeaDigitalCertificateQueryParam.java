package com.huabo.central.enterprises.audit.vo.param;

import com.huabo.central.enterprises.audit.util.PageableParam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblCeaDigitalCertificateQueryParam extends PageableParam {

	@Schema(name = "服务类型:新动证书、证书冻结、USBKey解锁、证书解冻、证书更新、证书注销、USBKey丢失补办、USBKey损坏补办、授权码更新")
	private String serviceType;

	@Schema(name = "用户姓名")
	private String userName;

	@Schema(name = "员工编号")
	private String staffCode;

	@Schema(name="创建人",hidden=true)
	private Long creator;

	@Schema(name="工作单位",hidden=true)
	private Long workUnit;

	@Schema(name="所属集团",hidden=true)
	private Long belongGroup;

	@Schema(name="数据权限查询")
	private String deptIds;
}
