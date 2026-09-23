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
@TableName("TBL_RISK_MONMODEL")
@Schema(name="风险监督指标字典-关联部门信息表")
public class TblRiskMonModel  implements Serializable {
	private static final long serialVersionUID = 1L;
	
    @Schema(name = "主键")
    @TableId(value = "ID")
    @Id
    private BigDecimal id;
    
    @Schema(name = "关联字典表id")
    @TableField(value = "DICTONARYID")
    private BigDecimal dictonaryId;

    @Schema(name = "部门id")
    @TableField(value = "DEPTID")
    private BigDecimal deptId;
    
    @Schema(name = "部门名称")
    @TableField(exist=false)
    private String deptName;
    
    @Schema(name = "排序标记")
    @TableField(exist=false)
    private String sort;
    
    @Schema(name = "关联公司")
    @TableField(value = "LINKORGID")
    private BigDecimal linkOrgID;
    
    @Schema(name = "字典版本表")
    @TableField(value = "VERSIONID")
    private BigDecimal versionId;
    
    
    @Schema(name = "创建时间")
    @TableField(value = "CREATETIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createtime;
}
     

