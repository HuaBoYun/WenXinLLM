package com.huabo.central.enterprises.audit.vo.result;

import com.huabo.central.enterprises.audit.util.excel.annotation.ExcelField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblCeaExpatriateApplyExpress {

	@ExcelField(title = "申请人")
	private String applyPeopleName;

	@ExcelField(title = "部门")
	private String applyWorkUnitName;

	@ExcelField(title = "申请外出时间")
	private String applyExpatriateTime1;

	@ExcelField(title = "安排返回时间")
	private String applyReturnTime1;

	@ExcelField(title = "预计外派天数")
	private Integer leavedays;

	@ExcelField(title = "实际外派天数")
	private Integer actualleavedays;
}
