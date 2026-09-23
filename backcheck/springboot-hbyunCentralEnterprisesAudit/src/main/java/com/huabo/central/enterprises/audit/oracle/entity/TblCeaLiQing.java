package com.huabo.central.enterprises.audit.oracle.entity;

import java.io.Serializable;
import java.util.Date;

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
 * 央企内审-综合管理-员工离庆
 */
@Schema(name="TblCeaLiQing")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_LI_QING")
public class TblCeaLiQing implements Serializable {
	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "主键ID")
	private Long id;

	/**
	 * 填报单位
	 */
	@Column(name = "FILLBELONGGROUP")
	@Schema(name = "填报单位")
	private Long fillBelongGroup;

	@Transient
	@Schema(name = "填报单位")
	private String fillBelongGroupName;

	/**
	 * 填报部门
	 */
	@Column(name = "FILLWORKUNIT")
	@Schema(name = "填报部门")
	private Long fillWorkUnit;

	@Transient
	@Schema(name = "填报部门名称")
	private String fillWorkUnitName;

	/**
	 * 人员ID
	 */
	@Column(name = "STAFFID")
	@Schema(name = "人员ID")
	private Long staffId;

	@Transient
	@Schema(name = "人员名称")
	private String staffName;

	/**
	 * 联系方式
	 */
	@Column(name = "CONTACTPHONE")
	@Schema(name = "联系方式")
	private String contactPhone;

	/**
	 * 人员性质
	 */
	@Column(name = "PERSONNELNATURE")
	@Schema(name = "人员性质")
	private String personnelNature;

	/**
	 * 身份证号
	 */
	@Column(name = "IDENTITYCARD")
	@Schema(name = "身份证号")
	private String identityCard;

	/**
	 * 离庆事由
	 */
	@Column(name = "REASON")
	@Schema(name = "离庆事由")
	private String reason;

	/**
	 * 目的地
	 */
	@Column(name = "DESTINATION")
	@Schema(name = "目的地")
	private String destination;

	/**
	 * 出发地
	 */
	@Column(name = "PLACEOFDEPARTURE")
	@Schema(name = "出发地")
	private String placeOfDeparture;

	/**
	 * 离庆时间
	 */
	@Column(name = "LEAVECELEBRATESTARTTIME")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(name = "离庆时间 yyyy-MM-dd")
	private Date leaveCelebrateStartTime;

	/**
	 * 计划返庆日期
	 */
	@Column(name = "LEAVECELEBRATEENDTIME")
	@Schema(name = "计划返庆日期 yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date leaveCelebrateEndTime;

	/**
	 * 离庆健康情况
	 */
	@Column(name = "HEALTHSITUATION")
	@Schema(name = "离庆健康情况")
	private String healthSituation;

	/**
	 * 离庆交通工具
	 */
	@Column(name = "VEHICLE")
	@Schema(name = "离庆交通工具")
	private String vehicle;

	/**
	 * 上传文件ids 多个逗号隔开
	 */
	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

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
	@Schema(name="创建人ID名称",hidden=true)
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

	public static TblCeaLiQing ofId(Long id) {
		TblCeaLiQing tblCeaLiQing = new TblCeaLiQing();
		tblCeaLiQing.setId(id);
		return tblCeaLiQing;
	}
}