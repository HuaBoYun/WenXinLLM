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
 * 活动管理表
 */
@Schema(name="TblFwglActivityManagement")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_activity_management")
public class TblFwglActivityManagement implements Serializable {

	private static final long serialVersionUID = 1L;
//	@ExcelField(title = "活动管理ID", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "活动管理ID")
	private Long activityManagementId;
	@ExcelField(title = "单位名称", sort = 1, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "单位名称")
	private String unitName;
	@ExcelField(title = "登记时间", sort = 2, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "登记时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date registerTime;
//	@ExcelField(title = "计划外", sort = 3, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "计划外")
	private String unplanned;
//	@ExcelField(title = "计划内", sort = 4, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "计划内")
	private String planned;
	@ExcelField(title = "活动主题", sort = 5, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "活动主题")
	private String activityTopic;
//	@ExcelField(title = "活动内容简介", sort = 6, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "活动内容简介")
	private String activityContent;
	@ExcelField(title = "创建人", sort = 7, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "创建人")
	private String activityManagementCreator;
//	@ExcelField(title = "创建时间", sort = 8, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date activityManagementCreatedTime;
//	@ExcelField(title = "子公司公众号链接", sort = 9, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "子公司公众号链接")
	private String subsidiaryOfficialAccountLink;
//	@ExcelField(title = "公司官网链接", sort = 10, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "公司官网链接")
	private String firmWebsiteLink;
//	@ExcelField(title = "现场照片", sort = 11, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "上传文件ids 多个逗号隔开(现场照片)")
	private String sitePhoto;
	@Column(name = "ACTIVITYFILEIDS")
	@Schema(name = "上传活动附 上传文件ids 多个逗号隔开")
	private String activityFileIds;
	@Column(name = "ACTIVITYOTHERFILEIDS")
	@Schema(name = "上传活动附件-其他 上传文件ids 多个逗号隔开")
	private String activityOtherFileIds;
//	@ExcelField(title = "状态", sort = 12, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.STATE_TYPE)
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
	@ExcelField(title = "创建时间", sort = 13, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;
//	@ExcelField(title = "更新时间", sort = 14, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	@Schema(name = "知会人员，多选用逗号隔开")
	private String informPersonnel;

	public static TblFwglActivityManagement ofId(Long id) {
		TblFwglActivityManagement tblFwglActivityManagementMySql = new TblFwglActivityManagement();
		tblFwglActivityManagementMySql.setActivityManagementId(id);
		return tblFwglActivityManagementMySql;
	}
}
