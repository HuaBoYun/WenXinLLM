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
public class TblComplianceDtyExcel implements Serializable {

	private static final long serialVersionUID = 1L;


	@Schema(name = "部门")
	private Integer department;

	@ExcelField(title = "部门", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "部门名称")
	private String departmentName;

	@ExcelField(title = "岗位名称", sort = 1, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "岗位名称")
	private String postName;

	@ExcelField(title = "合规职责", sort = 2, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "合规职责")
	private String complianceTdr;

	@ExcelField(title = "创建人", sort = 3, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "创建人名称")
	private String creatorName;

	@ExcelField(title = "创建时间", sort = 4, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	@ExcelField(title = "备注", sort = 5, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "备注")
	private String remark;

	@ExcelField(title = "审批状态", sort = 6, column = 0, align = ExcelField.Align.CENTER, width = 5000, dictType = DictMapUtil.COMPLIANCE_IM_STATE_TYPE)
	@Schema(name = "状态")
	private Integer state;

	@Schema(name = "创建人")
	private Integer creator;

	@Schema(name = "工作单位")
	private Integer workUnit;

	@Schema(name = "所属集团")
	private Integer belongGroup;

	@Schema(name = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;
}
