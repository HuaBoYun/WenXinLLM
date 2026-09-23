package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.Id;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

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
    @TableId(value = "PROJECTID", type= IdType.INPUT)
    @Schema(name = "审计项目ID")
    private BigDecimal projectId;

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
   @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date startDate;

    @TableField(value = "ENDDATE")
    @Schema(name = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date endDate;

    @TableField(value = "PMID")
    @Schema(name = "项目经理（负责人）")
    private BigDecimal pmId;

    @TableField(value = "TEMPID")
    @Schema(name = "审计模板id")
    private BigDecimal tempId;

    @TableField(value = "COSTS")
    @Schema(name = "费用")
    private BigDecimal costs;

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
    private BigDecimal umpireId;

    @TableField(value = "CONTROLID")
    @Schema(name = "质控经理")
    private BigDecimal controlId;

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
    private BigDecimal createStaffId;

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
    private BigDecimal planId;

    @TableField(value = "AUDITORGID")
    @Schema(name = "审计对象组织/被审计单位组织id")
    private BigDecimal auditOrgId;

    @TableField(value = "TEMPZYID")
    @Schema(name = "指引模板")
    private BigDecimal tempzyId;

    @TableField(value = "UPDATESTATUS")
    @Schema(name = "修改模板时间")
    private Integer updateStatus;

    @TableField(value = "ORGID")
    @Schema(name = "隶属组织")
    private BigDecimal orgId;

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
    private BigDecimal auditStaffId;

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
    private BigDecimal templeteId;

    @TableField(value = "STAFFID")
    @Schema
    private BigDecimal staffId;

    @TableField(value = "AUDITORG")
    @Schema
    private String auditOrg;

    @TableField(value = "FPSTATUS")
    @Schema(name = "项目任务分配状态：0未分配 1 分配中 2 已分配 3 分配完成")
    private Integer fpStatus;

    @TableField(value = "PLANPROJECTID")
    @Schema
    private BigDecimal planProjectId;

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
    private BigDecimal externAlassig;

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
    @TableField(exist = false)
    private TblStaff pmStaff;
    
    @Schema(name = "被审计单位实体")
    @Transient
    @IgnoreSwaggerParameter
    @TableField(exist = false)
    private TblOrganization auditOrgInfo;
    
    @Schema
    @TableField(exist = false)
    private TblOrganization orgInfo;

    @Schema(name="项目团队人员",hidden=true)
    @Transient
    @TableField(exist = false)
    private BigDecimal teamStaffId;
	
    @Schema(name="实施天数",hidden=true)
    @Transient
    @TableField(exist = false)
    private Integer days;

   @Schema(name="被审计单位",hidden=true)
   @Transient
   @TableField(exist = false)
   private String ORGNAME;

   @Schema(name="待整改问题",hidden=true)
   @Transient
   @TableField(exist = false)
   private Integer FQZGS;

   @Schema(hidden=true)
   @Transient
   @TableField(exist = false)
   private Integer RN;

   @Schema(name="发现问题数量",hidden=true)
   @Transient
   @TableField(exist = false)
   private Integer WTZS;

   @Schema(name="未整改",hidden=true)
   @Transient
   @TableField(exist = false)
   private Integer WZG;
   @Schema(name="已整改",hidden=true)
   @Transient
   @TableField(exist = false)
   private Integer YZG;




    @Schema(name = "借阅次数")
    @Transient
    @TableField(exist = false)
    private Integer pcount;


    
    @TableField(value = "realname")
    @Schema
    private String realname;

    @TableField(exist = false)
    @Schema
    private String planName;

    @TableField(exist = false)
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

    @TableField(exist = false)
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private TblNbsjAuditplan tblnbsjPlan;


    @TableField(exist = false)
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private String planStartDate;

    @TableField(exist = false)
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
    
    @TableField(value = "IMPLEMENTAIONSTEPS")
    @Schema(name = "具体实施步骤")
    private String implementaionsteps;
    
    @TableField(value = "AUDITREQUIREMENTS")
    @Schema(name = "审计要求")
    private String auditrequirements;
    
    
    @TableField(value = "PROJECTTYPE")
    @Schema(name = "项目类别（计划内/计划外）")
    private String projecttype;
    
    
    
    
    @TableField(value = "TIMEREQUIREMENT")
    @Schema(name="实施时间要求",hidden=true)
    private String timerequirment;
    
    @Schema(name = "协办部门实体")
    @TableField(exist = false)
    private TblOrganization cosdepartemnt;
    
    
    @TableField(value = "AUDITBASIS")
    @Schema(name="审计依据",hidden=true)
    private String aduitbasis;
   
    
    
    
    @Schema(name = "审计分析-数量")
    private Integer sl;
    
    @Schema(name = "审计分析-审批状态")
    private String examineTypes;

 @TableField(exist = false)
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private String createUserName;
    @TableField(exist = false)
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private Integer isBmAudit;

    @TableField(exist = false)
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private String auditOrgName;

    @TableField(exist = false)
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private String auditStaffName;

    @TableField(exist = false)
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private Set<TblAttachment> tblprojectAtts;

    @TableField(exist = false)
    @Schema
    private Integer cyrrentStatre;//0 未切换  1切换

    @TableField(exist = false)
    @Schema
    private String protempid;
    
    @TableField(exist = false)
    @Schema(name = "项目经理名称")
    private String pmname;
    
    @TableField(exist = false)
    @Schema(name = "项目实施天数")
    private String daynumber;
    
    
    @TableField(exist = false)
    @Schema(name = "问题标题")
    private String questitle;
    
    @TableField(exist = false)
    @Schema(name = "审计发现")
    private String auditdiscoverable;
    
    
    @TableField(exist = false)
    @Schema(name = "发现人")
    private String findrealname;
    
    @TableField(exist = false)
    @Schema(name = "底稿问题类型")
    private String internalType;
    
    
    @TableField(value = "CNTTYPE")
    @Schema(name = "审计类型（国资委）")
    private String cntType;
    
    
    @TableField(value = "SJAP")
    @Schema(name = "时间安排")
    private String sjap;
    
    @TableField(value = "XMGS")
    @Schema(name = "项目概述")
    private String xmgs;
    
    @TableField(value = "COSPOMSORDEPARTMENTSTAFFID")
    @Schema(name = "协办部门负责人")
    private BigDecimal cospomsordepartmentstaffid;
    
    //中核新增20250101
    @TableField(value = "PROJECTORGID")
    @Schema(name = "项目单位ID")
    private String projectorgid;
    
    @TableField(value = "PROJECTORGNAME")
    @Schema(name = "项目单位名称")
    private String projectorgname;
    
    @TableField(value = "APPPROYEARSTART")
    @Schema(name = "批复的项目起止年限—开头")
    private String appproyearstart;

    @TableField(value = "APPPROYEAREND")
    @Schema(name = "批复的项目起止年限—结尾")
    private String appproyearend;


    @TableField(value = "ACTPROYEARSTART")
    @Schema(name = "实际的起止年限—开头")
    private String actproyearstart;
    @TableField(value = "ACTPROYEAREND")
    @Schema(name = "实际的起止年限—结尾")
    private String actproyearend;
    
    @TableField(value = "PROJECTMGDEPTID")
    @Schema(name = "项目主管部门ID")
    private String projectmgdeptid;
    
    @TableField(value = "PROJECTMGDEPTNAME")
    @Schema(name = "项目主管部门名称")
    private String projectmgdeptname;
    
    @TableField(value = "PROJECTORGADDRESS")
    @Schema(name = "项目单位地址")
    private String projectorgaddress;
    
    @TableField(value = "PROJECTLINKMAN")
    @Schema(name = "项目联系人")
    private String projectlinkman;
    
    @TableField(value = "PROJECTLINKTEL")
    @Schema(name = "联系电话")
    private String projectlinktel;
    
    @TableField(value = "SECRETLEVEL")
    @Schema(name = "密级")
    private String secretlevel;

    @Schema(name = "密级主键")
    @TableField("SECRECTLEVELID")
    private BigDecimal secrectLevelId;

    @Schema(name = "知悉范围 多个逗号分隔")
    @TableField("STAFFSCOPEIDS")
    private String staffScopeIds;

    @Schema(name = "知悉访问人员姓名 多个逗号分隔")
    @TableField("STAFFSCOPENAMES")
    private String staffScopeNames;
    
    @Schema(name = "消息下发拼接字符串")
    @TableField(exist=false)
    private String jsonString;
    
}
