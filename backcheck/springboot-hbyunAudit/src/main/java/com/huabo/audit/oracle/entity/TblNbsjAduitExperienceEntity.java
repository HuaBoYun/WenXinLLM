package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_AUDIT_EXPERIENCE")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjAduitExperienceEntity {
	@TableId(value = "programid", type= IdType.INPUT)
	//@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @Schema
    private BigDecimal programId;

    @TableField(exist = false)
    @Schema
    private TblExperienceTypeEntity experienceType;
    
    @TableField(value = "businesstype")
    @Schema
    private String businessType;
    
    @TableField(value = "status")
    @Schema
    private Integer status;
    
    @TableField(value = "suditprocess")
    @Schema
    private String suditProcess;
    
    @TableField(value = "risksource")
    @Schema
    private String riskSource;
    
    @TableField(value = "riskpoint")
    @Schema
    private String riskPoint;
    
    @TableField(value = "control")
    @Schema
    private String control;

    @TableField(exist = false)
    @Schema
    private TblStaff staff;
    
//    @TableField(value = "control")
//    @Schema
//    private Set<TblNbsjAuthorization> authorizations;
    
    @TableField(value = "control")
    @Schema
    private String bioData;
    
    @TableField(value = "control")
    @Schema
    private Date updateTime;
    
    @TableField(value = "control")
    @Schema
    private Date createTime;

    @TableField(exist = false)
    @Schema
    private TblNbsjExperienceEntity nbsjExperience;
    
}
