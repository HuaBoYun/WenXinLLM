package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 审计模板类型
 * @author Lenovo
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_VIDEO_TYPE")
@Schema(name="TblVideoType对象", description="")
public class TblVideoType implements Serializable{

    private static final long serialVersionUID = -15604782677274749L;

    @TableId(value="TYPEID",type = IdType.INPUT)
    @Schema(name="主键")
    private BigDecimal typeId; 
    @TableField("TYPENAME")
    @Schema(name="课程类型说明")
    private String typename; 
    @TableField("VERSION")
    @Schema(name="版本")
    private Integer version; 
    @TableField("ORGID")
    @Schema(name="隶属组织ID")
    private BigDecimal orgid;//组织ID
    @TableField("TYPE")
    @Schema(name="类型")
    private String type;

    @Transient
    private TblVideoType videoType;
}