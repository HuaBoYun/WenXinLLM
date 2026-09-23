package com.huabo.central.enterprises.audit.oracle.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 央企内审-项目评优-审计工质量评估扩展表
 */
@Schema(name="TblCeaQualityAssessmentExt")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_QUALITY_ASSESSMENT_EXT")
public class TblCeaQualityAssessmentExt implements Serializable {
	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "ID")
	@Schema(name = "主键ID")
	@GeneratedValue(generator = "JDBC")
	private Long id;

	/**
	 * 审计工质量评估主键ID
	 */
	@Column(name = "QUALITYASSESSMENTID")
	@Schema(name="审计工质量评估主键ID",hidden=true)
	private Long qualityAssessmentId;

	/**
	 * 列一（题目）
	 */
	@Column(name = "ONE")
	@Schema(name = "列一（题目）")
	private String one;

	/**
	 * 列二（题目）
	 */
	@Column(name = "TWO")
	@Schema(name = "列二（题目）")
	private String two;

	/**
	 * 列三（题目）
	 */
	@Column(name = "THREE")
	@Schema(name = "列三（题目）")
	private String three;

	/**
	 * 列四（题目）
	 */
	@Column(name = "FOUR")
	@Schema(name = "列四（题目）")
	private String four;

	/**
	 * 列五（题目）
	 */
	@Column(name = "FIVE")
	@Schema(name = "列五（题目）")
	private String five;

	/**
	 * 填报要求
	 */
	@Column(name = "FILLREQUIRE")
	@Schema(name = "填报要求")
	private String fillRequire;

	/**
	 * 评估分值
	 */
	@Column(name = "EVALUATIONSCORE")
	@Schema(name = "评估分值")
	private String evaluationScore;

	/**
	 * 调整后分值
	 */
	@Column(name = "ADJUSTEDSCORE")
	@Schema(name = "调整后分值")
	private String adjustedScore;

	/**
	 * 评估说明（地区公司）
	 */
	@Column(name = "EVALUATIONEXPLANATIONREGION")
	@Schema(name = "评估说明（地区公司）")
	private String evaluationExplanationRegion;

	/**
	 * 评估说明（审计部）
	 */
	@Column(name = "EVALUATIONEXPLANATIONAUDIT")
	@Schema(name = "评估说明（审计部）")
	private String evaluationExplanationAudit;

	/**
	 * 评估参考
	 */
	@Column(name = "EVALUATIONREFERENCE")
	@Schema(name = "评估参考")
	private String evaluationReference;

	/**
	 * 上传文件ids 多个逗号隔开
	 */
	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

	@Transient
	@Schema(name="文件列表")
	private List<TblAttachment> fileList;

	@Column(name = "EXTID")
	@Schema(name="题目关联ID")
	private Long extId;

	/**
	 * 状态
	 */
	@Column(name = "STATE")
	@Schema(name="状态",hidden=true)
	private Integer state;

	/**
	 * 创建人ID
	 */
	@Column(name = "CREATOR")
	@Schema(name="创建人ID",hidden=true)
	private Long creator;

	@Transient
	@Schema(name="创建人名称",hidden=true)
	private String creatorName;

	/**
	 * 工作单位ID
	 */
	@Column(name = "WORKUNIT")
	@Schema(name="工作单位ID",hidden=true)
	private Long workUnit;

	/**
	 * 所属集团ID
	 */
	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团ID",hidden=true)
	private Long belongGroup;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATEDTIME")
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	/**
	 * 更新时间
	 */
	@Column(name = "UPDATEDTIME")
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	private static final long serialVersionUID = 1L;

	public static TblCeaQualityAssessmentExt ofId(Long id) {
		TblCeaQualityAssessmentExt tblCeaQualityAssessmentExt = new TblCeaQualityAssessmentExt();
		tblCeaQualityAssessmentExt.setId(id);
		return tblCeaQualityAssessmentExt;
	}
}