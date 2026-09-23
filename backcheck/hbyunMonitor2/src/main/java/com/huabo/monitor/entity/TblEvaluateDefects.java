package com.huabo.monitor.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author CJ
 * @since 2024-12-16
 */
@Data
@TableName("TBL_EVALUATEDEFECTS")
@Schema(name="TblEvaluateDefects对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblEvaluateDefects implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(type= IdType.INPUT)
    @Schema(name="评价缺陷ID")
	@TableField("ID")
    private BigDecimal id;

    @Schema(name="缺陷编号")
    @TableField("DEFECTSCODE")
    private String defectscode;

    @Schema(name="缺陷名称")
    @TableField("DEFECTSNAME")
    private String defectsname;
    
    
    @Schema(name="缺陷描述及依据")
    @TableField("DESCRIPTION")
    private String description;
    
    @Schema(name="发生时间")
    @TableField("OCCURRENCEDATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date occurrencedate;
    
    @Schema(name="涉及金额(万元)")
    @TableField("AMOUNT")
    private BigDecimal amount ;
    
    @Schema(name="原因分析")
    @TableField("CAUSEANALYSIS")
    private String causeanalysis;
    
    
    @Schema(name="缺陷类别")
    @TableField("DEFECTCATEGORY")
    private BigDecimal defectcategory ;
    
    @Schema(name="缺陷等级")
    @TableField("DEFECTLEVEL")
    private BigDecimal defectlevel ;
    
    @Schema(name="缺陷种类")
    @TableField("DEFECTTYPE")
    private BigDecimal defecttype ;
	
    @Schema(name="是否涉诉")
    @TableField("LITIGATION")
    private BigDecimal litigation ;
    
    @Schema(name="是否境外")
    @TableField("OVERSEAS")
    private BigDecimal overseas ;

    @Schema(name="创建人")
    @TableField("CREATESTAFFID")
    private BigDecimal createstaffid;
    
    @TableField("CREATETIME")
 	@Schema(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
   	@DateTimeFormat(pattern = "yyyy-MM-dd")
     private Date createtime;

    @Schema(name="关联公司")
    @TableField("UNIT")
    private BigDecimal unit;
    
    @Schema(name="审批状态")
    @TableField("STATUS")
    private Integer status;
 
    @Schema(name="创建人姓名")
    @TableField(exist=false)
    private String  createstaffname;
    
    
}
