package com.huabo.compliance.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "TBL_RISK")
@Schema(name="TblRisk")
public class TblRisk {

    @TableId("RISKID")
    @Id
    //@KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
    private BigDecimal riskid;
    @TableField("RISKNAME")
    private String riskname;//
    @TableField("RISKNUMBER")
    private String risknumber;//
    @TableField("RISKDES")
    private String riskdes;
    @TableField("BELONGSTO")
    private String belongsto;
    @TableField("MEMO")
    private String memo;
    @TableField("INRISKDB")
    private Integer inriskdb;
    @TableField("VERSION")
    private String version;
    @TableField("UNIT")
    private String unit;
    @TableField("REORG")
    private String reorg;
    @TableField("SUBSYS")
    private String subsys;
    @TableField("RISKCREATEDT")
    private Date riskcreatedt;
    @TableField("STAFFID")
    private String staffid;
    @TableField("RISKLERABILITYNUMBER")
    private String risklerabilitynumber;
    @TableField("RISKLERABILITYNAME")
    private String risklerabilityname;
    @TableField("RISKLERABILITYDES")
    private String risklerabilitydes;
    @TableField("RISKPROGRAM")
    private String riskprogram;
    @Transient
    private Set tblControlmatrixes = new HashSet(0);
    @Transient
    private Set tblRiskevents = new HashSet(0);
    @Transient
    private Set tblRiskevents_1 = new HashSet(0);
    @Transient
    private Set tblFlows = new HashSet(0);
    @TableField("YDUSERID")
    private Integer yduserid;
    @Transient
    private String zrbmName;
    @Transient
    private String xgbmName;
    @Transient
    private String ssjgName;
    @Transient
    private String ssfxName;
    @Transient
    private String ydusername;
    @Transient
    private String level;
    @Transient
    private String copingPlot;
    private TblRiskcategory tblRiskcategory;

    public void setTblRiskcategory(TblRiskcategory tblRiskcategory) {
        this.tblRiskcategory = tblRiskcategory;
    }
}
