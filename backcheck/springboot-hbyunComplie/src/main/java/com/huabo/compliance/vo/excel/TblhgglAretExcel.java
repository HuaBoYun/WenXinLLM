package com.huabo.compliance.vo.excel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.compliance.util.excel.DictMapUtil;
import com.huabo.compliance.util.excel.annotation.ExcelField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblhgglAretExcel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name = "主键")
	private Integer id;

	@ExcelField(title = "序号", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "序号")
	private Integer numberno;

	@ExcelField(title = "问题", sort = 1, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "问题")
	private String isuue;

	@ExcelField(title = "类型", sort = 2, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "类型")
	private String type;

	@ExcelField(title = "业务领域", sort = 3, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "业务领域")
	private String business;

	@ExcelField(title = "是否完成整改", sort = 4, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.STATE_TYPE)
	@Schema(name = "是否完成整改（是/否）")
	private Integer rectification;

	@ExcelField(title = "整改完成时间", sort = 5, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "整改完成时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date rectificationtime;

	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileids;
}
