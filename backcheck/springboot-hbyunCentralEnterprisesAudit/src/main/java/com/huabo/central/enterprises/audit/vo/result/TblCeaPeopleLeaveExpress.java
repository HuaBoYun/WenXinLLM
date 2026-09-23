package com.huabo.central.enterprises.audit.vo.result;

import com.huabo.central.enterprises.audit.util.excel.annotation.ExcelField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblCeaPeopleLeaveExpress {

	@ExcelField(title = "名称")
	private String userName;

	@ExcelField(title = "工号")
	private String staffNumber;

	@ExcelField(title = "性别")
	private String staffsex;

	@ExcelField(title = "参工时间")
	private String parworkdate1;

	@ExcelField(title = "请假类型")
	private String leaveReason;

	@ExcelField(title = "请假时间")
	private String leaveTimeString;

	@ExcelField(title = "预计请假天数")
	private Integer leavedays;

	@ExcelField(title = "实际请假天数")
	private Integer actualleavedays;

	@ExcelField(title = "相差天数")
	private Integer differDays;
}
