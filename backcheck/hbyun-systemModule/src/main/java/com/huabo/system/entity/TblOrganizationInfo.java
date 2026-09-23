package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="审计机构管理", description="审计机构管理")
@TableName(value = "TBL_ORGANIZATION_INFO")
public class TblOrganizationInfo implements Serializable {
	 
	public final static String REGISTERUSERPASSWORD = "REDACTED";
	
	@TableId(value="INFOID",type = IdType.INPUT)
	@Schema(name="主键ID,自动增长")
	private BigDecimal infoid;//主键ID,自动增长
	
	@TableField(value = "UNIFIEDCODE")
	@Schema(name="统一社会信用代码")
	private String unifiedcode; 
	
	@TableField(value =  "NOTUNIFIEDCODE")
	@Schema(name="尚未领取统一社会信用代码的填写原组织机构代码")
	private String notunifiedcode; 
	@TableField(value =  "ORGNAME")
	@Schema(name="单位详细名称")
	private String orgname; 
	@TableField(value =  "ORGID")
	@Schema(name="单位ID")
	private BigDecimal orgid; 
	@TableField(value =  "ORGANIZATIONTYPE")
	@Schema(name="机构类型  10 企业 20 事业单位 30 机关 40 社会团体 51 民办非企业单位")
	private BigDecimal organizationtype; 
	@TableField(value =  "MAINACTIVITYONE")
	@Schema(name="主要活动1")
	private String mainactivityone; 
	@TableField(value =  "MAINACTIVITYTWO")
	@Schema(name="主要活动2")
	private String mainactivitytwo; 
	@TableField(value =  "MAINACTIVITYTHR")
	@Schema(name="主要活动3")
	private String mainactivitythr; 
	@TableField(value =  "INDUSTRYCODE")
	@Schema(name="行业代码")
	private String industrycode; 
	@TableField(value =  "PROVINCE")
	@Schema(name="省")
	private String province; 
	@TableField(value =  "LAND")
	@Schema(name="地")
	private String land;   
	@TableField(value =  "COUNTY")
	@Schema(name="县")
	private String county;   
	@TableField(value =  "COUNTRY")
	@Schema(name="乡")
	private String country;  
	@TableField(value =  "STREET")
	@Schema(name="街")
	private String street;  
	@TableField(value =  "SUBDISTRICTOFFICE")
	@Schema(name="街道办事处")
	private String subdistrictoffice;  
	@TableField(value =  "COMMUNITY")
	@Schema(name="社区")
	private String community;  
	@TableField(value =  "AREACODE")
	@Schema(name="区域代码")
	private String areacode;  
	@TableField(value =  "RURALCODE")
	@Schema(name="城乡代码")
	private String ruralcode;  
	@TableField(value =  "UNITSIZE")
	@Schema(name="单位规模  1 大型 2 中型 3 小型 4 微型")
	private BigDecimal unitsize;  
	@TableField(value =  "NUMBEREMPLOYEES")
	@Schema(name="从业人员期末人数")
	private BigDecimal numberemployees;  
	@TableField(value =  "LEGALREPRESENTATIVE")
	@Schema(name="法定代表人")
	private String legalrepresentative;  
	@TableField(value =  "ACCOUNTINGSTANDARDS")
	@Schema(name="执行会计标准类别1 企业会计准则制度 2 政府会计准则制度")
	private BigDecimal accountingstandards;  
	
	@TableField(value =  "EMAIL")
	@Schema(name="电子邮件")
	private String email;  
	@TableField(value =  "PHONE")
	@Schema(name="长途电话")
	private BigDecimal phone;  
	@TableField(value =  "FIXEDTELEPHONE")
	@Schema(name="固定电话")
	private String fixedtelephone;  
	@TableField(value =  "POSTALCODE")
	@Schema(name="邮政编码")
	private BigDecimal postalcode;  
	@TableField(value =  "WEBSITE")
	@Schema(name="网址")
	private String website;  
	@TableField(value =  "SFYSJFR")
	@Schema(name="本法人单位是否有上一级法人  1是 0否")
	private BigDecimal sfysjfr;  
	@TableField(value =  "UNIFIEDCODEONE")
	@Schema(name="上一级法人社会信用代码")
	private String unifiedcodeone;  
	@TableField(value =  "UNIFIEDCODETWO")
	@Schema(name="尚未领取统一社会信用代码的填写原组织机构代码")
	private String unifiedcodetwo;  
	@TableField(value =  "UNIFIEDCODETHR")
	@Schema(name="上一级法人单位单位详细名称")
	private String unifiedcodethr;  
	@TableField(value =  "WHETHERFIT")
	@Schema(name="是否设置总审计师  1 是 2 否")
	private BigDecimal whetherfit;  
	@TableField(value =  "POSITIONLEVEL")
	@Schema(name="1 领导班子成员 2 除领导班子成  总审计师职位层级   员以外的其他高级管理人员3 中层机构主要负责人或以下级别 4 其他")
	private BigDecimal positionlevel;  
	@TableField(value =  "APPOINTMENTMODE")
	@Schema(name="1 上一级法人单位委派 2 本单位自行产生（须报上级单位批准或备案）3 本单位自行产生（无须批准或备案） 4 其他")
	private BigDecimal appointmentmode;  
	 
	@TableField(value =  "IFSETUP")
	@Schema(name="是否设置内部审计机构  1 是 2 否")
	private BigDecimal ifsetup;  
	 
	@TableField(value =  "AUDITNAME")
	@Schema(name="内部审计机构名称")
	private String auditname;  
	 
	@TableField(value =  "LEADINGORGANIZATION")
	@Schema(name="内部审计工作的领导机构1 主要负责人分管 2 主要负责人分管（其他领导人员协管）3 其他班子成员分管 4 总审计师分管5 其他")
	private BigDecimal leadingorganization;  
	 
	@TableField(value =  "IFINDEPENDENTLY")
	@Schema(name="是否独立设置内部机构 1 是 2 否")
	private BigDecimal ifindependently;  
	 
	@TableField(value =  "FUNCTIONALDEPARTMENT")
	@Schema(name="如为2，则内部审计机构与以下哪些职能部门合并设置")
	private String functionaldepartment;   
	
	@TableField(value =  "INTERNALAUDIT")
	@Schema(name="内部审计机构层级  1 本单位二级部门 2 本单位所属独立核算的二级单位3 本单位二级部门的内设部门 4 其他")
	private BigDecimal internalaudit;   
	 
	@TableField(value =  "STATISTICAL")
	@Schema(name="统计负责人")
	private String statistical;   
	 
	@TableField(value =  "PREPARER")
	@Schema(name="填表人")
	private String preparer;   
	 
	@TableField(value =  "CONTACTNUMBER")
	@Schema(name="联系电话")
	private String contactnumber;   
	 
	@TableField(value =  "FILLINGDATE")
	@JSONField(format = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Schema(name="填表日期")
	private Date fillingdate;   
	 
 
 
}
