package com.huabo.compliance.mysql.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hbfk.entity.TblAttachment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "TBL_RISKEVENT")
@Schema(name="TblRiskeventMySql")
public class TblRiskeventMySql {

    @TableId("RISEVEID")
    //@KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order = ORDER.DEFAULT)
    private BigDecimal riseveid;
    @Transient
    private TblRiskcategoryMySql tblRiskcategory;
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
    private Date occureddate;
    @TableField("OCCUREDDEPARTMENT")
    private String occureddepartment;
    @TableField("LOSSEVENTCATEGORY")
    private String losseventcategory;
    @TableField("DISCOVEREDDATE")
    private Date discovereddate;
    @TableField("INRISKEVENTDB")
    private Integer inriskeventdb;
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
    @Transient
    private Set tblInnerrules = new HashSet(0);
    @Transient
    private Set tblRisks = new HashSet(0);
    // private Set tblFlows = new HashSet(0);
    @Transient
    private Set tblRisks_1 = new HashSet(0);
    @Transient
    private Set tblOuterrules = new HashSet(0);
    @Transient
    private Set tblControlmatrixes = new HashSet(0);
    //private Set tblFlows_1 = new HashSet(0);
    @Transient
    private Set tblFlowsRisk = new HashSet(0);
    //private Set<TblIndicator> tblIndicators = new HashSet();
    @Transient
    private Set<TblAttachment> tblAttachments = new HashSet();
    @Transient
    private String price;
    @Transient
    private String startDate;
}
