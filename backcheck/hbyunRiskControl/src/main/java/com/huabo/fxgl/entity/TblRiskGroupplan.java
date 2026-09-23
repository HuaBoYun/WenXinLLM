package com.huabo.fxgl.entity;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Data
@TableName("TBL_RISK_GROUPPLAN")
@Schema(name="集团评估计划表")
public class TblRiskGroupplan extends FlexibleFieldEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public final static String PLANSTATUS_WKS  = "1";//未开始
    public final static String PLANSTATUS_PGZ  = "2"; //评估中
    public final static String PLANSTATUS_YWC  = "3"; //已完成
    /**
     * ID
     */
    @Schema(name="主键ID")
    @TableId(type = IdType.INPUT)
    private BigDecimal id;
    /**
     * 计划编号
     */
	@Schema(name="计划编号")
    private String plancode;
    /**
     * 计划名称
     */
	@Schema(name="计划名称")
    @TableField("planName")
    private String planName;
    /**
     * 计划开始时间
     */
	@Schema(name="计划开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField("startDate")
    private Date startDate;
    /**
     * 计划结束时间
     */
	@Schema(name="计划结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField("endDate")
    private Date endDate;
	
	

	
    /**
     * 计划状态
     */
	@Schema(name="计划状态  1-未开始 2-评估中 3-已完成")
    @TableField("planStatus")
    private String planStatus;
    /**
     * 公司ID
     */
	@Schema(name="公司ID")
    @TableField(value = "UNIT", property = "organization.orgid",exist = false)
    private Organization organization;

	@Schema
    private String unit;
    /**
     * 备注
     */
	@Schema(name="备注")
    private String memo;
    /**
     * 计划描述
     */
	@Schema(name="计划描述")
    private String plandes;
    /**
     * 计划类型
     */
	@Schema(name="计划类型 1-年度计划  2-临时性计划")
    @TableField("planType")
    private String planType;
	
	
    private String recorder;
    /**
     * 创建时间
     */
	@Schema(name="创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	 @TableField("RECORDDATE")
    private Date recorddate;
	
	
    private String asshead;
    /**
     * 评估标准ID
     */
	@Schema(name="评估标准ID")
    @TableField(value = "assstdid", property = "assessmentstd.assstdid",exist = false)
    private RiskAssessmentstd assessmentstd;

	
	@Schema(name="关联评估标准ID")
    @TableField("ASSSTDID")
    private BigDecimal assstdid;
	
	@Schema
    private String version;

//	@Schema(name="一个评估计划可能映射多个风险")
//    @TableField(exist = false)
//    private List<RiskAssplanRisk> riskAssplanRiskList;//一个评估计划可能映射多个风险

    @TableField(exist = false)
    private Set<Attachment> tblAttachments = new HashSet<Attachment>();

//    @TableField(exist = false)
//    private Set<RiskAssplanRisk> assPlanRisks = new TreeSet<RiskAssplanRisk>();



    @TableField(exist = false)
    private Integer count = 0;
    @TableField(exist = false)
    private Integer count1 = 0;
    @TableField(exist = false)
    private Integer count2= 0;
    @TableField(exist = false)
    private Integer count3= 0;
    @TableField(exist = false)
    private Integer count4= 0;
    @TableField(exist = false)
    private Integer count5= 0;
 
	
	
	@Schema(name="已评估字段")
    @TableField(exist = false)
	private String ypgStr;
	
	@Schema(name="未评估字段")
    @TableField(exist = false)
	private String wpgStr;
	
	@Schema(name="审批状态")
    @TableField("STATUS")
    private String status;
	
	@Schema
    private String content;
	
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
 
    @Schema(name = "所属部门")
    @TableField("LINKDEPTID")
     private BigDecimal linkdeptid;
    
    @Schema(name="创建人")
    @TableField("CREATESTAFFID")
    private BigDecimal createstaffid;
    
    @TableField(value = "ISSUEDDATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(name="下发时间")
    private Date issueddate;
 
    @TableField(value = "ISSUED_STAFFID")
    @Schema(name="下发人员id")
    private String issuedStaffid;
    
    @TableField(value = "ISSUED_STAFFNAME")
    @Schema(name="下发人员名字")
    private String issuedStaffName;

	@Schema(name="创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField("CREATETIME")
    private Date createtime;

    @Schema(name="是否下发 1 下发 0未下发")
    @TableField(value = "TOISSUED")
    private BigDecimal toIssued; 
    
    @TableField(value = "ISSUED_UNIT")
    @Schema(name="下发单位id--对应下发人员id")
    private String lssuedUnit;
    
    @TableField(value = "ISSUED_UNITNAME")
    @Schema(name="下发单位名称--对应下发人员")
    private String issuedUnitName;
}
