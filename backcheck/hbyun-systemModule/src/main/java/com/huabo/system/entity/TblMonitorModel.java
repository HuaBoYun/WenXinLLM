package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_MONITOR_MODEL")
@Schema(name="TblMonitorModel对象", description="")
public class TblMonitorModel implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value="MODELID",type = IdType.INPUT)
      private BigDecimal modelid;

    @TableField("MODELNAME")
    private String modelname;

    @TableField("MODELDES")
    private String modeldes;

    @TableField("MODELCATEGORY")
    private String modelcategory;

    @TableField("MODELSTATUS")
    private String modelstatus;

    @TableField("ORGID")
    private String orgid;

    @TableField("STAFFID")
    private String staffid;

    @TableField("CREATEDATE")
    private Date createdate;

    @TableField("MODELREMINDER")
    private String modelreminder;

    @TableField("CONNECTIONSTRINGS")
    private String connectionstrings;

    @TableField("MODELSTEP1")
    private String modelstep1;

    @TableField("MODELSTEP2")
    private String modelstep2;

    @TableField("MODELSTEP3")
    private String modelstep3;

    @TableField("MODELSTEP4")
    private String modelstep4;

    @TableField("MODELSTEP5")
    private String modelstep5;

    @TableField("EXEINTERVAL")
    private String exeinterval;

    @TableField("MODELCODE")
    private String modelcode;

    @TableField("INMODELDB")
    private String inmodeldb;

    @TableField("RUNSTATUS")
    private Integer runstatus;

    @TableField("SUBSYSTEM")
    private Integer subsystem;

    @TableField("MEMO")
    private String memo;

    @TableField("BUSSINESSCAT")
    private String bussinesscat;



    @Transient
    private BigDecimal solutionid;
    @Transient
    private String solutioncode;
    @Transient
    private String solutionname;
    @Transient
    private String solutionstatus;
    @Transient
    private BigDecimal type;
    @Transient
    private String exefrequncy;


}
