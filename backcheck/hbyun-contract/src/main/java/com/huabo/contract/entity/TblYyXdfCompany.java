package com.huabo.contract.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YY_XDF_COMPANY")
@Schema(name="TblYyXdfCompany对象")
public class TblYyXdfCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "COMPANYID",type = IdType.INPUT)
    private BigDecimal companyid;

    @TableField("COMPANYNAME")
    private String companyname;

    @TableField("TEAMID")
    private BigDecimal teamid;

    @TableField("CREATEDATE")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdate;

    @TableField("STAFFID")
    private BigDecimal staffid;

    @TableField("ORGID")
    private BigDecimal orgid;

    @TableField("REPORTID")
    private BigDecimal reportid;

    @TableField("FXTYPE")
    private String fxtype;

    @TableField("CSTATUS")
    private BigDecimal cstatus;

    @Schema(name = "创建用户")
    @TableField(exist=false,select=false,fill=FieldFill.DEFAULT)
    private TblStaff staff;

    @TableField(exist=false,select=false,fill=FieldFill.DEFAULT)
    private TblYyReportModel reportmodel;//形成报告id

    @TableField(exist=false,select=false,fill=FieldFill.DEFAULT)
    private List<TblYyPrice> list;
    
    @TableField(exist=false,select=false,fill=FieldFill.DEFAULT)
    private Set<TblBiPage> pages = new HashSet();


}
