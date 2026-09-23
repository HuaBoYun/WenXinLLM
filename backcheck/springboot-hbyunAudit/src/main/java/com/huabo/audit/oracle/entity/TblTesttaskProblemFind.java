package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_TESTTASK_PROBLEMFIND")
public class TblTesttaskProblemFind implements Serializable {
	
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
    private Integer testYear;//

    @TableField("RISKNUMBERID")
    @Column(name = "RISKNUMBERID")
    @Schema(name="风险编号")
    private String risknumberid;
    
    @TableField("整改状态")
	@Column(name = "ZGSTATUS")
	@Schema(name = "整改状态  0未整改 1已整改")
    private Integer zgstatus;
    
    private String realname;
    
    private String orgname;

    private String riskid;

    private String risknumber;
    
    private String planid;
    
    private String plannumber;
    
    private String planname;

    @Schema(name = "被审计对象类型 1公司 2部门 3用户")
    @Transient
    private Integer auditObjectType;

}