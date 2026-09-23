package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_NBSJ_AUDITEXPERIENCE_TYPE")
@Schema(name="TblNbsjAuditExperienceTypeEntity对象")
public class TblNbsjAuditExperienceTypeEntity {
//	private static final long serialVersionUID = 1L;
	
	@TableId(value = "TYPEID", type= IdType.INPUT)
	@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @Schema(name = "分类目录主键id")
    private BigDecimal typeId;
	
	@TableField( "TYPENAME")
    @Schema(name = "分类名称")
    private String typeName;
	
	@TableField(value = "PARENTID")
    @Schema(name = "父级id")
    private BigDecimal parentId;
	
	@TableField(value = "CREATETIME")
    @Schema(name="创建时间",hidden=true)
    private Date createTime;
	
	@TableField(value = "UPDATETIME")
    @Schema(name="修改时间",hidden=true)
    private Date updateTime;
	
	@TableField(value = "CREATESTAFFID")
    @Schema(name="创建人",hidden=true)
    private BigDecimal createStaffid;
	
	
	@TableField(value = "MPDELTYPE")
	@Schema(name = "模块类型：ZNSJ  FXCT")
    private String mpdeltype;
	
}
