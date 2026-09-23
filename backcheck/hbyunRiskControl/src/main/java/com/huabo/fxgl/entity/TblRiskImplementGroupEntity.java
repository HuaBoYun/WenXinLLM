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
import java.util.Date;

import javax.persistence.Column;

@Data
@TableName("TBL_RISK_IMPLEMENTGROUP")
@Schema(name="重大风险创建风险信息表")
public class TblRiskImplementGroupEntity extends FlexibleFieldEntity  implements Serializable {

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
    
    
    @TableField(value = "IMP_LSSUED_STAFFID")
    @Schema(name="下发人员id")
    private String impLssuedStaffid;
    
    @TableField(value = "LSSUED_STAFFNAME")
    @Schema(name="下发人员名字")
    private String lssuedStaffName;
    
    
    @TableField(value = "IMP_LSSUED_UNIT")
    @Schema(name="下发单位id--对应下发人员id")
    private String impLssuedUnit;
    
    @TableField(value = "IMP_LSSUED_UNITNAME")
    @Schema(name="下发单位名称--对应下发人员")
    private String impLssuedUnitName;


    
    
    @Schema(name="是否下发 1 下发 0未下发")
    @TableField(value = "TOISSUED")
    private BigDecimal toIssued; 
   
  
    @TableField(value = "MAJORID")
    @Schema(name="关联季度ID")
    private BigDecimal majorid;
    
    @TableField(value = "IMP_LSSUED_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(name="下发时间")
    private Date impLssuedDate;
    
    @Schema(name = "创建人")
    @TableField(value = "CREATESTAFFID")
    private BigDecimal createstaffid;
    
    @Schema(name = "创建时间")
    @TableField(value = "CREATETIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createtime;
    
    
    @Schema(name="重点事项")
	@TableField("PRIORITIES")
    private String priorities;   			
}
