package com.huabo.legal.vo.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.legal.util.excel.annotation.ExcelField;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 规范管理表
 */
@Schema(name="TblFwglPlanManagement")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_plan_management")
public class TblFwglPlanManagement implements Serializable {

	private static final long serialVersionUID = 1L;
	//	@ExcelField(title = "规划管理ID", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "规划管理ID")
	private Long planManagementId;
	@ExcelField(title = "规划类型", sort = 1, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "规划类型")
	private String type;
	//	@ExcelField(title = "规划登记类型", sort = 2, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "规划登记类型")
	private String registerType;
	@ExcelField(title = "规划名称", sort = 3, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "规划名称")
	private String planManagementName;
	@ExcelField(title = "创建人（列表）", sort = 4, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "创建人（列表）")
	private String planManagementCreator;
	//	@ExcelField(title = "创建时间（列表）", sort = 5, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "创建时间（列表）")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date planManagementCreatedTime;
	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;
	//	@ExcelField(title = "状态", sort = 6, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.STATE_TYPE)
	@Schema(name="状态",hidden=true)
	private Integer state;
	@Column(name = "CREATOR")
	@Schema(name="创建人",hidden=true)
	private String creator;
	@Column(name = "WORKUNIT")
	@Schema(name="工作单位",hidden=true)
	private String workUnit;
	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;
	@ExcelField(title = "创建时间", sort = 7, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;
	//	@ExcelField(title = "更新时间", sort = 8, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	public static TblFwglPlanManagement ofId(Long id) {
		TblFwglPlanManagement tblFwglPlanManagementMySql = new TblFwglPlanManagement();
		tblFwglPlanManagementMySql.setPlanManagementId(id);
		return tblFwglPlanManagementMySql;
	}
}
