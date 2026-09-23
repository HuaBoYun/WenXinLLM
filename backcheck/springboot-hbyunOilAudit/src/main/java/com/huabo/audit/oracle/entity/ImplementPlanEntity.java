package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Rui
 * @ClassName ProjectSortEntity
 * @Description
 * @DATE 2023/10/27
 */
@Data
@TableName("TBL_YQNS_IMPLEMENT_PLAN")
@Schema(name="项目管理-实施方案表")
@Accessors(chain = true)
public class ImplementPlanEntity extends BaseReservedProperty implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final Integer SHEETID = 163;
	
	public final static Integer NO_SELECT=0;//未切换
	public final static Integer YE_SELECT=1;//已切换
	public final static Integer UPDATENO = 0;//不完整
	public final static Integer UPDATEYES = 1;//完整

    public final static Integer COMPLETED = 6;//已完成
    @TableId(value="ID", type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;
    
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

    @TableField(value="PLAN_ID")
    @Schema(name="计划ID")
    private BigDecimal planId;

    @TableField(value="PLAN_PROJECT_ID")
    @Schema(name="计划项目ID")
    private BigDecimal planProjectId;

    @TableField(value="PLAN_NAME")
    @Schema(name="计划名称")
    private String planName;

    @TableField(value="PLAN_PROJECT_NAME")
    @Schema(name="计划项目名称")
    private String planProjectName;

    @TableField(value="PROJECT_NAME")
    @Schema(name="项目名称")
    private String projectName;

    @TableField(value="PROJECT_TYPE")
    @Schema(name="项目类别 1.计划内 2.计划外")
    private Integer projectType;

    @TableField(value="AUDIT_ORG_ID")
    @Schema(name="被审计对象")
    private String auditOrgId;

    @TableField(value="AUDIT_ORG_NAME")
    @Schema(name="被审计对象名称")
    private String auditOrgName;

    @TableField(value="PLAN_YEAR")
    @Schema(name="计划年度")
    private Integer planYear;

    @TableField(value="PLAN_TIME")
    @Schema(name="时间安排")
    private String planTime;

    @TableField(value="PROJECT_SUMMARY")
    @Schema(name="项目概述")
    private String projectSummary;

    @TableField(value="PROJECT_ORDER_ID")
    @Schema(name="项目负责人")
    private BigDecimal projectOrderId;

    @TableField(value="PROJECT_ORDER_NAME")
    @Schema(name="项目负责人名字")
    private String projectOrderName;

    @TableField(value="PLAN_STARTTIME")
    @Schema(name="项目计划起始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date planStarttime;

    @TableField(value="PLAN_ENDTIME")
    @Schema(name="项目计划结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date planEndtime;

    @TableField(value="AUDIT_METHOD")
    @Schema(name="审计方式 1.现场 2.非现场 3.非现场与现场结合")
    private Integer auditMethod;

    @TableField(value="COST_ESTIMATION")
    @Schema(name="项目费用估算")
    private Integer costEstimation;

    @TableField(value="IS_WW")
    @Schema(name="是否外委 1.是 0 否")
    private Integer isWw;

    @TableField(value="TEMP_ID")
    @Schema(name="审计模板ID") 
    private BigDecimal tempId;

    @TableField(value="TEMP_NAME") 
    @Schema(name="审计模板名称")
    private String tempName;
 
    @TableField(value="PROJECTTEMP_ID")
    @Schema(name="工程模版ID")
    private BigDecimal projecttempId;

    @TableField(value="PROJECTTEMP_NAME")
    @Schema(name="工程模版名称")
    private String projecttempName;

    @TableField(value="SJLX_ID")
    @Schema(name="审计类型ID")
    private BigDecimal sjlxId;

    @TableField(value="SJLX_NAME")
    @Schema(name="审计类型名称")
    private String sjlxName;

    @TableField(value="IMPLEMENT_TYPE")
    @Schema(name="实施主体 1.自主实施 2.委外实施 3.联合外部机构实施")
    private Integer implementType;

    @TableField(value="DEPT_ID")
    @Schema(name="协办部门ID")
    private BigDecimal deptId;

    @TableField(value="DEPT_NAME")
    @Schema(name="协办部门名称")
    private String deptName;

    @TableField(value="IMPLEMENT_STEPS")
    @Schema(name="具体实施步骤")
    private String implementSteps;

    @TableField(value="AUDIT_REQUIREMENT")
    @Schema(name="审计要求")
    private String auditRequirement;

    @TableField(value="AUDIT_REASON")
    @Schema(name="审计目标和范围")
    private String auditReason;

    @TableField(value="AUDIT_CONTENT")
    @Schema(name="审计内容和重点")
    private String auditContent;

    @TableField(value="AUDIT_PROCESS")
    @Schema(name="审计程序和方法")
    private String auditProcess;

    @TableField(value="AUDIT_RESULT_USE")
    @Schema(name="对专家和外部审计结果的利用")
    private String auditResultUse;

    @TableField(value="SPZT")
    @Schema(name="审批状态")
    private String spzt;

    @TableField(value="OTHER_CONTENT")
    @Schema(name="其他有关内容")
    private String otherContent;

    @Schema(name = "小组集合")
    @TableField(exist = false)
    private List<ImplementPlanTeamEntity> teams;

    @Schema(name = "附件主键集合")
    @TableField(exist = false)
    private String attIds;

    @Schema(name = "附件集合")
    @TableField(exist = false)
    private List<TblAttachment> attachments;
    
   
    
    
    @TableField(value = "FPSTATUS")
    @Schema(name = "项目任务分配状态：0未分配 1 分配中 2 已分配 3 分配完成 ")
    private Integer fpStatus;
    
    @TableField(value = "UPDATESTATUS")
    @Schema(name = "修改模板时间")
    private Integer updateStatus;
    
    @TableField(value = "CURRENTSTATRE")
    @Schema(name = "选择的项目")
    private Integer currentStatre;
    
    
    @TableField(value = "STATUS")
    @Schema(name = "状态:1启动 0未启动 2实施 3完成 4归档 5停止")
    private Integer status;
    
    @TableField(value = "IMPLEMENTTIME")
    @Schema(name = "实施时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date implementtime;
    
    
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private Integer isBmAudit;
    
    
    
    @TableField(value="ZSSTAFFID")
    @Schema(name="主审ID")
    private BigDecimal zsstaffid;
    
    @TableField(value="ZSNAME")
    @Schema(name="主审名称")
    private String zsname;
    
    
    
    @TableField(value="FZSTAFFID")
    @Schema(name="助审ID")
    private BigDecimal fzstaffid;
    
    @TableField(value="FZNAME")
    @Schema(name="助审名称")
    private String fzname;
    
    
    @TableField(value="ISJY")
    @Schema(name="是否境外")
    private String isjy;
    
    
    
    @Schema(name = "关联项目启动主键")
    @TableField(value = "XMDQID")
    private BigDecimal xmdqid;
    
    
    @TableField(exist = false)
    private TblYqnsXmdq xmqd;
    
    
    @Schema(name = "项目名称")
    @TableField(value = "XMNAME")
    private String xmname;
    
    @Schema(name = "编号")
    @TableField(value = "QDCODE")
    private String qdcode;
    
    
    @TableField(value="ISGC")
    @Schema(name="是否工程：0其他项目 1工程项目")
    private String isgc;
    
    @TableField(value="CREATEDATE")
    @Schema(name="创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createdate;
    
    
    @TableField(value="UPDATEDATE")
    @Schema(name="修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updatedate;
    
    @TableField(value="YQDATE")
    @Schema(name="延期时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date yqdate;
    
    @TableField(value = "GDSTATUS")
    @Schema(name = "归档审批状态")
    private Integer gdstatus;
    
    @TableField(exist = false)
    @Schema(name = "借阅次数")
    private Integer pcnt;
    
    //借阅状态
    @TableField(exist = false)
    @Schema(name = "借阅状态")
    private Integer jystatus;
    
    
    @TableField(value="ZYKSRYIDS")
    @Schema(name="专业科室人员id")
    private String zyksryids;
    
    @TableField(value="ZYKSRYRWNAMES")
    @Schema(name="专业科室人员名称")
    private String zyksryrwnames;
    
    @TableField(value="ZYKSTYPE")
    @Schema(name="方案所属类型:工程、基建、专项")
    private String zykstype;
    
    
    @Schema(name = "关联项目安排表ID")
    @TableField(value = "XMAPBID")
    private BigDecimal xmapbid;
    
    @TableField(exist = false)
    @Schema(name = "组长主键")
    private BigDecimal leaderId;
    
    @TableField(exist = false)
    @Schema(name = "组长名称")
    private String leaderName;
    
    @TableField(exist = false)
    @Schema(name = "副组长主键")
    private BigDecimal fzzStafffId;
    
    @TableField(exist = false)
    @Schema(name = "副组长名称")
    private String fzzName;
    
    @TableField(exist = false)
    @Schema(name = "专业科室审核人")
    private String zyksAppStaffName;
    
    @TableField(exist = false)
    @Schema(name = "业务分管副主任")
    private String ywfgAppStaffName;
    
    @TableField(exist = false)
    @Schema(name = "终稿时间")
    private Date endAppDate;
    
    @TableField(exist = false)
    @Schema(name = "审批意见")
    private String commont;
    
    @TableField(exist = false)
    @Schema(name = "提交时间")
    private Date startAppDate;
    
    
    @TableField(value="CREATESTAFFID")
    @Schema(name="创建人ID")
    private BigDecimal createstaffid;
    
    
    @TableField(exist = false)
    @Schema(name = "问题金额")
    private String wtje;
    
    @TableField(exist = false)
    @Schema(name = "查询筛选类型：sjtzsp-审计通知审批选择项目")
    private String xctype;
    
}
