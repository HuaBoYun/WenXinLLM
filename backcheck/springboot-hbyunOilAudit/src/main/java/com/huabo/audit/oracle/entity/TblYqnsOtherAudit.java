package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 其他审计表
 */
@TableName(value ="TBL_YQNS_OTHERAUDIT")
@Data
public class TblYqnsOtherAudit implements Serializable {

	private static final long serialVersionUID = 1L;

    @Schema(name = "主键")
    @TableId(value = "AUDITID",type = IdType.INPUT)
    private BigDecimal auditId;

    
    @Schema(name = "审计项目名称")
    @TableField(value = "AUDITITEMNAME")
    private String auditItemName;
    
    @Schema(name = "被审计单位，多个用逗号分隔")
    @TableField(value = "AUDITORGIDSTRS")
    private String auditOrgidStrs;
    
    @Schema(name = "被审计单位名称")
    @TableField(exist = false)
    private String auditOrgNameStrs;
    
    @Schema(name = "实施类型")
    @TableField(value = "IMPLTYPE")
    private String implType;

    @Schema(name = "实施审计机构")
    @TableField(value = "IMPLORGID")
    private BigDecimal implOrgId;
    
    @Schema(name = "实施审计机构名称")
    @TableField(exist = false)
    private String implOrgName;
    

    @Schema(name = "项目负责处（科）室")
    @TableField(value = "ITEMDEPTID")
    private BigDecimal itemDeptId;
    
    @Schema(name = "项目负责处（科）室名称")
    @TableField(exist = false)
    private String itemDeptName;
    
    @Schema(name = "审计项目类型")
    @TableField(value = "AUDITITEMTYPE")
    private String auditItemType;
    
    @Schema(name = "立项依据")
    @TableField(value = "PROJECTBASIS")
    private String projectBasis;
    
    @Schema(name = "计划投入人日")
    @TableField(value = "PLANPERSONDAYS")
    private Integer planPersonDays;
    
    
    @Schema(name = "计划实施月份")
    @TableField(value = "PLANIMPLMONTH")
    private Integer planImplMonth;
    
    @Schema(name = "创建人、编制人")
    @TableField(value = "CREATESTAFFID")
    private BigDecimal createStaffId;
    
    @Schema(name = "创建人名称")
    @TableField(exist = false)
    private String createStaffName;
    
    @Schema(name = "创建时间")
    @TableField(value = "CREATETIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
    
    @Schema(name = "立项单位")
    @TableField(value = "PROJECTINITUNIT")
    private BigDecimal projectInitUnit;
    
    @Schema(name = "立项单位名称")
    @TableField(exist = false)
    private String projectInitUnitName;
    
    @Schema(name = "项目年度")
    @TableField(value = "PROJECTYEAR")
    private Integer projectYear;
    
    @Schema(name = "是否对被审计单位的全部经营活动进行审计，0-是 ，1否")
    @TableField(value = "ISORNOTALL")
    private Integer isOrNotAll;
    
    @Schema(name = "项目类型，1-新增计划，2-境外项目")
    @TableField(value = "PLANTYPE")
    private Integer planType;
    
    @Schema(name = "外包花费")
    @TableField(value = "OUTCOST")
    private BigDecimal outCost;
    
    @Schema(name = "审批状态 0-未整改 1-审批中 ，2-已退回 3-已撤销 6-已完成")
    @TableField(value = "STATUS")
    private Integer status;
    
    
    @Schema(name = "修改人")
    @TableField(value = "MODIFYSTAFFID")
    private BigDecimal modifyStaffid;
    
    @Schema(name = "修改人名称")
    @TableField(exist = false)
    private String modifyStaffName;
    
    @Schema(name = "计划草稿主键")
    @TableField(value = "DRAFTPLANID")
    private BigDecimal draftPlanId;
    
    @Schema(name = "是否关联的计划初稿，1-是  ，其余否")
    @TableField(value = "FIRSTDRAFTPLANID")
    private BigDecimal firstDraftPlanId;
    
    @Schema(name = "是否关联的计划初稿，1-是  ，其余否")
    @TableField(value = "SECONDRAFTPLANID")
    private BigDecimal secondDraftPlanId;
    
    @Schema(name = "审计范围")
    @TableField(value = "AUDITSCOPE")
    private String auditScope;
    
    @Schema(name = "修改时间")
    @TableField(value = "MODIFYDATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date modifyDate;
    
    @Schema(name = "其他审计单据所属部门")
    @TableField(value = "LINKDEPT")
    private BigDecimal linkDept;
    

    @Schema(name = "附件主键集合")
    @TableField(exist = false)
    private List<String> attIds;

    @Schema(name = "附件集合")
    @TableField(exist = false)
    @IgnoreSwaggerParameter
    private List<TblAttachment> attachments;
}