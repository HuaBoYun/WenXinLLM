package com.huabo.legal.mysql.entity;

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
 * 制度审核/经营事项审核表
 */
@Schema(name="TblFwglInstitutionAuditMySql")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_institution_audit")
public class TblFwglInstitutionAuditMySql implements Serializable {

	@Id
	@Column(name = "INSTITUTIONAUDITID")
	@Schema(name = "制度审核ID")
	@GeneratedValue(generator = "JDBC")
	private Integer institutionAuditId;

	@NotNull(message = "auditType审核类型 1-制度审核 2-经营事项审核,不能为空")
	@Column(name = "AUDITTYPE")
	@Schema(name = "审核类型 1-制度审核 2-经营事项审核")
	private Integer auditType;

	@Column(name = "AUDITNAME")
	@Schema(name = "名称")
	private String auditName;

	@Column(name = "OPINIONNAME")
	@Schema(name = "起草意见（制度审核）")
	private String opinionName;

	@Column(name = "AUDITBELONGGROUPID")
	@Schema(name = "各部室及所属公司ID（制度审核）")
	private String auditBelongGroupId;

	@Transient
	@Schema(name = "各部室及所属公司名称（制度审核）")
	private String auditBelongGroupName;

	@Column(name = "DRAFTADMINISTRATOROPINION")
	@Schema(name = "起草部门合规管理员意见")
	private String draftAdministratorOpinion;

	@Column(name = "DRAFTDEPARTMENTOPINION")
	@Schema(name = "起草部门意见")
	private String draftDepartmentOpinion;

	@Column(name = "DEPARTMENTBELONGGROUPOPINION")
	@Schema(name = "各部室及所属公司意见")
	private String departmentBelongGroupOpinion;

	@Column(name = "DRAFTDEPARTMENTOPINIONSUCK")
	@Schema(name = "起草部门意见吸收情况")
	private String draftDepartmentOpinionSuck;

	@Column(name = "INSTITUTIONAUDITEXTID")
	@Schema(name = "关联ID-制度审核-制度ID,多个逗号隔开")
	private String institutionAuditExtId;

	@Column(name = "MATTERSINFORMEDPERSONNEL")
	@Schema(name = "事项知会人员(经营事项审核)")
	private String mattersInformedPersonnel;

	@Column(name = "MATTERSINSTRUCTIONS")
	@Schema(name = "事项说明(经营事项审核)")
	private String mattersInstructions;

	@Column(name = "HOSTDEPARTMENTOPINION")
	@Schema(name = "主办部门意见(经营事项审核)")
	private String hostDepartmentOpinion;

	@Column(name = "RELATEDDEPARTMENTOPINION")
	@Schema(name = "相关部门意见(经营事项审核)")
	private String relatedDepartmentOpinion;

	@Column(name = "TEAMLEADEROPINION")
	@Schema(name = "分管领导意见(经营事项审核)")
	private String teamLeaderOpinion;

	@Column(name = "FIRMLEADEROPINION")
	@Schema(name = "公司领导意见(经营事项审核)")
	private String firmLeaderOpinion;

	@Column(name = "FILEIDS")
	@Schema(name = "制度 上传文件ids 多个逗号隔开")
	private String fileIds;

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
	
	@Transient
	@Schema(name="所属集团名称",hidden=true)
	private String belongGroupName;

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

	public static TblFwglInstitutionAuditMySql ofId(Integer id) {
		TblFwglInstitutionAuditMySql tblFwglInstitutionAuditMySql = new TblFwglInstitutionAuditMySql();
		tblFwglInstitutionAuditMySql.setInstitutionAuditId(id);
		return tblFwglInstitutionAuditMySql;
	}
}
