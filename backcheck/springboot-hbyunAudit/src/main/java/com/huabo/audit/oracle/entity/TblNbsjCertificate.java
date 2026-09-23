package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "TBL_NBSJ_CERTIFICATE")
@Schema(name="审计取证单实体")
public class TblNbsjCertificate {

	public final static Integer STATE1 = 1;//未审核
	public final static Integer STATE2 = 2;//审核中
	public final static Integer STATE3 = 3;//审核驳回
	public final static Integer STATE4 = 4;//审核完成
	public final static Integer STATE5 = 5;//需调整
	
	@Column(name = "CERTIFICATEID")
	@TableId(value = "CERTIFICATEID", type= IdType.INPUT)
	@Id
    //@KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
	@Schema
	private BigDecimal certificateId;
	
	@Column(name = "PROJECTID")
	@Schema(name = "项目id")
	private BigDecimal projectId;
	
	@Column(name = "ORGID")
	@Schema
	private BigDecimal orgId;
	
	@Column(name =  "STATUS")
	@Schema
	private Integer status;
	
	@Column(name =  "AUDITABSTRACT")
	@Schema(name = "审计事项摘要")
	private String auditAbstract;
	
	@Column(name =  "EVIDENCEOPINION")
	@Schema(name = "证据提供单位意见")
	private String evidenceOpinion;
	
	@Column(name =  "CERTIFICATESTAFFID")
	@Schema(name = "证据提供者id")
	private BigDecimal certificateStaffId;
	
	@Column(name =  "CERTIFICATEUSER")
	@Schema(name = "证据提供者")
	private String certificateUser;
	
	@Column(name =  "CERTIFICATEDATE")
	@Schema(name = "日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
	private Date certificateDate;
	
	@Column(name =  "AUDITMATTER")
	@Schema(name = "审计事项")
	private String auditMatter;
	
	@Column(name =  "AUDITUSERID")
	@Schema(name = "审计人员id(创建人)")
	private BigDecimal auditUserId;
	
	@Schema(name="审计人员",hidden=true)
	 @Transient
	private String auditUserName;
	
	@Schema(name="项目名称",hidden=true)
	 @Transient
	private String projectName;
	
	@Column(name =  "CREATEDATE")
	@Schema(name="审计日期",hidden=true)
	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
	private Date createDate;
	
	@Schema(name="项目名称",hidden=true)
	@Transient
	private String prjoectname;
	
	@Schema(name="被审计单位",hidden=true)
	 @Transient
	private String auditOrgName;
	
	@Schema(name="被审计人员",hidden=true)
	 @Transient
	private String auditStaffName;

	@Column(name =  "ASSISTEDBMFZR")
	@Schema(name = "协办部门负责人")
	private String assistedbmfzr;

	@Column(name =  "ASSISTEDBMFZRID")
	@Schema(name = "协办部门负责人id")
	private BigDecimal assistedbmfzrid;

	@Column(name =  "ASSISTEDFGLD")
	@Schema(name = "协办部门分管领导")
	private String assistedfgld;

	@Column(name =  "ASSISTEDFGLDID")
	@Schema(name = "协办部门分管领导id")
	private BigDecimal assistedfgldid;

	@Column(name =  "ASSISTEDZBUSER")
	@Schema(name = "总部审计部门人员")
	private String assistedzbuser;

	@Column(name =  "ASSISTEDZBUSERID")
	@Schema(name = "总部审计部门人员id")
	private BigDecimal assistedzbuserid;

	
	 @Column(name =  "FIRSTSTAFFID")
	 @Schema(name = "第一复核人id")
	 private BigDecimal firststaffid;
	 
	 @Column(name =  "YJFH")
	 @Schema(name = "一级复核人姓名")
	 private String yjfh;
	    
	 @Column(name =  "SECONDSTAFFID")
	 @Schema(name = "第二复核人id")
	 private BigDecimal secondstaffid;
	 
	 @Column(name =  "EJFH")
	 @Schema(name = "二级复核人姓名")
	 private String ejfh;

	 @Column(name =  "ORGIDS")
	 @Schema(name = "被审计对象id")
	 private String orgIds;

	 @Column(name =  "ORGIDNAMES")
	 @Schema(name = "被审计对象名称")
	 private String orgIdNames;
	 
	@Schema(name = "密级主键")
    @TableField("SECRECTLEVELID")
    @Column(name = "SECRECTLEVELID")
    private BigDecimal secrectLevelId;
    
    @Schema(name = "知悉范围 多个逗号分隔")
    @TableField("STAFFSCOPEIDS")
    @Column(name = "STAFFSCOPEIDS")
    private String staffScopeIds;
    
    @Schema(name = "知悉访问人员姓名 多个逗号分隔")
    @TableField("STAFFSCOPENAMES")
    @Column(name = "STAFFSCOPENAMES")
    private String staffScopeNames;

	//创建人id
	@TableField("CREATESTAFFID")
	private BigDecimal createstaffid;
    
}
