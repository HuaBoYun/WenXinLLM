package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

 
@Data
@TableName("TBL_MAJORRISKCREATE")
@Schema(name="重大风险创建表单")
public class TblMajorRiskCreate extends FlexibleFieldEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
    @Schema(name = "主键")
    @TableId(value = "ID")
    @Id
    private BigDecimal id;
    
    @Schema(name = "年度")
    @TableField(value = "RISKYEAR")
    private BigDecimal riskyear;

    @Schema(name = "季度名称")
    @TableField(value = "QUARTERNAME")
    private String quartername;
 
    @Schema(name = "创建人")
    @TableField(value = "CREATESTAFFID")
    private BigDecimal createstaffid;
    

    @Schema(name = "创建人名称")
    @TableField(value = "CREATENAME")
    private String createname;

    @Schema(name = "创建时间")
    @TableField(value = "CREATETIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createtime;

    
    @Schema(name = "状态")
    @TableField(value = "STATUS")
    private Integer status;
    
    //密级及查询条件
    @Schema(name = "密级主键")
    @TableField("SECRECTLEVELID")
    private BigDecimal secrectlevelid;
   
    @Schema(name = "知悉范围id")
    @TableField("STAFFSCOPEIDS")
    private String staffscopeids;
    
    @Schema(name = "知悉范围名称")
    @TableField("STAFFSCOPENAMES")
    private String staffscopenames;
 
    @Schema(name = "所属公司/填报单位id")
    @TableField("LINKORGID")
    @Column(name = "LINKORGID")
    private BigDecimal linkOrgId;
    
    @Schema(name = "所属公司/填报单位名称")
    @TableField("LINKORGNAME")
    @Column(name = "LINKORGNAME")
    private String linkOrgName;
    
    
    @Schema(name = "所属部门")
    @TableField("LINKDEPTID")
    @Column(name = "LINKDEPTID")
  private BigDecimal linkDeptId;
    
    @Schema(name = "下发状态 0 未下发  1已下发")
    @TableField(value = "LSSUEDSTATUS")
    private Integer lssuedstatus;
    
    @TableField(value = "IMPLSSUEDDATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(name="下发时间")
    private Date implssuedDate;
//    @Schema(name = "关联内容")
//    @TableField(exist = false)
//    @Transient
//    private List<LeaveAudit3LEntity>  list;

}

