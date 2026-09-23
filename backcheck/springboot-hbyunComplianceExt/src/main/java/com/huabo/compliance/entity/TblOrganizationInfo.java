package com.huabo.compliance.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "TBL_ORGANIZATION_INFO")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="审计机构管理", description="审计机构管理")
public class TblOrganizationInfo implements Serializable {
	 
	public final static String REGISTERUSERPASSWORD = "REDACTED";
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")  
	@TableId("INFOID")
	@Schema(name = "主键ID,自动增长")
	private BigDecimal infoid;//主键ID,自动增长
	
	@Column(name = "UNIFIEDCODE")
	@Schema(name = "统一社会信用代码")
	private String unifiedcode; 
	
	@Column(name = "NOTUNIFIEDCODE")
	@Schema(name = "尚未领取统一社会信用代码的填写原组织机构代码")
	private String notunifiedcode; 
	@Column(name = "ORGNAME")
	@Schema(name = "单位详细名称")
	private String orgname; 
	@Column(name = "ORGID")
	@Schema(name = "单位ID")
	private BigDecimal orgid; 
	@Column(name = "ORGANIZATIONTYPE")
	@Schema(name = "机构类型  10 企业 20 事业单位 30 机关 40 社会团体 51 民办非企业单位")
	private BigDecimal organizationtype; 
	@Column(name = "MAINACTIVITYONE")
	@Schema(name = "主要活动1")
	private String mainactivityone; 
	@Column(name = "MAINACTIVITYTWO")
	@Schema(name = "主要活动2")
	private String mainactivitytwo; 
	@Column(name = "MAINACTIVITYTHR")
	@Schema(name = "主要活动3")
	private String mainactivitythr; 
	@Column(name = "INDUSTRYCODE")
	@Schema(name = "行业代码")
	private String industrycode; 
	@Column(name = "PROVINCE")
	@Schema(name = "省")
	private String province; 
	@Column(name = "LAND")
	@Schema(name = "地")
	private String land;   
	@Column(name = "COUNTY")
	@Schema(name = "县")
	private String county;   
	@Column(name = "COUNTRY")
	@Schema(name = "乡")
	private String country;  
	@Column(name = "STREET")
	@Schema(name = "街")
	private String street;  
	@Column(name = "SUBDISTRICTOFFICE")
	@Schema(name = "街道办事处")
	private String subdistrictoffice;  
	@Column(name = "COMMUNITY")
	@Schema(name = "社区")
	private String community;  
	@Column(name = "AREACODE")
	@Schema(name = "区域代码")
	private String areacode;  
	@Column(name = "RURALCODE")
	@Schema(name = "城乡代码")
	private String ruralcode;  
	@Column(name = "UNITSIZE")
	@Schema(name = "单位规模  1 大型 2 中型 3 小型 4 微型")
	private BigDecimal unitsize;  
	@Column(name = "NUMBEREMPLOYEES")
	@Schema(name = "从业人员期末人数")
	private BigDecimal numberemployees;  
	@Column(name = "LEGALREPRESENTATIVE")
	@Schema(name = "法定代表人")
	private String legalrepresentative;  
	@Column(name = "ACCOUNTINGSTANDARDS")
	@Schema(name = "执行会计标准类别1 企业会计准则制度 2 政府会计准则制度")
	private BigDecimal accountingstandards;  
	
	@Column(name = "EMAIL")
	@Schema(name = "电子邮件")
	private String email;  
	@Column(name = "PHONE")
	@Schema(name = "长途电话")
	private BigDecimal phone;  
	@Column(name = "FIXEDTELEPHONE")
	@Schema(name = "固定电话")
	private String fixedtelephone;  
	@Column(name = "POSTALCODE")
	@Schema(name = "邮政编码")
	private BigDecimal postalcode;  
	@Column(name = "WEBSITE")
	@Schema(name = "网址")
	private String website;  
	@Column(name = "SFYSJFR")
	@Schema(name = "本法人单位是否有上一级法人  1是 0否")
	private BigDecimal sfysjfr;  
	@Column(name = "UNIFIEDCODEONE")
	@Schema(name = "上一级法人社会信用代码")
	private String unifiedcodeone;  
	@Column(name = "UNIFIEDCODETWO")
	@Schema(name = "尚未领取统一社会信用代码的填写原组织机构代码")
	private String unifiedcodetwo;  
	@Column(name = "UNIFIEDCODETHR")
	@Schema(name = "上一级法人单位单位详细名称")
	private String unifiedcodethr;  
	@Column(name = "WHETHERFIT")
	@Schema(name = "是否设置总审计师  1 是 2 否")
	private BigDecimal whetherfit;  
	@Column(name = "POSITIONLEVEL")
	@Schema(name = "1 领导班子成员 2 除领导班子成  总审计师职位层级   员以外的其他高级管理人员3 中层机构主要负责人或以下级别 4 其他")
	private BigDecimal positionlevel;  
	@Column(name = "APPOINTMENTMODE")
	@Schema(name = "1 上一级法人单位委派 2 本单位自行产生（须报上级单位批准或备案）3 本单位自行产生（无须批准或备案） 4 其他")
	private BigDecimal appointmentmode;  
	 
	@Column(name = "IFSETUP")
	@Schema(name = "是否设置内部审计机构  1 是 2 否")
	private BigDecimal ifsetup;  
	 
	@Column(name = "AUDITNAME")
	@Schema(name = "内部审计机构名称")
	private String auditname;  
	 
	@Column(name = "LEADINGORGANIZATION")
	@Schema(name = "内部审计工作的领导机构1 主要负责人分管 2 主要负责人分管（其他领导人员协管）3 其他班子成员分管 4 总审计师分管5 其他")
	private BigDecimal leadingorganization;  
	 
	@Column(name = "IFINDEPENDENTLY")
	@Schema(name = "是否独立设置内部机构 1 是 2 否")
	private BigDecimal ifindependently;  
	 
	@Column(name = "FUNCTIONALDEPARTMENT")
	@Schema(name = "如为2，则内部审计机构与以下哪些职能部门合并设置")
	private String functionaldepartment;   
	
	@Column(name = "INTERNALAUDIT")
	@Schema(name = "内部审计机构层级  1 本单位二级部门 2 本单位所属独立核算的二级单位3 本单位二级部门的内设部门 4 其他")
	private BigDecimal internalaudit;   
	 
	@Column(name = "STATISTICAL")
	@Schema(name = "统计负责人")
	private String statistical;   
	 
	@Column(name = "PREPARER")
	@Schema(name = "填表人")
	private String preparer;   
	 
	@Column(name = "CONTACTNUMBER")
	@Schema(name = "联系电话")
	private String contactnumber;   
	 
	@Column(name = "FILLINGDATE")
	@TableField("FILLINGDATE")
	@JSONField(format = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Schema(name = "填表日期")
	private Date fillingdate;   
	 
 
 
}
