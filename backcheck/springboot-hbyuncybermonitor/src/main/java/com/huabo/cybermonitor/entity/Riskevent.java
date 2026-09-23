package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author kangjx
 * @since 2022-08-11
 */
@TableName("TBL_RISKEVENT")
public class Riskevent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private BigDecimal riseveid;

    private String riskeventcode;

    private String riskeventname;

    private String riskeventdescription;

    private String indirectloss;

    private String directloss;

    private String riskfactor1;

    private String riskfactor2;

    private String memo;

    private LocalDateTime occureddate;

    private String occureddepartment;

    private String losseventcategory;

    private LocalDateTime discovereddate;

    private BigDecimal inriskeventdb;

    private String indirectlossdes;

    private String directlossdes;

    private String unit;

    private String eventstatus;

    private String recordorg;

    private String recorddepart;

    private String subsystem;

    private String bussiness;

    private BigDecimal maxestimateloss;

    private BigDecimal confirmeddirectloss;

    private BigDecimal confirmeddirectlossa;

    private Integer riskcatid;

    public BigDecimal getRiseveid() {
        return riseveid;
    }

    public void setRiseveid(BigDecimal riseveid) {
        this.riseveid = riseveid;
    }
    public String getRiskeventcode() {
        return riskeventcode;
    }

    public void setRiskeventcode(String riskeventcode) {
        this.riskeventcode = riskeventcode;
    }
    public String getRiskeventname() {
        return riskeventname;
    }

    public void setRiskeventname(String riskeventname) {
        this.riskeventname = riskeventname;
    }
    public String getRiskeventdescription() {
        return riskeventdescription;
    }

    public void setRiskeventdescription(String riskeventdescription) {
        this.riskeventdescription = riskeventdescription;
    }
    public String getIndirectloss() {
        return indirectloss;
    }

    public void setIndirectloss(String indirectloss) {
        this.indirectloss = indirectloss;
    }
    public String getDirectloss() {
        return directloss;
    }

    public void setDirectloss(String directloss) {
        this.directloss = directloss;
    }
    public String getRiskfactor1() {
        return riskfactor1;
    }

    public void setRiskfactor1(String riskfactor1) {
        this.riskfactor1 = riskfactor1;
    }
    public String getRiskfactor2() {
        return riskfactor2;
    }

    public void setRiskfactor2(String riskfactor2) {
        this.riskfactor2 = riskfactor2;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public LocalDateTime getOccureddate() {
        return occureddate;
    }

    public void setOccureddate(LocalDateTime occureddate) {
        this.occureddate = occureddate;
    }
    public String getOccureddepartment() {
        return occureddepartment;
    }

    public void setOccureddepartment(String occureddepartment) {
        this.occureddepartment = occureddepartment;
    }
    public String getLosseventcategory() {
        return losseventcategory;
    }

    public void setLosseventcategory(String losseventcategory) {
        this.losseventcategory = losseventcategory;
    }
    public LocalDateTime getDiscovereddate() {
        return discovereddate;
    }

    public void setDiscovereddate(LocalDateTime discovereddate) {
        this.discovereddate = discovereddate;
    }
    public BigDecimal getInriskeventdb() {
        return inriskeventdb;
    }

    public void setInriskeventdb(BigDecimal inriskeventdb) {
        this.inriskeventdb = inriskeventdb;
    }
    public String getIndirectlossdes() {
        return indirectlossdes;
    }

    public void setIndirectlossdes(String indirectlossdes) {
        this.indirectlossdes = indirectlossdes;
    }
    public String getDirectlossdes() {
        return directlossdes;
    }

    public void setDirectlossdes(String directlossdes) {
        this.directlossdes = directlossdes;
    }
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getEventstatus() {
        return eventstatus;
    }

    public void setEventstatus(String eventstatus) {
        this.eventstatus = eventstatus;
    }
    public String getRecordorg() {
        return recordorg;
    }

    public void setRecordorg(String recordorg) {
        this.recordorg = recordorg;
    }
    public String getRecorddepart() {
        return recorddepart;
    }

    public void setRecorddepart(String recorddepart) {
        this.recorddepart = recorddepart;
    }
    public String getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }
    public String getBussiness() {
        return bussiness;
    }

    public void setBussiness(String bussiness) {
        this.bussiness = bussiness;
    }
    public BigDecimal getMaxestimateloss() {
        return maxestimateloss;
    }

    public void setMaxestimateloss(BigDecimal maxestimateloss) {
        this.maxestimateloss = maxestimateloss;
    }
    public BigDecimal getConfirmeddirectloss() {
        return confirmeddirectloss;
    }

    public void setConfirmeddirectloss(BigDecimal confirmeddirectloss) {
        this.confirmeddirectloss = confirmeddirectloss;
    }
    public BigDecimal getConfirmeddirectlossa() {
        return confirmeddirectlossa;
    }

    public void setConfirmeddirectlossa(BigDecimal confirmeddirectlossa) {
        this.confirmeddirectlossa = confirmeddirectlossa;
    }
    public Integer getRiskcatid() {
        return riskcatid;
    }

    public void setRiskcatid(Integer riskcatid) {
        this.riskcatid = riskcatid;
    }

    @Override
    public String toString() {
        return "Riskevent{" +
            "riseveid=" + riseveid +
            ", riskeventcode=" + riskeventcode +
            ", riskeventname=" + riskeventname +
            ", riskeventdescription=" + riskeventdescription +
            ", indirectloss=" + indirectloss +
            ", directloss=" + directloss +
            ", riskfactor1=" + riskfactor1 +
            ", riskfactor2=" + riskfactor2 +
            ", memo=" + memo +
            ", occureddate=" + occureddate +
            ", occureddepartment=" + occureddepartment +
            ", losseventcategory=" + losseventcategory +
            ", discovereddate=" + discovereddate +
            ", inriskeventdb=" + inriskeventdb +
            ", indirectlossdes=" + indirectlossdes +
            ", directlossdes=" + directlossdes +
            ", unit=" + unit +
            ", eventstatus=" + eventstatus +
            ", recordorg=" + recordorg +
            ", recorddepart=" + recorddepart +
            ", subsystem=" + subsystem +
            ", bussiness=" + bussiness +
            ", maxestimateloss=" + maxestimateloss +
            ", confirmeddirectloss=" + confirmeddirectloss +
            ", confirmeddirectlossa=" + confirmeddirectlossa +
            ", riskcatid=" + riskcatid +
        "}";
    }
}
