package com.huabo.compliance.oracle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 合规-计划管理
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_COMPLIANCE_PLAN_MGT")
public class TblCompliancePlanMgtOracle implements Serializable {
	/**
	 * id
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	@Schema(name = "id")
	private Integer id;

	/**
	 * 计划编号
	 */
	@Column(name = "PLANSERIALNUMBER")
	@Schema(name = "计划编号")
	private String planSerialNumber;

	/**
	 * 计划名称
	 */
	@Column(name = "PLANNAME")
	@Schema(name = "计划名称")
	private String planName;

	/**
	 * 合规进程 1-拟定 2-审批 3-发布
	 */
	@Column(name = "COMPLIANCEPROCESS")
	@Schema(name = "合规进程 1-拟定 2-审批 3-发布")
	private Integer complianceProcess;

	/**
	 * 层级
	 */
	@Column(name = "HIERARCHY")
	@Schema(name = "层级")
	private String hierarchy;

	/**
	 * 编制人
	 */
	@Column(name = "COMPILER")
	@Schema(name = "编制人")
	private Integer compiler;

	@Transient
	@Schema(name="编制人名称",hidden=true)
	private String compilerName;

	/**
	 * 部门负责人
	 */
	@Column(name = "DEPARTMENTHEAD")
	@Schema(name = "部门负责人")
	private Integer departmentHead;

	@Transient
	@Schema(name="部门负责人名称",hidden=true)
	private String departmentHeadName;

	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

	@Column(name = "CONTENT")
	@Schema(name = "富文本框")
	private String content;

	/**
	 * 状态
	 */
	@Column(name = "STATE")
	@Schema(name="状态",hidden=true)
	private Integer state;

	/**
	 * 创建人
	 */
	@Column(name = "CREATOR")
	@Schema(name="创建人",hidden=true)
	private Integer creator;

	@Transient
	@Schema(name="创建人名称",hidden=true)
	private String creatorName;

	/**
	 * 工作单位
	 */
	@Column(name = "WORKUNIT")
	@Schema(name="工作单位",hidden=true)
	private Integer workUnit;

	/**
	 * 所属集团
	 */
	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团",hidden=true)
	private Integer belongGroup;

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

	public static TblCompliancePlanMgtOracle ofId(Integer id) {
		TblCompliancePlanMgtOracle tblCompliancePlanMgtOracle = new TblCompliancePlanMgtOracle();
		tblCompliancePlanMgtOracle.setId(id);
		return tblCompliancePlanMgtOracle;
	}
}