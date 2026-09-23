package com.huabo.contract.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-04-16
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_RISKEVENT")
@Schema(name="TblRiskevent对象")
public class TblRiskevent implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "RISEVEID",type = IdType.INPUT)
      private BigDecimal riseveid;

    @TableField("RISKEVENTCODE")
    private String riskeventcode;

    @TableField("RISKEVENTNAME")
    private String riskeventname;

    @TableField("RISKEVENTDESCRIPTION")
    private String riskeventdescription;

    @TableField("INDIRECTLOSS")
    private String indirectloss;

    @TableField("DIRECTLOSS")
    private String directloss;

    @TableField("RISKFACTOR1")
    private String riskfactor1;

    @TableField("RISKFACTOR2")
    private String riskfactor2;

    @TableField("MEMO")
    private String memo;

    @TableField("OCCUREDDATE")
    private LocalDateTime occureddate;

    @TableField("OCCUREDDEPARTMENT")
    private String occureddepartment;

    @TableField("LOSSEVENTCATEGORY")
    private String losseventcategory;

    @TableField("DISCOVEREDDATE")
    private LocalDateTime discovereddate;

    @TableField("INRISKEVENTDB")
    private BigDecimal inriskeventdb;

    @TableField("INDIRECTLOSSDES")
    private String indirectlossdes;

    @TableField("DIRECTLOSSDES")
    private String directlossdes;

    @TableField("UNIT")
    private String unit;

    @TableField("EVENTSTATUS")
    private String eventstatus;

    @TableField("RECORDORG")
    private String recordorg;

    @TableField("RECORDDEPART")
    private String recorddepart;

    @TableField("SUBSYSTEM")
    private String subsystem;

    @TableField("BUSSINESS")
    private String bussiness;

    @TableField("MAXESTIMATELOSS")
    private BigDecimal maxestimateloss;

    @TableField("CONFIRMEDDIRECTLOSS")
    private BigDecimal confirmeddirectloss;

    @TableField("CONFIRMEDDIRECTLOSSA")
    private BigDecimal confirmeddirectlossa;

    @TableField("RISKCATID")
    private BigDecimal riskcatid;

    @TableField("RISKID")
    private BigDecimal riskid;

    @TableField("RECSTATUS")
    private String recstatus;

    @TableField("CREATEUSER")
    private BigDecimal createuser;


}
