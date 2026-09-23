package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Rui
 * @ClassName LeaveAudit3LEntity
 * @Description
 * @DATE 2023/9/14
 */
@Data
@TableName("TBL_YQNS_LEAVE_AUDIT_3L")
@KeySequence(value = "HIBERNATE_SEQUENCE")
@Schema(name="三级单位离任审计")
@Accessors(chain = true)
public class LeaveAudit3LEntity extends BaseReservedProperty implements Serializable {

    @Bean
    public OracleKeyGenerator genkey() {
        return new OracleKeyGenerator();
    }

    @TableId(value="ID", type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;

    @TableField(value="NAME") 
    @Schema(name="姓名")
    private String name;
 
    @TableField(value="OLD_JOB")
    @Schema(name="原职务")
    private String oldJob;


    @TableField(value="OLD_LEVEL")
    @Schema(name="原行政级别")
    private String oldLevel;

    @TableField(value="OLD_ORG_ID")
    @Schema(name="原单位")
    private String oldOrgId;

	@TableField(exist = false)
	private TblOrganization oldOrg;

    @TableField(value="OLD_JOB_START_TIME")
    @Schema(name="原任职开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date oldJobStartTime;



    @TableField(value="OLD_JOB_END_TIME")
    @Schema(name="原任职结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date oldJobEndTime;



    @TableField(value="JOB")
    @Schema(name="现职务")
    private String job;


    @TableField(value="LEVELS")
    @Schema(name="现行政级别")
    private String levels;

    @TableField(value="ORG_ID")
    @Schema(name="现单位")
    private String orgId;

    @TableField(exist = false)
    private TblOrganization org;

    @TableField(value="JOB_START_TIME")
    @Schema(name="现任职开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date jobStartTime;

    @TableField(value="JOB_END_TIME")
    @Schema(name="现任职结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date jobEndTime;



    @TableField(value="DEPT_TYPE")
    @Schema(name="部门职能分类")
    private String deptType;


    @TableField(value="DEPT_HISTORY")
    @Schema(name="部门历史沿革")
    private String deptHistory;

    @TableField(value="MAIN_DUTY")
    @Schema(name="主责主业")
    private String mainDuty;

    @TableField(value="MAIN_POWER")
    @Schema(name="主要权利")
    private String mainPower;

    @TableField(value="BUSINESS")
    @Schema(name="对外业务")
    private String business;

    @TableField(value="PERSON")
    @Schema(name="人员构成")
    private String person;

    @TableField(value="IS_SEPARATE_ACCOUNT")
    @Schema(name="是否独立核算, 0：否 1：是")
    private Integer isSeparateAccount;

    @TableField(value="ASSET_INFO")
    @Schema(name="资产情况")
    private String assetInfo;

    @TableField(value="MAIN_COST")
    @Schema(name="主要费用")
    private String mainCost;

    @TableField(value="INCOME")
    @Schema(name="收入")
    private BigDecimal income;

    @TableField(value="COST")
    @Schema(name="成本")
    private BigDecimal cost;

    @TableField(value="CONTROLLABLE_COST")
    @Schema(name="可控成本")
    private BigDecimal controllableCost;

    @TableField(value="HAS_EXTERNAL_PERSON")
    @Schema(name="是否有外雇人员, 0：否 1：是")
    private Integer hasExternalPerson;

    @TableField(value="UNCONVENT_JOB")
    @Schema(name="非常规性工作")
    private String unconventJob;

    @TableField(value="IS_AUDIT")
    @Schema(name="是否审计, 0：否 1：是")
    private Integer isAudit;

    @TableField(value="QUARTER_ID")
    @Schema(name="季度ID")
    private String quarterId;

    @TableField(exist = false)
    private QuarterEntity quarter;

    @TableField(exist = false)
    @Schema(name="季度类型 1，三类 2.四类")
    private Integer quarterType;

    @TableField(exist = false)
    @Schema(name="季度")
    private Integer quarterNum;

    @TableField(value="CREATE_USER")
    @Schema(name="创建人")
    private TblStaff createUser;

    @TableField(value="CREATE_TIME")
    @Schema(name="创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @TableField(value = "STATUS")
    @Schema(name="状态")
    private Integer status;
    
    @Schema(name = "单位数量")
    @TableField(value = "UNITCOUNT")
    private Integer unitCount;
    
    @Schema(name = "备注")
    @TableField(value = "REMARKS")
    private String remarks;
    
    @TableField(value="DISFIRSTPERSON")
    @Schema(name="第一次下发人主键，拥有下发编辑权限")
    private BigDecimal disFirstPerson;
    
    @TableField(value="DISSECONDPERSON")
    @Schema(name="第二次下发人主键，拥有编辑权限")
    private BigDecimal disSecondPerson;
    
    
    
    @TableField(value="ZSSTAFFID")
    @Schema(name="主审ID")
    private BigDecimal zsstaffid;
    
    @TableField(value="ZSNAME")
    @Schema(name="主审名称")
    private String zsname;
    
    
    @TableField(value="ZZSTAFFIDS")
    @Schema(name = "组长主键")
    private BigDecimal zzstaffids;
    
    @TableField(value="ZZNAMES")
    @Schema(name = "组长名称")
    private String zznames;
    
    @TableField(value="FZSTAFFIDS")
    @Schema(name = "副组长主键")
    private BigDecimal fzstaffids;
    
    @TableField(value="FZNAMES")
    @Schema(name = "副组长名称")
    private String fznames;
    
    
    
    @TableField(value="FZSTAFFID")
    @Schema(name="助审ID")
    private String fzstaffid;
    
    @TableField(value="FZNAME")
    @Schema(name="助审名称")
    private String fzname;
    
    
    @TableField(value="PROJECTNAME")
    @Schema(name="项目名称")
    private String projectname;
    
    
    @Schema(name = "人员数量")
    @TableField("RSYQ")
    private Integer rsyq;
    
    
    @Schema(name = "现场开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField("XCSRARTTIME")
    private Date xcsrarttime;

    
    @Schema(name = "现场结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField("XCENDTIME")
    private Date xcendtime;


    @Schema(name = "ids")
    @TableField(exist = false)
    private String ids;
    
    @Schema(name = "ids")
    @TableField(exist = false)
    private String orgname;
}
