package com.huabo.central.enterprises.audit.vo.result;

import com.huabo.central.enterprises.audit.util.excel.annotation.ExcelField;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportTblCeaIpInventory {

	@ExcelField(title = "编号", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	private String num;

	@ExcelField(title = "IP地址", sort = 1, column = 1, align = ExcelField.Align.CENTER, width = 3000)
	private String ipAddress;

	@ExcelField(title = "办公楼层", sort = 2, column = 2, align = ExcelField.Align.CENTER, width = 3000)
	private String officeFloor;

	@ExcelField(title = "使用人", sort = 3, column = 3, align = ExcelField.Align.CENTER, width = 3000)
	private String usePeopleName;

	@ExcelField(title = "使用单位名称", sort = 4, column = 4, align = ExcelField.Align.CENTER, width = 3000)
	private String useBelongGroupName;

	@ExcelField(title = "用途", sort = 5, column = 5, align = ExcelField.Align.CENTER, width = 3000)
	private String purpose;

	@ExcelField(title = "备注", sort = 6, column = 6, align = ExcelField.Align.CENTER, width = 3000)
	private String remark;

	@Schema(name = "使用人ID")
	private Long usePeopleId;

	@Schema(name = "使用单位ID")
	private Long useBelongGroupId;

	@Schema(name="创建人ID",hidden=true)
	private Long creator;

	@Schema(name="工作单位ID",hidden=true)
	private Long workUnit;

	@Schema(name="所属集团ID",hidden=true)
	private Long belongGroup;
}