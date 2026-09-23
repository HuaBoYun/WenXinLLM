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
 * 课题管理表
 */
@Schema(name="TblFwglSubjectManagement")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_subject_management")
public class TblFwglSubjectManagement implements Serializable {

	private static final long serialVersionUID = 1L;
//	@ExcelField(title = "课题管理ID", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "课题管理ID")
	private Long subjectManagementId;
	@ExcelField(title = "课题名称", sort = 1, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "课题名称")
	private String subjectManagementName;
	@ExcelField(title = "课题类型 1-开题报告 2-结题报告", sort = 2, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.SUBJECT_TYPE)
	@Schema(name = "课题类型 1-开题报告 2-结题报告")
	private Integer type;
	@ExcelField(title = "单位名称", sort = 3, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "单位名称")
	private String creationUnit;
//	@ExcelField(title = "登记时间", sort = 4, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "登记时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date registerTime;
//	@ExcelField(title = "课题背景和意义", sort = 5, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "课题背景和意义")
	private String background;
//	@ExcelField(title = "课题研究目标", sort = 6, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "课题研究目标")
	private String target;
//	@ExcelField(title = "课题主要内容", sort = 7, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "课题主要内容")
	private String content;
//	@ExcelField(title = "课题初步提纲", sort = 8, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "课题初步提纲")
	private String preliminaryOutline;
//	@ExcelField(title = "拟取的研究方法和手段", sort = 9, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "拟取的研究方法和手段")
	private String proposedMethod;
//	@ExcelField(title = "研究工作进度安排", sort = 10, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "研究工作进度安排")
	private String studyScheduling;
//	@ExcelField(title = "研究工作人员安排", sort = 11, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "研究工作人员安排")
	private String studyPersonnelArrange;
//	@ExcelField(title = "需总部统筹支持事项", sort = 12, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "需总部统筹支持事项")
	private String supportIssues;
//	@ExcelField(title = "课题结果", sort = 13, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "课题结果")
	private String subjectOutcome;
//	@ExcelField(title = "其他", sort = 14, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "其他")
	private String other;
	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;
//	@ExcelField(title = "状态", sort = 15, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.STATE_TYPE)
	@Schema(name="状态",hidden=true)
	private Integer state;
	@Column(name = "CREATOR")
	@ExcelField(title = "创建人", sort = 15, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="创建人",hidden=true)
	private String creator;
	@Column(name = "WORKUNIT")
	@Schema(name="工作单位",hidden=true)
	private String workUnit;
	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;
	@ExcelField(title = "创建时间", sort = 16, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;
//	@ExcelField(title = "更新时间", sort = 17, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	public static TblFwglSubjectManagement ofId(Long id) {
		TblFwglSubjectManagement tblFwglSubjectManagementMySql = new TblFwglSubjectManagement();
		tblFwglSubjectManagementMySql.setSubjectManagementId(id);
		return tblFwglSubjectManagementMySql;
	}
}
