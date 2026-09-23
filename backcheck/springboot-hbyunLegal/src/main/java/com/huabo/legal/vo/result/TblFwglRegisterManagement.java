package com.huabo.legal.vo.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.legal.util.excel.DictMapUtil;
import com.huabo.legal.util.excel.annotation.ExcelField;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * 登记管理表
 */
@Schema(name="TblFwglRegisterManagement")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblFwglRegisterManagement implements Serializable {

	private static final long serialVersionUID = 1L;
	@ExcelField(title = "登记管理ID", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "登记管理ID")
	private Long registerManagementId;
	@ExcelField(title = "类别 1-商标 2-版权 3-专利", sort = 1, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.REGISTER_TYPE)
	@Schema(name = "类别 1-商标 2-版权 3-专利")
	private Integer type;
	@ExcelField(title = "名称", sort = 2, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "名称")
	private String registerName;
	//	@ExcelField(title = "类别", sort = 3, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "类别")
	private String category;
	@ExcelField(title = "注册公告日期", sort = 4, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "注册公告日期")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date registerTime;
	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;
	//	@ExcelField(title = "状态", sort = 5, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.STATE_TYPE)
	@Schema(name="状态",hidden=true)
	private Integer state;
	@Column(name = "CREATOR")
	@ExcelField(title = "创建人", sort = 5, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name="创建人",hidden=true)
	private String creator;
	@Column(name = "WORKUNIT")
	@Schema(name="工作单位",hidden=true)
	private String workUnit;
	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;
	@ExcelField(title = "创建时间", sort = 6, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;
	//	@ExcelField(title = "更新时间", sort = 7, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	public static TblFwglRegisterManagement ofId(Long id) {
		TblFwglRegisterManagement tblFwglRegisterManagementMySql = new TblFwglRegisterManagement();
		tblFwglRegisterManagementMySql.setRegisterManagementId(id);
		return tblFwglRegisterManagementMySql;
	}
}
