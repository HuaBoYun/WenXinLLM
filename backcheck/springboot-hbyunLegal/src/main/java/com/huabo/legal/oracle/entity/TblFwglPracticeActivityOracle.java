package com.huabo.legal.oracle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 执业活动表
 */
@Schema(name="TblFwglPracticeActivityOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_practice_activity")
public class TblFwglPracticeActivityOracle implements Serializable {

	@Id
	@Column(name = "PRACTICEACTIVITYID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "执业活动ID")
	private Long practiceActivityId;

	@NotNull(message = "activityCategory活动类别 2-文章发表 3-法律培训 5-法律尽调，不能为空")
	@Column(name = "ACTIVITYCATEGORY")
	@Schema(name = "活动类别 2-文章发表 3-法律培训 5-法律尽调")
	private Integer activityCategory;

	@Column(name = "ACTIVITYNAME")
	@Schema(name = "活动名称")
	private String activityName;

	@Column(name = "ACTIVITYTIME")
	@Schema(name = "活动时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date activityTime;

	@Column(name = "AUDITTIME")
	@Schema(name = "审核时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date audittime;

	@Column(name = "AUDITPERSON")
	@Schema(name = "审核人")
	private String auditPerson;

	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

	@Column(name = "LAWSUITAGENTNAME")
	@Schema(name = "诉讼代理-名称")
	private String lawsuitAgentName;

	@Column(name = "LAWSUITAGENTCASENUMBER")
	@Schema(name = "诉讼代理-案号")
	private String lawsuitAgentCaseNumber;

	@Column(name = "LAWSUITAGENTPLAINTIFF")
	@Schema(name = "诉讼代理-原告")
	private String lawsuitAgentPlaintiff;

	@Column(name = "LAWSUITAGENTDEFENDANT")
	@Schema(name = "诉讼代理-被告")
	private String lawsuitAgentDefendant;

	@Column(name = "LAWSUITAGENTMONEY")
	@Schema(name = "诉讼代理-标的金额")
	private Double lawsuitAgentMoney;

	@Column(name = "LAWSUITAGENTTIME")
	@Schema(name = "诉讼代理-立案日期")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lawsuitAgentTime;

	@Column(name = "LAWSUITAGENTTRIAL")
	@Schema(name = "诉讼代理-审理阶段")
	private String lawsuitAgentTrial;

	@Column(name = "LAWSUITAGENTPERSONNEL")
	@Schema(name = "诉讼代理-代理人")
	private String lawsuitAgentPersonnel;

	@Column(name = "ARTICLEPUBLISHTOPIC")
	@Schema(name = "文章发表-题目")
	private String articlePublishTopic;

	@Column(name = "ARTICLEPUBLISHTIME")
	@Schema(name = "文章发表-发表时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date articlePublishTime;

	/**
	 *  oracle数据库字段与实体类不一致注意
	 */
	@Column(name = "ARTICLEPUBLISHCARRIER")
	@Schema(name = "文章发表-发表载体")
	private String articlePublishCarrier;

	@Transient
	@Schema(name="文章发表-发表载体(映射-articlePublishCarrier)",hidden=true)
	private String articlePublishPublishingCarrier;

	@Column(name = "ARTICLEPUBLISHAUTHOR")
	@Schema(name = "文章发表-作者")
	private String articlePublishAuthor;

	@Column(name = "LEGALTRAININGTOPIC")
	@Schema(name = "法律培训-培训主题")
	private String legalTrainingTopic;

	@Column(name = "LEGALTRAININGCONTENT")
	@Schema(name = "法律培训-培训内容")
	private String legalTrainingContent;

	@Column(name = "LEGALTRAININGTIME")
	@Schema(name = "法律培训-培训时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date legalTrainingTime;

	//	@Column(name = "LEGALTRAININGPERSONNEL")
	//	@Schema(name = "法律培训-参训人员")
	//	private String legalTrainingPersonnel;

	@Column(name = "LEGALAUDITNAME")
	@Schema(name = "法律审核-项目名称")
	private String legalAuditName;

	@Column(name = "LEGALAUDITSUMMARY")
	@Schema(name = "法律审核-项目概述")
	private String legalAuditSummary;

	@Column(name = "LEGALAUDITTIME")
	@Schema(name = "法律审核-审核时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date legalAuditTime;

	@Column(name = "LEGALAUDITPERSONNEL")
	@Schema(name = "法律审核-审核人")
	private String legalAuditPersonnel;

	@Column(name = "LEGALADJUSTMENTWORKUNIT")
	@Schema(name = "法律尽调-尽调单位")
	private String legalAdjustmentWorkUnit;

	@Column(name = "LEGALADJUSTMENTCONTENT")
	@Schema(name = "法律尽调-尽调内容")
	private String legalAdjustmentContent;

	@Column(name = "LEGALADJUSTMENTTIME")
	@Schema(name = "法律尽调-尽调时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date legalAdjustmentTime;

	@Column(name = "LEGALADJUSTMENTPERSONNEL")
	@Schema(name = "法律尽调-尽调人")
	private String legalAdjustmentPersonnel;

	@Column(name = "STATE")
	@Schema(name="状态",hidden=true)
	private Integer state;

	@Column(name = "CREATOR")
	@Schema(name="创建人",hidden=true)
	private String creator;

	@Transient
	@Schema(name="创建人名称",hidden=true)
	private String creatorName;

	@Column(name = "WORKUNIT")
	@Schema(name="工作单位",hidden=true)
	private String workUnit;

	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;

	@Column(name = "CREATEDTIME")
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	@Column(name = "UPDATEDTIME")
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	private static final long serialVersionUID = 1L;

	public static TblFwglPracticeActivityOracle ofId(Long id) {
		TblFwglPracticeActivityOracle tblFwglPracticeActivityMySql = new TblFwglPracticeActivityOracle();
		tblFwglPracticeActivityMySql.setPracticeActivityId(id);
		return tblFwglPracticeActivityMySql;
	}
}
