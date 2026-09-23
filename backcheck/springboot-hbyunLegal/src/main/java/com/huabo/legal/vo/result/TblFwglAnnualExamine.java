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
 * 年度考核表
 */
@Schema(name="TblFwglAnnualExamine")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_annual_examine")
public class TblFwglAnnualExamine implements Serializable {

	private static final long serialVersionUID = 1L;
	//	@ExcelField(title = "年度考核ID", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "年度考核ID")
	private Long annualExamineId;
	@ExcelField(title = "考核名称", sort = 1, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "考核名称")
	private String annualExamineName;
	//	@ExcelField(title = "评分事务id", sort = 2, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "评分事务id")
	private String scoreTransaction;
	@ExcelField(title = "考核类型 1-外部监管考核 2-子单位考核", sort = 3, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.ASSESS_TYPE)
	@Schema(name = "考核类型 1-外部监管考核 2-子单位考核")
	private Integer type;
	@ExcelField(title = "考核时间", sort = 4, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "考核时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date examineTime;
	@ExcelField(title = "创建人（列表）", sort = 5, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "创建人（列表）")
	private String annualExamineCreator;
	//	@ExcelField(title = "创建时间（列表）", sort = 6, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "创建时间（列表）")
	private String annualExamineCreatedTime;
	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;
	//	@ExcelField(title = "状态", sort = 7, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.STATE_TYPE)
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
	@ExcelField(title = "创建时间", sort = 8, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;
	//	@ExcelField(title = "更新时间", sort = 9, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	@Schema(name = "子单位ID")
	private Long sonCompanyId;

	@Schema(name = "总分")
	private String totalScore;

	@Schema(name = "题目事务ID")
	private String topicTransactionId;

	public static TblFwglAnnualExamine ofId(Long id) {
		TblFwglAnnualExamine tblFwglAnnualExamineMySql = new TblFwglAnnualExamine();
		tblFwglAnnualExamineMySql.setAnnualExamineId(id);
		return tblFwglAnnualExamineMySql;
	}
}
