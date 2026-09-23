package com.huabo.monitor.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.monitor.vo.param.fieldActivationVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_TESTTASK_PROBLEMFIND")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblTesttaskProblemFind extends FlexibleFieldEntity implements Serializable {
	
	//测试任务-问题发现
	
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.INPUT)
    @Id
    @Column(name = "FINDID")
	@Schema(name = "主键")
    private BigDecimal findid;
    
    @TableField("TESTTASKID")
	@Column(name = "TESTTASKID")
	@Schema(name = "测试任务ID")
    private BigDecimal testtaskid;//
    
    @TableField("CREATESTAFFID")
	@Column(name = "CREATESTAFFID")
	@Schema(name = "创建人")
    private BigDecimal createstaffid;//

    @TableField("CREATETIME")
	@Column(name = "CREATETIME")
	@Schema(name = "创建时间")
    private Date createtime;
    
    @TableField("ONEPROCESS")
	@Column(name = "ONEPROCESS")
	@Schema(name = "一级流程")
    private String oneprocess;

    @TableField("PROBLEMMEMO")
	@Column(name = "PROBLEMMEMO")
	@Schema(name = "问题概述")
    private String problemmemo;
    
    @TableField("DEFECTMEMO")
	@Column(name = "DEFECTMEMO")
	@Schema(name = "缺陷具体描述")
    private String defectmemo;
    
    @TableField("PROBLEMTYPE")
	@Column(name = "PROBLEMTYPE")
	@Schema(name = "问题类别")
    private String problemtype;
    
    @TableField("DEFECTLEVEL")
	@Column(name = "DEFECTLEVEL")
	@Schema(name = "缺陷等级")
    private String defectlevel;
    
    @TableField("QUABASIS")
	@Column(name = "QUABASIS")
	@Schema(name = "定性依据")
    private String quabasis;
    
    @TableField("MAINORG")
	@Column(name = "MAINORG")
	@Schema(name = "主责部门")
    private BigDecimal mainorg;
    
    @TableField("FEEDBACK")
	@Column(name = "FEEDBACK")
	@Schema(name = "反馈意见")
    private String feedback;
    
    @TableField("REFORMPLAN")
	@Column(name = "REFORMPLAN")
	@Schema(name = "整改计划")
    private String reformplan;
    
    @TableField("ESTFINISHDATE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "ESTFINISHDATE")
	@Schema(name = "预计完成时间")
    private Date estfinishdate;
    
    @TableField("REFORMSTAFFID")
	@Column(name = "REFORMSTAFFID")
	@Schema(name = "落实整改人")
    private BigDecimal reformstaffid;
    
    @TableField("STATUS")
	@Column(name = "STATUS")
	@Schema(name = "审批状态  1 审批中 2已退回 3已撤销 4 已终止 5已跟踪 6")
    private BigDecimal status;
    
    
    @TableField("DEFECTTYPE")
  	@Column(name = "DEFECTTYPE")
    @Schema(name="缺陷类型")
    private String defecttype;
    
    @TableField("LINKORG")
  	@Column(name = "LINKORG")
    @Schema(name="关联公司")
    private BigDecimal linkOrg;
    
    @TableField("CONTENT")
  	@Column(name = "CONTENT")
    @Schema(name="富文本")
    private String content;
    
    
    @TableField("TESTYEAR")
   	@Column(name = "TESTYEAR")
   	@Schema(name = "内控评价年度")
    private int testYear;//

    @TableField("RISKNUMBERID")
    @Column(name = "RISKNUMBERID")
    @Schema(name="风险编号")
    private String risknumberid;
    
    @TableField("ZGSTATUS")
	@Column(name = "ZGSTATUS")
	@Schema(name = "整改状态  0未整改 1已整改")
    private Integer zgstatus;
    
    @TableField(exist=false)
	@Schema(name = "查询条件")
    private Integer authorityType;
    
    //密级及查询条件
    @Schema(name = "密级主键")
    @TableField("SECRECTLEVELID")
    private BigDecimal secrectLevelId;
    
    @Schema(name = "知悉范围id")
    @TableField("STAFFSCOPEIDS")
    private String staffScopeIds;
    
    @Schema(name = "知悉范围名称")
    @TableField("STAFFSCOPENAMES")
    private String staffScopeNames;
    
//    @TableField("USESECRECT")
//    @Column(name = "USESECRECT")
//    @Schema(name = "是否使用密级 0 不使用；1 使用")
//    private Integer useSecrect;
//    
//    @TableField("SECRECTSTAFF")
//    @Column(name = "SECRECTSTAFF")
//    @Schema(name = "创建人-密级用")
//    private BigDecimal secrectStaff;
//    
//    @TableField("SECRECTSCOPEIDS")
//    @Column(name = "SECRECTSCOPEIDS")
//    @Schema(name = "当前用户所能查看密级数据的范围主键")
//    private String secrectScopeIds;
// 
    @Schema(name = "所属部门")
    @TableField("LINKDEPTID")
    @Column(name = "LINKDEPTID")
    private BigDecimal linkDeptId;
    //---------------------------中核新增整改字段
    @Schema(name = "整改措施")
    @TableField("MEASURES")
    @Column(name = "MEASURES")
    private String measures;
    
    @Schema(name = "整改时限start")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("DEADLINESTART")
    @Column(name = "DEADLINESTART")
    private Date deadlineStart;
    
    @Schema(name = "整改时限end")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("DEADLINEEND")
    @Column(name = "DEADLINEEND")
    private Date deadlineEnd;
    
    @Schema(name = "整改进展情况")
    @TableField("RECTIFICATION")
    @Column(name = "RECTIFICATION")
    private String rectification;
    
    @Schema(name = "完成状态")
    @TableField("RECTIFICATIONSTATUS")
    @Column(name = "RECTIFICATIONSTATUS")
    private int rectificationStatus;
    
    
    @TableField(value = "ISSUEDDATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(name="下发时间")
    private Date issuedDate;
    
    @TableField(value = "ISSUEDSTAFFID")
    @Schema(name="下发人员")
    private BigDecimal issuedStaffid;
    
    @TableField(exist=false)
    @Schema(name="下发人员名称")
    private String issuedStaffidName;
    
    
    @TableField(exist=false)
    private String realname;
    
    @TableField(exist=false)
    private String orgname;

    @TableField(exist=false)
    private String riskid;

    @TableField(exist=false)
    private String risknumber;
    
    @TableField(exist=false)
    private String planid;
    
    @TableField(exist=false)
    private String plannumber;
    
    @TableField(exist=false)
    private String planname;


}