package com.huabo.fxgl.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

@Data
@TableName("TBL_RISK_IMPLEMENT")
@Schema(name="重大风险填报表单")
public class TblRiskImplementEntity extends FlexibleFieldEntity  implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "ID")
    private BigDecimal id;

    @TableField(value = "IMP_RISK_NAME")
    @Schema(name="风险名称")
    private String impRiskName;
    
    @TableField(value = "IMP_RISK_DETAILS")
    @Schema(name="风险描述")
    private String impRiskDetails;
    
    @TableField(value = "IMP_WAY_STAFF")
    @Schema(name="牵头领导")
    private String impWayStaff;

    @TableField(value = "IMP_WAY_STAFFNAME")
    @Schema(name="牵头领导名称")
    private String impWayStaffName;
    @TableField(value = "IMP_WAY_DEPT")
    @Schema(name="牵头责任部门")
    private String impWayDept;

    @TableField(value = "IMP_WAY_DEPTNAME")
    @Schema(name="牵头责任部门名称")
    private String impWayDeptName;

    @TableField(value = "IMP_DUTY_UNIT")
    @Schema(name="公司相关责任单位")
    private String impDutyUnit;

    @TableField(value = "IMP_DUTY_UNITNAME")
    @Schema(name="公司相关责任单位名称")
    private String impDutyUnitName;
    @TableField(value = "IMP_KEY_ISSUES")
    @Schema(name="重点事项")
    private String impKeyIssues;

    @TableField(value = "IMP_THIS_CONTROL")
    @Schema(name="本季度风险防控情况")
    private String impThisControl;
    @TableField(value = "IMP_SOLUTIONS")
    @Schema(name="已发生的风险事件及应对处置情况")
    private String impSolutions;
    @TableField(value = "IMP_TIPS")
    @Schema(name="需要提示的问题和风险")
    private String impTips;
    @TableField(value = "IMP_TEXT_CONTROL")
    @Schema(name="下一季度主要风险研判及相应防控措施")
    private String impTextControl;
    @TableField(value = "IMP_OTHER")
    @Schema(name="其他需要说明的情况")
    private String impOther;
    @TableField(value = "IMP_LSSUED_STAFFID")
    @Schema(name="创建人")
    private BigDecimal impLssuedStaffid;

    @TableField(exist = false)
    @Schema(name="创建人名称")
    private String  impLssuedStaffidName;
    
    @TableField(value = "IMP_LSSUED_UNIT")
    @Schema(name="创建单位")
    private BigDecimal impLssuedUnit;

    @TableField(exist = false)
    @Schema(name="创建单位名称")
    private String impLssuedUnitName;
    
    @Schema(name="是否上报 1上报 0未上报")
    private BigDecimal toreport;
    
    @Schema(name = "上报提交日期")
    @TableField("TOREPORTDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date toreportdate;

   
    //需求调整之后此处没有下发操作，以下字段待用
  
    @TableField(value = "REPORTING_ID")
    @Schema(name="填报ID")
    private BigDecimal reportingId;
    
    @TableField(value = "IMP_LSSUED_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(name="下发时间")
    private Date impLssuedDate;
    
    @TableField(value = "STATUS")
    @Schema(name="审批状态")
    private Integer status;
    
    
    @TableField(value = "MAJORID")
    @Schema(name="重大风险创建季度ID")
    private BigDecimal majorId;
    
    @TableField(value = "MAJORRISKID")
    @Schema(name="重大风险创建风险信息ID")
    private BigDecimal majorRiskId;
    
    @TableField(exist=false)
    @Schema(name="风险名称分组个数")
    private BigDecimal groupcount;
    
    @TableField(exist=false)
    @Schema(name="上报年度")
    private String nd;
    
    @TableField(exist=false)
    @Schema(name="上报季度")
    private String jd;
    
    @Schema(name = "相同的名称的数据")
    @Transient
    @TableField(exist=false)
    private List<TblRiskImplementEntity> relaList = new ArrayList<TblRiskImplementEntity>(0);
    
    @Schema(name = "相同的名称的数量")
    @Transient
    @TableField(exist=false)
    private Integer relaCount;
    
    
    @TableField(value = "TRANSFERSTAFFID")
    @Schema(name="转派人员")
    private BigDecimal transferStaffid;
    
    @TableField(exist=false)
    @Schema(name="转派人员姓名")
    private String transferStaffName;
}
