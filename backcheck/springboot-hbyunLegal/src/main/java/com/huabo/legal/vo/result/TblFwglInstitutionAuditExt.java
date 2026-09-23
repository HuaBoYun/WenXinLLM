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
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 制度审核-制度表
 */
@Schema(name="TblFwglInstitutionAuditExt")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_institution_audit_ext")
public class TblFwglInstitutionAuditExt implements Serializable {

	private static final long serialVersionUID = 1L;
	@ExcelField(title = "制度审核-制度ID", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "制度审核-制度ID")
	private Long institutionAuditExtId;
	@ExcelField(title = "制度名称", sort = 1, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "制度名称")
	private String institutionName;
	@ExcelField(title = "制度分类（制度审核） 1-经营管理类-一般制度 2-经营管理类-基本制度 3-经营管理类-重要制度 4-非经营管理类", sort = 2, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.INSTITUTION_TYPE)
	@Schema(name = "制度分类（制度审核） 1-经营管理类-一般制度 2-经营管理类-基本制度 3-经营管理类-重要制度 4-非经营管理类")
	private Integer institutionType;
	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;
	@ExcelField(title = "状态", sort = 3, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.STATE_TYPE)
	@Schema(name="状态",hidden=true)
	private Integer state;
	@ExcelField(title = "创建人", sort = 4, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name="创建人",hidden=true)
	private String creator;
	@ExcelField(title = "工作单位", sort = 5, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name="工作单位",hidden=true)
	private String workUnit;
	@ExcelField(title = "所属集团", sort = 6, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;
	@ExcelField(title = "创建时间", sort = 7, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;
	@ExcelField(title = "更新时间", sort = 8, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;
	@ExcelField(title = "备注", sort = 9, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "备注")
	private String remark;

	public static TblFwglInstitutionAuditExt ofId(Long id) {
		TblFwglInstitutionAuditExt tblFwglInstitutionAuditExtMySql = new TblFwglInstitutionAuditExt();
		tblFwglInstitutionAuditExtMySql.setInstitutionAuditExtId(id);
		return tblFwglInstitutionAuditExtMySql;
	}
}
