package com.huabo.compliance.oracle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 合规-检查方案
 */
@Schema(name="TblComplianceInspectPlanOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_COMPLIANCE_INSPECT_PLAN")
public class TblComplianceInspectPlanOracle implements Serializable {
	/**
	 * id
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	@Schema(name = "id")
	private Integer id;

	/**
	 * 方案编号
	 */
	@Column(name = "PLANCODE")
	@Schema(name = "方案编号")
	private String planCode;

	/**
	 * 方案名称
	 */
	@Column(name = "PLANNAME")
	@Schema(name = "方案名称")
	private String planName;

	/**
	 * 方案年度
	 */
	@Column(name = "PLANYEAR")
	@Schema(name = "方案年度 格式：yyyy")
	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy")
	private Date planYear;

	/**
	 * 检查类型 1-专项检查 2-综合检查 3-定期检查
	 */
	@Column(name = "INSPECTTYPE")
	@Schema(name = "检查类型 1-专项检查 2-综合检查 3-定期检查")
	private Integer inspectType;

	/**
	 * 方案开始时间
	 */
	@Column(name = "PLANTIMESTART")
	@Schema(name = "方案开始时间 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date planTimeStart;

	/**
	 * 方案结束时间
	 */
	@Column(name = "PLANTIMEEND")
	@Schema(name = "方案结束时间 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date planTimeEnd;

	/**
	 * 配合部门
	 */
	@Column(name = "COOPERATEDEPARTMENT")
	@Schema(name = "配合部门")
	private Integer cooperateDepartment;

	@Column(name = "COOPERATEDEPARTMENTNAME")
	@Schema(name = "配合部门名称")
	private String cooperateDepartmentName;

	/**
	 * 负责人
	 */
	@Column(name = "RESPONSIBLEPERSON")
	@Schema(name = "负责人")
	private Integer responsiblePerson;

	@Transient
	@Schema(name = "负责人名称")
	private String responsiblePersonName;

	/**
	 * 投入人力
	 */
	@Column(name = "INVESTMANPOWER")
	@Schema(name = "投入人力")
	private String investManpower;

	/**
	 * 检查公司
	 */
	@Column(name = "INSPECTCOMPANY")
	@Schema(name = "检查公司")
	private Integer inspectCompany;

	@Transient
	@Schema(name = "检查公司名称")
	private String inspectCompanyName;

	/**
	 * 检查部门
	 */
	@Column(name = "INSPECTDEPARTMENT")
	@Schema(name = "检查部门")
	private Integer inspectDepartment;

	@Column(name = "INSPECTDEPARTMENTNAME")
	@Schema(name = "检查部门名称")
	private String inspectDepartmentName;

	/**
	 * 富文本
	 */
	@Column(name = "CONTENT")
	@Schema(name = "富文本")
	private String content;

	/**
	 * 上传文件ids;多个逗号隔开
	 */
	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids;多个逗号隔开")
	private String fileIds;

	/**
	 * 状态
	 */
	@Column(name = "STATE")
	@Schema(name = "状态")
	private Integer state;

	/**
	 * 创建人
	 */
	@Column(name = "CREATOR")
	@Schema(name = "创建人")
	private Integer creator;

	@Transient
	@Schema(name = "创建人名称")
	private String creatorName;

	/**
	 * 工作单位
	 */
	@Column(name = "WORKUNIT")
	@Schema(name = "工作单位")
	private Integer workUnit;

	/**
	 * 所属集团
	 */
	@Column(name = "BELONGGROUP")
	@Schema(name = "所属集团")
	private Integer belongGroup;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATEDTIME")
	@Schema(name = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	/**
	 * 更新时间
	 */
	@Column(name = "UPDATEDTIME")
	@Schema(name = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	private static final long serialVersionUID = 1L;

	public static TblComplianceInspectPlanOracle ofId(Integer id) {
		TblComplianceInspectPlanOracle inspectPlanOracle = new TblComplianceInspectPlanOracle();
		inspectPlanOracle.setId(id);
		return inspectPlanOracle;
	}
}