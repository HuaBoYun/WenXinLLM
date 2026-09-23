package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.ibatis.annotations.Result;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_OPERATE")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjOperateEntity {
	
	public static final Integer FINISH = 1;
	public static final Integer UNFINISH = 0;
	private static final long serialVersionUID = 1L;
	
	@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	@TableId(value = "OPERATEID", type= IdType.INPUT)
    @Schema
    private BigDecimal operateid;

    @TableField(value = "finish")
    @Schema
    private Integer finish;
    
    @TableField(value = "finishTime")
    @Schema
    private Date finishtime;
    
    @TableField(value = "AUTHID")
    @Schema
    private BigDecimal authId;
    
    @TableField(value = "SHETTID")
    @Schema
    private BigDecimal sheetId;

    @TableField(exist = false)
    @Transient
    private TblNbsjAuthorizationEntity authorization;

    @TableField(exist = false)
    @Transient
    private TblNbsjSheetEntity nbsjSheet;
    
    @Schema(hidden=true)
    @Transient
    @TableField(exist = false)
    private String businessType;

    @TableField(exist = false)
    @Schema(hidden=true)
    @Transient
    private String riskAttrbution;
    
    @TableField(value = "suditProcess")
    @Schema(hidden=true)
    private String suditProcess;
   //@IgnoreSwaggerParameter

    @TableField(exist = false)
    @Schema(hidden=true)
    @Transient
    private String programid;

    @TableField(exist = false)
    @Schema(hidden=true)
    @Transient
    private String renyuan;

    @TableField(exist = false)
    @Transient
    private String control;

    @TableField(exist = false)
    @Transient
    private String riskPoint;

    @TableField(exist = false)
    @Transient
    private String riskSource;
    
    
}
