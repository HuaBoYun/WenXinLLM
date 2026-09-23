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
 * 执业活动表
 */
@Schema(name="TblFwglPracticeActivity")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_practice_activity")
public class TblFwglPracticeActivity implements Serializable {

	private static final long serialVersionUID = 1L;
	@ExcelField(title = "执业活动ID", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "执业活动ID")
	private Long practiceActivityId;
	@ExcelField(title = "活动名称", sort = 1, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "活动名称")
	private String activityName;
	@ExcelField(title = "活动类别", sort = 2, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.ACTIVITY_TYPE)
	@Schema(name = "活动类别 2-文章发表 3-法律培训 5-法律尽调")
	private Integer activityCategory;
	@ExcelField(title = "活动时间", sort = 3, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "活动时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date activityTime;
	@ExcelField(title = "合同编号", sort = 4, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "合同编号")
	private String contractCode;
	@ExcelField(title = "金额", sort = 5, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "金额")
	private Double money;
	@ExcelField(title = "审核时间", sort = 6, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "审核时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date audittime;
	@ExcelField(title = "审核人", sort = 7, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "审核人")
	private String auditPerson;
	@ExcelField(title = "创建人（列表）", sort = 8, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "创建人（列表）")
	private String practiceActivityCreator;
	@ExcelField(title = "创建时间（列表）", sort = 9, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "创建时间（列表）")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date practiceActivityCreatedTime;
	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;
	@ExcelField(title = "诉讼代理-名称", sort = 10, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "诉讼代理-名称")
	private String lawsuitAgentName;
	@ExcelField(title = "诉讼代理-案号", sort = 11, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "诉讼代理-案号")
	private String lawsuitAgentCaseNumber;
	@ExcelField(title = "诉讼代理-原告", sort = 12, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "诉讼代理-原告")
	private String lawsuitAgentPlaintiff;
	@ExcelField(title = "诉讼代理-被告", sort = 13, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "诉讼代理-被告")
	private String lawsuitAgentDefendant;
	@ExcelField(title = "诉讼代理-标的金额", sort = 14, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "诉讼代理-标的金额")
	private Double lawsuitAgentMoney;
	@ExcelField(title = "诉讼代理-立案日期", sort = 15, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "诉讼代理-立案日期")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lawsuitAgentTime;
	@ExcelField(title = "诉讼代理-审理阶段", sort = 16, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "诉讼代理-审理阶段")
	private String lawsuitAgentTrial;
	@ExcelField(title = "诉讼代理-代理人", sort = 17, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "诉讼代理-代理人")
	private String lawsuitAgentPersonnel;
	@ExcelField(title = "文章发表-题目", sort = 18, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "文章发表-题目")
	private String articlePublishTopic;
	@ExcelField(title = "文章发表-发表时间", sort = 19, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "文章发表-发表时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date articlePublishTime;
	@ExcelField(title = "文章发表-发表载体", sort = 20, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "文章发表-发表载体")
	private String articlePublishPublishingCarrier;
	@ExcelField(title = "文章发表-作者", sort = 21, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "文章发表-作者")
	private String articlePublishAuthor;
	@ExcelField(title = "法律培训-培训主题", sort = 22, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "法律培训-培训主题")
	private String legalTrainingTopic;
	@ExcelField(title = "法律培训-培训内容", sort = 23, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "法律培训-培训内容")
	private String legalTrainingContent;
	@ExcelField(title = "法律培训-培训时间", sort = 24, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "法律培训-培训时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date legalTrainingTime;
	@ExcelField(title = "法律培训-参训人员", sort = 25, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "法律培训-参训人员")
	private String legalTrainingPersonnel;
	@ExcelField(title = "法律审核-项目名称", sort = 26, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "法律审核-项目名称")
	private String legalAuditName;
	@ExcelField(title = "法律审核-项目概述", sort = 27, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "法律审核-项目概述")
	private String legalAuditSummary;
	@ExcelField(title = "法律审核-审核时间", sort = 28, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "法律审核-审核时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date legalAuditTime;
	@ExcelField(title = "法律审核-审核人", sort = 29, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "法律审核-审核人")
	private String legalAuditPersonnel;
	@ExcelField(title = "法律尽调-尽调单位", sort = 30, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "法律尽调-尽调单位")
	private String legalAdjustmentWorkUnit;
	@ExcelField(title = "法律尽调-尽调内容", sort = 31, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "法律尽调-尽调内容")
	private String legalAdjustmentContent;
	@ExcelField(title = "法律尽调-尽调时间", sort = 32, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "法律尽调-尽调时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date legalAdjustmentTime;
	@ExcelField(title = "法律尽调-尽调人", sort = 33, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "法律尽调-尽调人")
	private String legalAdjustmentPersonnel;
	@ExcelField(title = "计划名称", sort = 34, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.STATE_TYPE)
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
	@ExcelField(title = "创建时间", sort = 35, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;
	@ExcelField(title = "更新时间", sort = 36, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	public static TblFwglPracticeActivity ofId(Long id) {
		TblFwglPracticeActivity tblFwglPracticeActivityMySql = new TblFwglPracticeActivity();
		tblFwglPracticeActivityMySql.setPracticeActivityId(id);
		return tblFwglPracticeActivityMySql;
	}
}
