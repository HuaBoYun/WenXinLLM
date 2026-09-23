package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author kangjx
 * @since 2022-07-14
 */
@TableName("TBL_MONITOR_MODEL")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class MonitorModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**是否行业库  0标识否*/
    public static final Integer IS_HY0 = 0;
    /**是否行业库  1标识是*/
    public static final Integer IS_HY1 = 1;
    /**是否行业库  2标识是通用行业*/
    public static final Integer IS_HY2 = 2;

    @TableId(type= IdType.INPUT)
    private BigDecimal modelid;

    private String modelname;

    private String modeldes;

    private String modelcategory;

    private String modelstatus;

    private String orgid;

    private String staffid;

    @JsonFormat(pattern = "yyyy年MM月dd日")
    @Schema(name = "yyyy年MM月dd")
    private LocalDateTime createdate;

    private String modelreminder;

    private String connectionstrings;

    private String modelstep1;

    private String modelstep2;

    private String modelstep3;

    private String modelstep4;

    private String modelstep5;

    private String exeinterval;

    private String modelcode;

    private String inmodeldb;

    private Integer runstatus;

    private Integer subsystem;

    private String memo;

    private String bussinesscat;

    public BigDecimal getModelid() {
        return modelid;
    }

    public void setModelid(BigDecimal modelid) {
        this.modelid = modelid;
    }
    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }
    public String getModeldes() {
        return modeldes;
    }

    public void setModeldes(String modeldes) {
        this.modeldes = modeldes;
    }
    public String getModelcategory() {
        return modelcategory;
    }

    public void setModelcategory(String modelcategory) {
        this.modelcategory = modelcategory;
    }
    public String getModelstatus() {
        return modelstatus;
    }

    public void setModelstatus(String modelstatus) {
        this.modelstatus = modelstatus;
    }
    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }
    public String getStaffid() {
        return staffid;
    }

    public void setStaffid(String staffid) {
        this.staffid = staffid;
    }
    public LocalDateTime getCreatedate() {
        return createdate;
    }

    public void setCreatedate(LocalDateTime createdate) {
        this.createdate = createdate;
    }
    public String getModelreminder() {
        return modelreminder;
    }

    public void setModelreminder(String modelreminder) {
        this.modelreminder = modelreminder;
    }
    public String getConnectionstrings() {
        return connectionstrings;
    }

    public void setConnectionstrings(String connectionstrings) {
        this.connectionstrings = connectionstrings;
    }
    public String getModelstep1() {
        return modelstep1;
    }

    public void setModelstep1(String modelstep1) {
        this.modelstep1 = modelstep1;
    }
    public String getModelstep2() {
        return modelstep2;
    }

    public void setModelstep2(String modelstep2) {
        this.modelstep2 = modelstep2;
    }
    public String getModelstep3() {
        return modelstep3;
    }

    public void setModelstep3(String modelstep3) {
        this.modelstep3 = modelstep3;
    }
    public String getModelstep4() {
        return modelstep4;
    }

    public void setModelstep4(String modelstep4) {
        this.modelstep4 = modelstep4;
    }
    public String getModelstep5() {
        return modelstep5;
    }

    public void setModelstep5(String modelstep5) {
        this.modelstep5 = modelstep5;
    }
    public String getExeinterval() {
        return exeinterval;
    }

    public void setExeinterval(String exeinterval) {
        this.exeinterval = exeinterval;
    }
    public String getModelcode() {
        return modelcode;
    }

    public void setModelcode(String modelcode) {
        this.modelcode = modelcode;
    }
    public String getInmodeldb() {
        return inmodeldb;
    }

    public void setInmodeldb(String inmodeldb) {
        this.inmodeldb = inmodeldb;
    }
    public Integer getRunstatus() {
        return runstatus;
    }

    public void setRunstatus(Integer runstatus) {
        this.runstatus = runstatus;
    }
    public Integer getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(Integer subsystem) {
        this.subsystem = subsystem;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getBussinesscat() {
        return bussinesscat;
    }

    public void setBussinesscat(String bussinesscat) {
        this.bussinesscat = bussinesscat;
    }

    @Override
    public String toString() {
        return "MonitorModel{" +
                "modelid=" + modelid +
                ", modelname=" + modelname +
                ", modeldes=" + modeldes +
                ", modelcategory=" + modelcategory +
                ", modelstatus=" + modelstatus +
                ", orgid=" + orgid +
                ", staffid=" + staffid +
                ", createdate=" + createdate +
                ", modelreminder=" + modelreminder +
                ", connectionstrings=" + connectionstrings +
                ", modelstep1=" + modelstep1 +
                ", modelstep2=" + modelstep2 +
                ", modelstep3=" + modelstep3 +
                ", modelstep4=" + modelstep4 +
                ", modelstep5=" + modelstep5 +
                ", exeinterval=" + exeinterval +
                ", modelcode=" + modelcode +
                ", inmodeldb=" + inmodeldb +
                ", runstatus=" + runstatus +
                ", subsystem=" + subsystem +
                ", memo=" + memo +
                ", bussinesscat=" + bussinesscat +
                "}";
    }
}
