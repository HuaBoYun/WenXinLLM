package com.huabo.central.enterprises.audit.vo.result;

import com.huabo.central.enterprises.audit.util.excel.DictMapUtil;
import com.huabo.central.enterprises.audit.util.excel.annotation.ExcelField;
import lombok.Data;

import java.util.Date;

@Data
public class ExportTblCeaSealForm {

	@ExcelField(title = "序号", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	private Integer serialNumber;

	@ExcelField(title = "日期", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	private Date createdTime;

	@ExcelField(title = "用印部门", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	private String sealWorkUnitName;

	@ExcelField(title = "经办人", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	private String transactorName;

	@ExcelField(title = "发往单位", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	private String sendorg;

	@ExcelField(title = "用印事由", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	private String sealReasons;

	@ExcelField(title = "用印名称", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	private String sealName;

	@ExcelField(title = "用印枚数", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	private Long sealNum;

	@ExcelField(title = "签批人", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	private String signatory;

	@ExcelField(title = "备注", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	private String remark;

	@ExcelField(title = "状态", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.SHSTATE_TYPE)
	private Integer state;
}
