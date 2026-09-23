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
@TableName("TBL_RISK_MONDICTONARY")
@Schema(name="风险监督指标字典表")
public class TblRiskMonDictonary   implements Serializable {
	private static final long serialVersionUID = 1L;
	
    @Schema(name = "主键")
    @TableId(value = "ID")
    @Id
    private BigDecimal id;
    
    @Schema(name = "TBL_RISK_MONDICTONARY")
    @TableField(value = "CODE")
    private String code;

    @Schema(name = "字段名称")
    @TableField(value = "CODENAME")
    private String codeName;
    
    @Schema(name = "备注")
    @TableField(value = "NOTES")
    private String notes;
 
    @Schema(name = "创建人")
    @TableField(value = "CREATESTAFFID")
    private BigDecimal createstaffid;
    
    @Schema(name = "排序")
    @TableField(value = "SORT")
    private int sort;
    
    @Schema(name = "0 未启动 1 启动  2 废弃")
    @TableField("STATUS")
    private BigDecimal status;
    
    
    @Schema(name = "公司ID")
    @TableField("ORGID")
    private BigDecimal orgid;
    
    @Schema(name = "类型1")
    @TableField("TYPE1")
    private String type1;
    
    @Schema(name = "类型2")
    @TableField("TYPE2")
    private String type2;
    
    @Schema(name = "类型3")
    @TableField("TYPE3")
    private String type3;
    
    @Schema(name = "类型4")
    @TableField("TYPE4")
    private String type4;
    
    
    @Schema(name = "联合配置表给部门信息")
    @TableField(exist=false)
    private String deptId;
    
    @Schema(name = "联合配置表给部门信息")
    @TableField(exist=false)
    private String deptName;
}
     

