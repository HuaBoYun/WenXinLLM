package com.huabo.audit.oracle.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_PROJECT")
@Data
@Schema(name="审计项目实体类")
@Accessors(chain = true)
public class TblNbsjProjectEntity {
	
	private static final long serialVersionUID = 1L;
	public final static Integer NO_SELECT=0;//未切换
	public final static Integer YE_SELECT=1;//已切换
	public final static Integer UPDATENO = 0;//不完整
	public final static Integer UPDATEYES = 1;//完整
	public final static Integer GD_STATUS=4;//审计项目归档状态
	public final static Integer EXAMINETYPE1 = 1;//未审批
	/**
	 * 审批中
	 */
	public final static Integer EXAMINETYPE2 = 2;//审批中
	public final static Integer EXAMINETYPE3 = 3;//审批驳回
	public final static Integer EXAMINETYPE4 = 4;//审批通过
	public final static Integer EXAMINETYPE5 = 5;//需调整
	public final static Integer EXAMINETYPE6 = 6;//中断

    @TableId(value = "projectid", type= IdType.AUTO)
    @Schema(name = "审计项目ID")
    private Integer projectId;

    @TableField(value = "prjoectname")
    @Schema(name = "审计项目名称")
    private String prjoectName;

    @TableField(value = "planyear")
    @Schema(name = "计划年份")
    private String planYear;

    @TableField(value = "projectsource")
    @Schema(name = "项目来源")
    private String projectSource;

