package com.huabo.compliance.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 审计模板类型
 *
 * @author Lenovo
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_VIDEO_TYPE")
@Schema(name="TblVideoType对象")
public class TblVideoTypeMySql implements Serializable {

    private static final long serialVersionUID = -15604782677274749L;

    @TableId("TYPEID")
    @Column(name = "TYPEID")
    @Id
    private BigDecimal typeId;//审计类型ID
    @TableField("TYPENAME")
    @Column(name = "TYPENAME")
    private String typename;//审计类型说明
    @TableField("VERSION")
    @Column(name = "VERSION")
    private Integer version;//审计类型版本
    @TableField("ORGID")
    @Column(name = "ORGID")
    private Integer orgid;//组织ID
    @TableField("TYPE")
    @Column(name = "TYPE")
    private String type;

    @Transient
    private TblVideoTypeMySql videoType;


}