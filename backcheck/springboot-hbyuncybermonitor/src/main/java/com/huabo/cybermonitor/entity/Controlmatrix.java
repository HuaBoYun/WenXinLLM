package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author kangjx
 * @since 2022-08-11
 */
@TableName("TBL_CONTROLMATRIX")
public class Controlmatrix implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private BigDecimal conmatid;

    private String flowname;

    private String controltarget;

    private String controlnumber;

    private String controlname;

    private String controlmanager;

    private String controlfrequency;

    private String controltype;

    private String controlmethod;

    private String memo;

    private String controldes;

    private String insidecontroltarget;

    private String keycontrol;

    private String effective;

    private String controltest;

    private String financialreportidentify;

    private String relateddepart;

    private String controldocument;

    private String flowcode;

    private String toplevelflowcat;

    private String version;

    private String createdtime;

    private String lastmodifiedtime;

    private String subsystem;

    private String conkzcs;

    private BigDecimal versiontype;

    public BigDecimal getConmatid() {
        return conmatid;
    }

    public void setConmatid(BigDecimal conmatid) {
        this.conmatid = conmatid;
    }
    public String getFlowname() {
        return flowname;
    }

    public void setFlowname(String flowname) {
        this.flowname = flowname;
    }
    public String getControltarget() {
        return controltarget;
    }

    public void setControltarget(String controltarget) {
        this.controltarget = controltarget;
    }
    public String getControlnumber() {
        return controlnumber;
    }

    public void setControlnumber(String controlnumber) {
        this.controlnumber = controlnumber;
    }
    public String getControlname() {
        return controlname;
    }

    public void setControlname(String controlname) {
        this.controlname = controlname;
    }
    public String getControlmanager() {
        return controlmanager;
    }

    public void setControlmanager(String controlmanager) {
        this.controlmanager = controlmanager;
    }
    public String getControlfrequency() {
        return controlfrequency;
    }

    public void setControlfrequency(String controlfrequency) {
        this.controlfrequency = controlfrequency;
    }
    public String getControltype() {
        return controltype;
    }

    public void setControltype(String controltype) {
        this.controltype = controltype;
    }
    public String getControlmethod() {
        return controlmethod;
    }

    public void setControlmethod(String controlmethod) {
        this.controlmethod = controlmethod;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getControldes() {
        return controldes;
    }

    public void setControldes(String controldes) {
        this.controldes = controldes;
    }
    public String getInsidecontroltarget() {
        return insidecontroltarget;
    }

    public void setInsidecontroltarget(String insidecontroltarget) {
        this.insidecontroltarget = insidecontroltarget;
    }
    public String getKeycontrol() {
        return keycontrol;
    }

    public void setKeycontrol(String keycontrol) {
        this.keycontrol = keycontrol;
    }
    public String getEffective() {
        return effective;
    }

    public void setEffective(String effective) {
        this.effective = effective;
    }
    public String getControltest() {
        return controltest;
    }

    public void setControltest(String controltest) {
        this.controltest = controltest;
    }
    public String getFinancialreportidentify() {
        return financialreportidentify;
    }

    public void setFinancialreportidentify(String financialreportidentify) {
        this.financialreportidentify = financialreportidentify;
    }
    public String getRelateddepart() {
        return relateddepart;
    }

    public void setRelateddepart(String relateddepart) {
        this.relateddepart = relateddepart;
    }
    public String getControldocument() {
        return controldocument;
    }

    public void setControldocument(String controldocument) {
        this.controldocument = controldocument;
    }
    public String getFlowcode() {
        return flowcode;
    }

    public void setFlowcode(String flowcode) {
        this.flowcode = flowcode;
    }
    public String getToplevelflowcat() {
        return toplevelflowcat;
    }

    public void setToplevelflowcat(String toplevelflowcat) {
        this.toplevelflowcat = toplevelflowcat;
    }
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    public String getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime;
    }
    public String getLastmodifiedtime() {
        return lastmodifiedtime;
    }

    public void setLastmodifiedtime(String lastmodifiedtime) {
        this.lastmodifiedtime = lastmodifiedtime;
    }
    public String getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }
    public String getConkzcs() {
        return conkzcs;
    }

    public void setConkzcs(String conkzcs) {
        this.conkzcs = conkzcs;
    }
    public BigDecimal getVersiontype() {
        return versiontype;
    }

    public void setVersiontype(BigDecimal versiontype) {
        this.versiontype = versiontype;
    }

    @Override
    public String toString() {
        return "Controlmatrix{" +
            "conmatid=" + conmatid +
            ", flowname=" + flowname +
            ", controltarget=" + controltarget +
            ", controlnumber=" + controlnumber +
            ", controlname=" + controlname +
            ", controlmanager=" + controlmanager +
            ", controlfrequency=" + controlfrequency +
            ", controltype=" + controltype +
            ", controlmethod=" + controlmethod +
            ", memo=" + memo +
            ", controldes=" + controldes +
            ", insidecontroltarget=" + insidecontroltarget +
            ", keycontrol=" + keycontrol +
            ", effective=" + effective +
            ", controltest=" + controltest +
            ", financialreportidentify=" + financialreportidentify +
            ", relateddepart=" + relateddepart +
            ", controldocument=" + controldocument +
            ", flowcode=" + flowcode +
            ", toplevelflowcat=" + toplevelflowcat +
            ", version=" + version +
            ", createdtime=" + createdtime +
            ", lastmodifiedtime=" + lastmodifiedtime +
            ", subsystem=" + subsystem +
            ", conkzcs=" + conkzcs +
            ", versiontype=" + versiontype +
        "}";
    }
}