    @TableField(value = "startdate")
    @Schema(name = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    @TableField(value = "enddate")
    @Schema(name = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;

    @TableField(value = "pmid")
    @Schema(name = "项目经理")
    private Integer pmId;

    @TableField(value = "tempid")
    @Schema(name = "审计模板id")
    private Integer tempId;

    @TableField(exist = false)
    @Schema(name = "审计模板")
    private TblNbsjTempleteEntity temp;//审计模板

    @TableField(value = "costs")
    @Schema(name = "费用")
    private Integer costs;

    @TableField(value = "purpose")
    @Schema(name = "审计目标和范围")
    private String purpose;

    @TableField(value = "scopes")
    @Schema(name = "审计内容和重点")
    private String scopes;

    @TableField(value = "pursuant")
    @Schema(name = "审计程序和方法")
    private String pursuant;

    @TableField(value = "comments")
    @Schema(name = "备注")
    private String comments;

    @TableField(value = "umpireid")
    @Schema(name = "主审人")
    private Integer umpireId;

    @TableField(value = "controlid")
    @Schema(name = "质控经理")
    private Integer controlId;

    @TableField(value = "audittype")
    @Schema(name = "审计类型")
    private String auditType;

    @TableField(value = "projectcode")
    @Schema(name = "项目编号")
    private String projectCode;

    @TableField(value = "status")
    @Schema(name = "状态:1启动 0未启动 2实施 3完成 4归档")
    private Integer status;

    @TableField(value = "createtime")
    @Schema(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField(value = "updatetime")
    @Schema(name = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @TableField(value = "currentstatre")
    @Schema(name = "选择的项目")
    private Integer currentStatre;

    @TableField(value = "createstaffid")
    @Schema(name = "创建人")
    private Integer createStaffId;

    @TableField(value = "assigbedpmtime")
    @Schema(name = "指派项目经理时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date assigbedpmTime;

    @TableField(value = "assigbedumpetime")
    @Schema(name = "指派主审人时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date assigbedumpeTime;

    @TableField(value = "assigbedcontroltime")
    @Schema(name = "指派质控经理时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date assigbedControlTime;

    @TableField(value = "planid")
    @Schema(name = "计划编号")
    private Integer planId;

    @TableField(exist = false)
    @Schema(name = "计划名称")
    private String planName;

    @TableField(value = "auditorgid")
    @Schema(name = "审计对象组织/被审计单位组织id")
    private Integer auditOrgId;

    @TableField(value = "tempzyid")
    @Schema(name = "指引模板")
    private Integer tempzyId;

    @TableField(exist = false)
    @Schema(name = "指引模板")
    private TblNbsjTempleteEntity zyTemp;

    @TableField(value = "updatestatus")
    @Schema(name = "修改模板时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Integer updateStatus;

    @TableField(value = "orgid")
    @Schema(name = "隶属组织")
    private Integer orgId;

    @TableField(value = "finishtime")
    @Schema(name = "完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishTime;

    @TableField(value = "implementtime")
    @Schema(name = "实施时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date implementTime;

    @TableField(value = "examinetype")
    @Schema(name = "审批状态 1：未审批 2：审批中 3：审批驳回 4：审批通过")
    private Integer examineType;

    @TableField(value = "pro_desc")
    @Schema(name = "相关内容")
    private String proDesc;

    @TableField(value = "pro_sjfs")
    @Schema(name = "审计方式")
    private String proSjfs;

    @TableField(value = "auditstaffid")
    @Schema(name = "审计对象人")
    private Integer auditStaffId;

    @TableField(exist = false)
    @Schema(name = "项目对象名称")
    private String proObj;

    @TableField(exist = false)
    @Schema(name="项目实施期间（天）")
    private Integer carryOutDay;

    @TableField(value = "sjlx")
    @Schema
    private String sjlx;

    @TableField(value = "sjzr")
    @Schema
    private String sjzr;

    @TableField(value = "yqjcqk")
    @Schema
    private String yqjcqk;

    @TableField(value = "ejfhr")
    @Schema
    private Integer ejfhr;

    @TableField(value = "templeteid")
    @Schema
    private Integer templeteId;

    @TableField(value = "staffid")
    @Schema
    private Integer staffId;

    @TableField(value = "auditorg")
    @Schema
    private String auditOrg;

    @TableField(value = "fpstatus")
    @Schema
    private Integer fpStatus;

    @TableField(value = "planprojectid")
    @Schema
    private Integer planProjectId;

    @TableField(value = "pprojectname")
    @Schema(name = "计划项目名称")
    private String pProjectName;

    @TableField(value = "targetname")
    @Schema(name = "工作目标")
    private String targetName;

    @TableField(value = "orgids")
    @Schema(name = "组织id")
    private String orgIds;

    @TableField(value = "orgidnames")
    @Schema
    private String orgIdNames;

    @TableField(value = "externalassig")
    @Schema(name = "是否外委")
    private Integer externAlassig;

    @TableField(value = "pcount")
    @Schema
    private Integer pCount;

    @TableField(value = "pstatus")
    @Schema
    private Integer pStatus;

    @TableField(value = "filcode")
    @Schema
    private String filCode;

    @TableField(value = "filname")
    @Schema
    private String filName;

    @TableField(exist = false)
    @Schema(name = "审计单位")
    private String auditUnit;

    @TableField(exist = false)
    @Schema(name = "被审计单位/项目对象名称")
    private String auditedUnit;

    @TableField(exist = false)
    @Schema(name = "项目经理")
    private String projectManager;

    @TableField(exist = false)
    @Schema(name = "审计模板名称")
    private String auditTemp;

    @TableField(exist = false)
    @Schema(name = "指引模板名称")
    private String guideTemp;

    @TableField(exist = false)
    @Schema(name = "项目小组信息")
    private List<ProjectTeamInfoEntity> projectTeamInfo;

    @TableField(exist = false)
    @Schema(name = "附件信息")
    private List<TblAttachmentEntity> attachmentInfoList;

    @TableField(exist = false)
    @Schema(name = "项目对象 0：部门/单位 1：用户")
    private String projectObj;

    @TableField(exist = false)
    @Schema(name = "token")
    private String token;

    @TableField(exist = false)
    @Schema(name = "是否计划 y：更新计划")
    private String isPlan;
    
    
	@TableField(value = "tblPm")
    @Schema(name = "项目经理")
    private TblStaff tblPm;
	
	@TableField(value = "tblumpe")
    @Schema(name = "主审人")
    private TblStaff tblumpe;
	
	@TableField(value = "tblcontrol")
    @Schema(name = "质控经理")
    private TblStaff tblcontrol;
	
	@TableField(value = "tblcreater")
    @Schema(name = "创建人")
    private TblStaff tblcreater;
	
	@TableField(value = "tblProjectTeams")
    @Schema
    private Set<TblNbsjProjectTeamEntity> tblProjectTeams;
	
	@TableField(value = "tblnbsjorgs")
    @Schema
    private TblOrganization tblnbsjorgs;
	
	@TableField(value = "tblorg")
    @Schema
    private TblOrganization tblorg;
	
	@TableField(value = "tblnbsjstaffs")
    @Schema
    private TblStaff tblnbsjstaffs;
	
	@TableField(value = "tblnbsjPlan")
    @Schema
    private TblNbsjAuditplan tblnbsjPlan;
	
	@TableField(value = "tblprojectAtts")
    @Schema
    private Set<TblAttachmentEntity> tblprojectAtts;
	
}
