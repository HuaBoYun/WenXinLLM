package com.huabo.fxgl.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

 
@Data
@TableName("TBL_RISKMONITORING_CREATION")
@Schema(name="风险监督指标-创建表单")
public class TblRiskMonitoringCreationDto {
	
    @Schema(name = "主键")
    private BigDecimal id;
    
    @Schema(name = "年度")
    private BigDecimal riskyear;

    @Schema(name = "季度名称")
    private String quartername;
    
    @Schema(name = "备注")
    private String notes;
 
    @Schema(name = "创建人")
    private BigDecimal createstaffid;
    

    @Schema(name = "创建人名称")
    private String createname;

    @Schema(name = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createtime;

    
    @Schema(name = "审批状态")
    private Integer status;
    
 
    @Schema(name = "所属公司/填报单位id")
    private BigDecimal linkOrgId;
    
    @Schema(name = "所属公司/填报单位名称")
    private String linkOrgName;
    
    
    @Schema(name = "所属部门")
     private BigDecimal linkDeptId;
    
    @Schema(name = "所属部门名称")
     private BigDecimal linkDeptName;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(name="下发时间")
    private Date lssuedDate;
    
    
    @Schema(name="下发人员id")
    private String  lssuedUserId;
    
    @Schema(name = "下发状态 0 未下发  1已下发")
    private Integer lssuedstatus;
}
     

