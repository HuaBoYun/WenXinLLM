package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_TYPEOF")
@Data
@Schema(name="审计类型维护实体类")
@Accessors(chain = true)
@KeySequence(value = "HIBERNATE_SEQUENCE")
public class TblNbsjTypeOf extends BaseReservedProperty implements Serializable {
    private static final long serialVersionUID = 1L;

	
	 @TableId(value="TYPEID", type= IdType.INPUT)
	@Schema(name="TYPEID")
    private BigDecimal typeId;
	
	 @Schema(name = "编号")
	    @TableField(value = "NO")
	    private String no;
	 
	@TableField(value = "auditType")
	@Schema(name = "审计类型说明")
	private String auditType;
	
	@TableField(value = "status")
	@Schema(name = "审计类型状态      1禁用     2正常")
	private Integer status;
	
	@TableField(value = "version")
	@Schema(name = "审计类型版本")
	private String version;
	
	@TableField(value = "orgid")
	@Schema(name = "所属组织ID")
	private String orgid;
	
	@TableField(value = "AUDITCODE")
	@Schema(name = "审计编号")
	private Integer auditCode;
	
	@TableField("CREATESTAFF")
    private BigDecimal createstaff;

    @TableField("CREATETIME")
    @Schema(name="创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createtime;
    
    @TableField("PARENTID")
    private BigDecimal parentid;
    
    @Schema(name = "YQNS-央企内审")
    @TableField("TYPE")
   private String type;
	
    @Transient
	@TableField(exist = false)
    private List<TblNbsjTypeOf> childrenList = new ArrayList(0);

}
