package com.huabo.central.enterprises.audit.vo.result;

import com.huabo.central.enterprises.audit.util.excel.DictMapUtil;
import com.huabo.central.enterprises.audit.util.excel.annotation.ExcelField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAllResultExpress {

	@ExcelField(title = "用户名")
	private String userName;

	@Schema(name = "真实名字")
	@ExcelField(title = "真实姓名")
	private String realName;

	@Schema(name = "手机号码")
	@ExcelField(title = "手机号码")
	private String miblePhone;

	@Schema(name = "固定电话")
	@ExcelField(title = "固定电话")
	private String fixedPhone;

	@Schema(name = "邮箱")
	@ExcelField(title = "邮箱")
	private String email;

	@Schema(name = "所属部门")
	@ExcelField(title = "所属部门")
	private String orgname;

	@Schema(name="状态：1-在岗闲置 2-在岗项目内 3-请假 4-外派")
	@ExcelField(title = "状态",dictType =  DictMapUtil.PERSONNEL_STATUS_TYPE)
	private Integer onDutyStatus;

	@Schema(name = "备注")
	@ExcelField(title = "备注")
	private String memo;



}
