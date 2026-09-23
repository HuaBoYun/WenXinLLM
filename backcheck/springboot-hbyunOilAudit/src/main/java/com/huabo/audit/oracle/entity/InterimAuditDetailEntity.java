package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Rui
 * @ClassName InterimAuditDetailEntity
 * @Description
 * @DATE 2024/04/15
 */
@Data
@TableName("TBL_YQNS_INTERIM_AUDIT_DETAILS")
@Schema(name="任中审计明细表")
@Accessors(chain = true)
public class InterimAuditDetailEntity implements Serializable {

    @TableId(value="ID", type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;

    @TableField(value="SERIALNUMBER")
    @Schema(name="序号")
    private BigDecimal serialNumber;
    @TableField(value="ORDID")
    @Schema(name="单位")
    private BigDecimal orgId;

    @TableField(exist = false)
    private TblOrganization org;

    @TableField(value="AUDIT_INFO")
    @Schema(name="审计情况")
    private String auditInfo;

    @TableField(value="UNAUDIT_MONTH")
    @Schema(name="未审计月数")
    private Integer unauditMonth;  

    @TableField(value="UNAUDIT_YEAR") 
    @Schema(name="未审计年限")
    private String unauditYear;


//    @TableField(value="AUDIT_TIME")
//    @Schema(name="审计时间")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
//    private Date auditTime;
    @TableField(value="AUDIT_TIME")
    @Schema(name="审计时间")
    private String auditTime;

    @TableField(value="PROJECT_NAME")
    @Schema(name="项目名称")
    private String projectName;

//    @TableField(value="WORK_START_TIME")
//    @Schema(name="任职起始时间")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
//    private Date workStartTime;
    @TableField(value="WORK_START_TIME")
    @Schema(name="任职起始时间")
    private String workStartTime;

//    @TableField(value="WORK_END_TIME")
//    @Schema(name="任职结束时间")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
//    private Date workEndTime;
    @TableField(value="WORK_END_TIME")
    @Schema(name="任职结束时间")
    private String workEndTime;

//    @TableField(value="DO_AUDIT_TIME")
//    @Schema(name="审计实施时间")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
//    private Date doAuditTime;
    @TableField(value="DO_AUDIT_TIME")
    @Schema(name="审计实施时间")
    private String doAuditTime;

    @TableField(value="TEAM_LEADER_ID")
    @Schema(name="组长")
    private String teamLeaderId;

	@TableField(exist = false)
	private TblStaff teamLeader;

    @TableField(value="SUB_TEAM_LEADER_ID")
    @Schema(name="副组长")
    private String subTeamLeaderId;

    @TableField(exist = false)
    private TblStaff subTeamLeader;

    @TableField(value="LEADER_ID")
    @Schema(name="牵头人")
    private String leaderId;

	@TableField(exist = false)
	private TblStaff leader;

    @TableField(value="CHIEF_REVIEWER_ID")
    @Schema(name="主审")
    private String chiefReviewerId;

	@TableField(exist = false)
	private TblStaff chiefReviewer;

    @TableField(value="DEPUTY_REVIEWER_ID")
    @Schema(name="助审")
    private String deputyReviewerId;

	@TableField(exist = false)
	private TblStaff deputyReviewer;

    @TableField(value="PERSON_IDS")
    @Schema(name="下发的人员ID")
    private String personIds;

    @TableField(value="CREATE_USER")
    @Schema(name="创建人")
    private String createUserId;

	@TableField(exist = false)
	private TblStaff createUser;

//    @TableField(value="CREATE_TIME")
//    @Schema(name="创建时间")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
//    private Date createTime;
	@TableField(value="CREATE_TIME")
    @Schema(name="创建时间")
    private String createTime;

    @TableField(value = "STATUS")
    @Schema(name="状态")
    private Integer status;

    @TableField(value = "PROJECTTYPE")
    @Schema(name="是否建议审计")
    private String projectType;

    @TableField(value = "TEXTAREA")
    @Schema(name="原因")
    private String textarea;

    @TableField(value = "CREATEYEAR")
    @Schema(name="创建年度")
    private String createyear;

    @TableField(exist = false)
    @Schema(name="用户查看数据权限部门") 
    private String queryDeptIds;
    
    
    @TableField(exist = false)
    @Schema(name="关联填报的id")
    private BigDecimal tbid;
    
    @TableField(value = "PROJECTLX")
    @Schema(name="项目类型")
    private String projectlx;
    
    
    @TableField(value = "REMARKS")
    @Schema(name="备注")
    private String remarks ;
    
    @TableField(exist = false)
    @Schema(name="关联id")
    private BigDecimal relaid;
    
    
    @TableField(value="LDNAME")
    @Schema(name="现任领导名称")
    private String ldname;
    
    @TableField(value="LDZW")
    @Schema(name="职务")
    private String ldzw;
    
    @TableField(value="CSYM")
    @Schema(name="出生年月")
    private String csym;
    
    
    @TableField(value="YJEXSJ")
    @Schema(name="预计二线时间")
    private String yjexsj;
    
    @TableField(value="ZJPROJECTNAME")
    @Schema(name="最近一次审计项目名称")
    private String zjprojectname;
    
    
    @TableField(value="RZSJ")
    @Schema(name="预计二线时间")
    private String rzsj;
    
    
    @TableField(value="SJFWSTARTTIME")
    @Schema(name="审计范围开始时间（X年X月）")
    private String sjfwstarttime;
    
    
    
    @TableField(value="SJFWENDTIME")
    @Schema(name="审计范围结束时间（X年X月）")
    private String sjfwendtime;


    @TableField(exist = false)
    @Schema(name="ids")
    private String ids;
}

