package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.cybermonitor.util.IgnoreSwaggerParameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@TableName("TBL_NBSJ_PROJECT")
@Data
@Schema(name="审计项目实体类")
@Accessors(chain = true)
public class TblNbsjProject implements Serializable{
	
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

	@Id
    @TableId(value = "PROJECTID")
    @Schema(name = "审计项目ID")
    private Integer projectId;

    @TableField(value = "PRJOECTNAME")
    @Schema(name = "审计项目名称")
    private String prjoectName;

    @TableField(value = "PLANYEAR")
    @Schema(name = "计划年份")
    private String planYear;

    @TableField(value = "PROJECTSOURCE")
    @Schema(name = "项目来源")
    private String projectSource;

    @TableField(value = "STARTDATE")
    @Schema(name = "开始时间")
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date startDate;

    @TableField(value = "ENDDATE")
    @Schema(name = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date endDate;

    @TableField(value = "PMID")
    @Schema(name = "项目经理")
    private Integer pmId;

    @TableField(value = "TEMPID")
    @Schema(name = "审计模板id")
    private Integer tempId;

    @TableField(value = "COSTS")
    @Schema(name = "费用")
    private Integer costs;

    @TableField(value = "PURPOSE")
    @Schema(name = "审计目标和范围")
    private String purpose;

    @TableField(value = "SCOPES")
    @Schema(name = "审计内容和重点")
    private String scopes;

    @TableField(value = "PURSUANT")
    @Schema(name = "审计程序和方法")
    private String pursuant;

    @TableField(value = "COMMENTS")
    @Schema(name = "备注")
    private String comments;

    @TableField(value = "UMPIREID")
    @Schema(name = "主审人")
    private Integer umpireId;

    @TableField(value = "CONTROLID")
    @Schema(name = "质控经理")
    private Integer controlId;

    @TableField(value = "AUDITTYPE")
    @Schema(name = "审计类型")
    private String auditType;

    @TableField(value = "PROJECTCODE")
    @Schema(name = "项目编号")
    private String projectCode;

    @TableField(value = "STATUS")
    @Schema(name = "状态:1启动 0未启动 2实施 3完成 4归档")
    private Integer status;

    @TableField(value = "CREATETIME")
    @Schema(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField(value = "UPDATETIME")
    @Schema(name = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @TableField(value = "CURRENTSTATRE")
    @Schema(name = "选择的项目")
    private Integer currentStatre;

    @TableField(value = "CREATESTAFFID")
    @Schema(name = "创建人")
    private Integer createStaffId;

    @TableField(value = "ASSIGBEDPMTIME")
    @Schema(name = "指派项目经理时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date assigbedpmTime;

    @TableField(value = "ASSIGBEDUMPETIME")
    @Schema(name = "指派主审人时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date assigbedumpeTime;

    @TableField(value = "ASSIGBEDCONTROLTIME")
    @Schema(name = "指派质控经理时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date assigbedControlTime;

    @TableField(value = "PLANID")
    @Schema(name = "计划编号")
    private Integer planId;

    @TableField(value = "AUDITORGID")
    @Schema(name = "审计对象组织/被审计单位组织id")
    private Integer auditOrgId;

    @TableField(value = "TEMPZYID")
    @Schema(name = "指引模板")
    private Integer tempzyId;

    @TableField(value = "UPDATESTATUS")
    @Schema(name = "修改模板时间")
    private Integer updateStatus;

    @TableField(value = "ORGID")
    @Schema(name = "隶属组织")
    private Integer orgId;

    @TableField(value = "FINISHTIME")
    @Schema(name = "完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishTime;

    @TableField(value = "IMPLEMENTTIME")
    @Schema(name = "实施时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date implementTime;

    @TableField(value = "EXAMINETYPE")
    @Schema(name = "审批状态 1：未审批 2：审批中 3：审批驳回 4：审批通过")
    private Integer examineType;

    @TableField(value = "PRO_DESC")
    @Schema(name = "相关内容")
    private String proDesc;

    @TableField(value = "PRO_SJFS")
    @Schema(name = "审计方式")
    private String proSjfs;

    @TableField(value = "AUDITSTAFFID")
    @Schema(name = "审计对象人")
    private Integer auditStaffId;

    @TableField(value = "SJLX")
    @Schema
    private String sjlx;

    @TableField(value = "SJZR")
    @Schema
    private String sjzr;

    @TableField(value = "YQJCQK")
    @Schema
    private String yqjcqk;

    @TableField(value = "EJFHR")
    @Schema
    private Integer ejfhr;

    @TableField(value = "TEMPLETEID")
    @Schema
    private Integer templeteId;

    @TableField(value = "STAFFID")
    @Schema
    private Integer staffId;

    @TableField(value = "AUDITORG")
    @Schema
    private String auditOrg;

    @TableField(value = "FPSTATUS")
    @Schema
    private Integer fpStatus;

    @TableField(value = "PLANPROJECTID")
    @Schema
    private Integer planProjectId;

    @TableField(value = "PPROJECTNAME")
    @Schema(name = "计划项目名称")
    private String pprojectName;

    @TableField(value = "TARGETNAME")
    @Schema(name = "工作目标")
    private String targetName;

    @TableField(value = "ORGIDS")
    @Schema(name = "组织id")
    private String orgIds;

    @TableField(value = "ORGIDNAMES")
    @Schema
    private String orgIdNames;

    @TableField(value = "EXTERNALASSIG")
    @Schema(name = "是否外委")
    private Integer externAlassig;

    @TableField(value = "PCOUNT")
    @Schema
    private Integer pCount;

    @TableField(value = "PSTATUS")
    @Schema
    private Integer pStatus;

    @TableField(value = "FILCODE")
    @Schema
    private String filCode;

    @TableField(value = "FILNAME")
    @Schema
    private String filName;

    @Schema(name = "项目经理实体")
    @Transient
    @IgnoreSwaggerParameter
    private Staff pmStaff;
    
    @Schema(name = "被审计单位实体")
    @Transient
    @IgnoreSwaggerParameter
    private TblOrganization auditOrgInfo;
    
    @TableField(value = "orgInfo")
    @Schema
    private TblOrganization orgInfo;

    @Schema(name="项目团队人员",hidden=true)
    @Transient
    private BigDecimal teamStaffId;
	
    @Schema(name="实施天数",hidden=true)
    @Transient
    private Integer days;
    
    @TableField(value = "realname")
    @Schema
    private String realname;
    
    @TableField(value = "")
    @Schema
    private String planName;
    
    @TableField(value = "")
    @Schema
    private String planCode;
    
    @TableField(exist = false)
    @Schema(name="审计模板",hidden=true)
    @IgnoreSwaggerParameter
    private TblNbsjTempleteEntity tbltemplete;//审计模板
    
    @TableField(exist = false)
    @Schema(name="指引模板",hidden=true)
    @IgnoreSwaggerParameter
    private TblNbsjTempleteEntity tbltempletezy;
    
    @TableField(value = "tblnbsjPlan")
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private TblNbsjAuditplan tblnbsjPlan;
    
    
    @TableField(value = "")
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private String planStartDate;
    
    @TableField(value = "")
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private String planEndDate;
    
    //浙资新增字段新增字段
    @TableField(value = "IMPLEMENTAION")
    @Schema(name = "实施主体")
    private String implementaion;
    
    @TableField(value = "COSPOMSORDEPARTMENT")
    @Schema(name = "协办部门id")
    private Integer cospomsordepartment;
    
    @TableField(value = "TIMEREQUIREMENT")
    @Schema(name = "实施时间要求")
    private String timerequirment;
    
    
    @TableField(value = "AUDITBASIS")
    @Schema(name = "审计依据")
    private String aduitbasis;
    
    
    @TableField(value = "IMPLEMENTAIONSTEPS")
    @Schema(name = "具体实施步骤")
    private String implementaionsteps;
    
    @TableField(value = "AUDITREQUIREMENTS")
    @Schema(name = "审计要求")
    private String auditrequirements;
    
    
    
    
    
    @TableField(value = "")
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private String createUserName;
    @TableField(value = "")
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private Integer isBmAudit;
    
    @TableField(value = "")
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private String auditOrgName;
    
    @TableField(value = "")
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private String auditStaffName;
    
    @TableField(value = "")
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private Set<TblAttachment> tblprojectAtts;
    
    @TableField(value = "")
    @Schema
    private Integer cyrrentStatre;//0 未切换  1切换
    
    @TableField(value = "")
    @Schema
    private String protempid;
    
    
}
