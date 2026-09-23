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
 * 央企内审-项目评优-审计工质量评估
 */
@Schema(name="TblCeaQualityAssessment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_QUALITY_ASSESSMENT")
public class TblCeaQualityAssessment implements Serializable {

	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "主键ID")
	private Long id;

	/**
	 * 套题ID
	 */
	@Column(name = "SUBJECTID")
	@Schema(name = "套题ID")
	private Long subjectId;

	/**
	 * 填报单位ID
	 */
	@Column(name = "FILLBELONGGROUP")
	@Schema(name = "填报单位ID")
	private Long fillBelongGroup;

	@Transient
	@Schema(name = "填报单位名称")
	private String fillBelongGroupName;

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

	@Transient
	@Schema(name="明细")
	private List<TblCeaQualityAssessmentExt> qualityAssessmentExtList;

	@Column(name = "TOTALSCORE")
	@Schema(name = "总分")
	private Integer totalScore;

	private static final long serialVersionUID = 1L;

	public static TblCeaQualityAssessment ofId(Long id) {
		TblCeaQualityAssessment tblCeaQualityAssessment = new TblCeaQualityAssessment();
		tblCeaQualityAssessment.setId(id);
		return tblCeaQualityAssessment;
	}
}