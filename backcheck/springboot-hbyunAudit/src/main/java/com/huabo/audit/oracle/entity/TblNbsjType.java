package com.huabo.audit.oracle.entity;

import javax.persistence.Id;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@TableName("TBL_NBSJ_TYPE")
@Data
@Schema(name="审计类型实体类")
@Accessors(chain = true)
public class TblNbsjType extends FlexibleFieldEntity {
	@Id
    @TableId(value = "TYPEID")
    @Schema(name = "审计类型ID")
    private BigDecimal typeId;
	
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
	
}
