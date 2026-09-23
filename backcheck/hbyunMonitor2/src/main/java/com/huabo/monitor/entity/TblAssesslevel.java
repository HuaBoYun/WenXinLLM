package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@Data
@TableName("TBL_ASSESSLEVEL")
@Schema(name="TblAssesslevel对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblAssesslevel extends FlexibleFieldEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableId(type= IdType.INPUT)
    @Schema(name="Id")
    private BigDecimal asslevid;

    @Schema(name="级别名称")
    @TableField("LEVELNAME")
    private String levelname;

    @Schema(name="上限区域")
    @TableField("UPPERREGIONDES")
    private String upperregiondes;

    @Schema(name="级别上限")
    @TableField("LEVELUPPER")
    private BigDecimal levelupper;

    @Schema(name="级别下限")
    @TableField("LEVELLOWER")
    private BigDecimal levellower;

    @Schema(name="下限区域")
    @TableField("LOWERREGIONDES")
    private String lowerregiondes;

    @Schema(name="级别说明")
    @TableField("LEVELDES")
    private String leveldes;
   
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Schema(name="日期")
    @JsonFormat(pattern="yyyy-MM-dd")
    @TableField("MODIFIEDDATE")
    private Date modifieddate;

    @Schema(name="备注")
    @TableField("MEMO")
    private String memo;

    @Schema(name="隶属组织")
    @TableField("TBLCOMANY")
    private String tblcomany;
    
    
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
    @Column(name = "LINKDEPTID")
  private BigDecimal linkdeptid;
    
    @Schema(name="创建人")
    @TableField("CREATESTAFFID")
    private BigDecimal createstaffid;
    
    @TableField("CREATETIME")
 	@Column(name = "CREATETIME")
 	@Schema(name = "创建时间")
     private Date createtime;
 
    @Override
    public String toString() {
        return "TblAssesslevel{" +
            "asslevid=" + asslevid +
            ", levelname=" + levelname +
            ", upperregiondes=" + upperregiondes +
            ", levelupper=" + levelupper +
            ", levellower=" + levellower +
            ", lowerregiondes=" + lowerregiondes +
            ", leveldes=" + leveldes +
            ", modifieddate=" + modifieddate +
            ", memo=" + memo +
            ", tblcomany=" + tblcomany +
        "}";
    }
}
