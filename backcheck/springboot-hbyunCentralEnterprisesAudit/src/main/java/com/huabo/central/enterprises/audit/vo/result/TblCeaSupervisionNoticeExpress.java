package com.huabo.central.enterprises.audit.vo.result;

import com.huabo.central.enterprises.audit.util.excel.DictMapUtil;
import com.huabo.central.enterprises.audit.util.excel.annotation.ExcelField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblCeaSupervisionNoticeExpress {

	@ExcelField(title = "通知单编号")
	private String noticeNumber;

	@ExcelField(title = "通知单名称")
	private String noticeName;

	@ExcelField(title = "完成时间")
	private String noticeTime1;

	@ExcelField(title = "督办部门")
	private String supervisionWorkUnitName;

	@ExcelField(title = "状态", dictType = DictMapUtil.SHSTATE_TYPE)
	private Integer state;

	@ExcelField(title = "办理状态", dictType = DictMapUtil.HAND_STATUS_TYPE)
	private String handstatus;
}
