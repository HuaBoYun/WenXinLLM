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
 * 制度审核/经营事项审核表
 */
@Schema(name="TblFwglInstitutionAudit")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_institution_audit")
public class TblFwglInstitutionAudit implements Serializable {

	private static final long serialVersionUID = 1L;
	//@ExcelField(title = "制度审核ID", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "制度审核ID")
	private Long institutionAuditId;
	@ExcelField(title = "审核类型 1-制度审核 2-经营事项审核", sort = 1, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.INSTITUTION_TYPE)
	@Schema(name = "审核类型 1-制度审核 2-经营事项审核")
	private Integer auditType;
	@ExcelField(title = "名称", sort = 2, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "名称")
	private String auditName;
	//@ExcelField(title = "起草意见（制度审核）", sort = 3, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "起草意见（制度审核）")
	private String opinionName;
	//@ExcelField(title = "各部室及所属公司ID（制度审核）", sort = 4, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "各部室及所属公司ID（制度审核）")
	private String auditBelongGroupId;
	//@ExcelField(title = "各部室及所属公司名称（制度审核）", sort = 5, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "各部室及所属公司名称（制度审核）")
	private String auditBelongGroupName;
	//@ExcelField(title = "起草部门合规管理员意见", sort = 6, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "起草部门合规管理员意见")
	private String draftAdministratorOpinion;
	//@ExcelField(title = "起草部门意见", sort = 7, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "起草部门意见")
	private String draftDepartmentOpinion;
	//@ExcelField(title = "各部室及所属公司意见", sort = 8, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "各部室及所属公司意见")
	private String departmentBelongGroupOpinion;
	//@ExcelField(title = "起草部门意见吸收情况", sort = 9, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "起草部门意见吸收情况")
	private String draftDepartmentOpinionSuck;
	@Column(name = "INSTITUTIONAUDITEXTID")
	@Schema(name = "关联ID-制度审核-制度ID,多个逗号隔开")
	private String institutionAuditExtId;
	@Column(name = "MATTERSINFORMEDPERSONNELID")
	//@ExcelField(title = "事项知会人员ID,多个逗号隔开(经营事项审核)", sort = 10, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "事项知会人员ID(经营事项审核)")
	private String mattersInformedPersonnelId;
	//@ExcelField(title = "事项知会人员(经营事项审核)", sort = 10, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "事项知会人员(经营事项审核)")
	private String mattersInformedPersonnel;
	//@ExcelField(title = "事项说明(经营事项审核)", sort = 11, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "事项说明(经营事项审核)")
	private String mattersInstructions;
	//@ExcelField(title = "主办部门意见(经营事项审核)", sort = 12, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "主办部门意见(经营事项审核)")
	private String hostDepartmentOpinion;
	//@ExcelField(title = "相关部门意见(经营事项审核)", sort = 13, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "相关部门意见(经营事项审核)")
	private String relatedDepartmentOpinion;
	//@ExcelField(title = "分管领导意见(经营事项审核)", sort = 14, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "分管领导意见(经营事项审核)")
	private String teamLeaderOpinion;
	//@ExcelField(title = "公司领导意见(经营事项审核)", sort = 15, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "公司领导意见(经营事项审核)")
	private String firmLeaderOpinion;
	@Column(name = "FILEIDS")
	@Schema(name = "制度 上传文件ids 多个逗号隔开")
	private String fileIds;
	@ExcelField(title = "状态", sort = 16, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.SHSTATE_TYPE)
	@Schema(name="状态",hidden=true)
	private Integer state;
	@Schema(name="创建人",hidden=true)
	private String creator;
	@Schema(name="创建人名称",hidden=true)
	private String creatorName;
	@Schema(name="工作单位",hidden=true)
	private String workUnit;
	@Schema(name="所属集团名称",hidden=true)
	private String belongGroupName;
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;
	@ExcelField(title = "创建时间", sort = 17, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;
	@ExcelField(title = "更新时间", sort = 18, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	public static TblFwglInstitutionAudit ofId(Long id) {
		TblFwglInstitutionAudit tblFwglInstitutionAuditMySql = new TblFwglInstitutionAudit();
		tblFwglInstitutionAuditMySql.setInstitutionAuditId(id);
		return tblFwglInstitutionAuditMySql;
	}
}
