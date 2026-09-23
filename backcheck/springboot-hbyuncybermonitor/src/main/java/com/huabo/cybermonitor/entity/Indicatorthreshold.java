package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author kangjx
 * @since 2022-07-13
 */
@TableName("TBL_INDICATORTHRESHOLD")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class Indicatorthreshold implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type= IdType.INPUT)
    private BigDecimal thresholdid;


    private String sequencenumber;

    private String tolerance;

    private String thresholdname;

    private String regionvalue;

    private String prewarningmethod;

    private String memo;

    private Integer indicatorid;

    private String tolerancelower;

    private String toleranceupper;

    public BigDecimal getThresholdid() {
        return thresholdid;
    }

    public void setThresholdid(BigDecimal thresholdid) {
        this.thresholdid = thresholdid;
    }
    public String getSequencenumber() {
        return sequencenumber;
    }

    public void setSequencenumber(String sequencenumber) {
        this.sequencenumber = sequencenumber;
    }
    public String getTolerance() {
        return tolerance;
    }

    public void setTolerance(String tolerance) {
        this.tolerance = tolerance;
    }
    public String getThresholdname() {
        return thresholdname;
    }

    public void setThresholdname(String thresholdname) {
        this.thresholdname = thresholdname;
    }
    public String getRegionvalue() {
        return regionvalue;
    }

    public void setRegionvalue(String regionvalue) {
        this.regionvalue = regionvalue;
    }
    public String getPrewarningmethod() {
        return prewarningmethod;
    }

    public void setPrewarningmethod(String prewarningmethod) {
        this.prewarningmethod = prewarningmethod;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public Integer getIndicatorid() {
        return indicatorid;
    }

    public void setIndicatorid(Integer indicatorid) {
        this.indicatorid = indicatorid;
    }
    public String getTolerancelower() {
        return tolerancelower;
    }

    public void setTolerancelower(String tolerancelower) {
        this.tolerancelower = tolerancelower;
    }
    public String getToleranceupper() {
        return toleranceupper;
    }

    public void setToleranceupper(String toleranceupper) {
        this.toleranceupper = toleranceupper;
    }

    @Override
    public String toString() {
        return "Indicatorthreshold{" +
            "thresholdid=" + thresholdid +
            ", sequencenumber=" + sequencenumber +
            ", tolerance=" + tolerance +
            ", thresholdname=" + thresholdname +
            ", regionvalue=" + regionvalue +
            ", prewarningmethod=" + prewarningmethod +
            ", memo=" + memo +
            ", indicatorid=" + indicatorid +
            ", tolerancelower=" + tolerancelower +
            ", toleranceupper=" + toleranceupper +
        "}";
    }
}
