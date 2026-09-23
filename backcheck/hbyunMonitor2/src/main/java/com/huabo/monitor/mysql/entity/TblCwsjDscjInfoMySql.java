package com.huabo.monitor.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import javax.persistence.Transient;
import java.util.Date;

public class TblCwsjDscjInfoMySql {
    @TableId("INFOID")
    private Integer infoId;
    @TableField("INFONAME")
    private String infoName;
    @TableField("ORGID")
    private Integer orgId;
    @TableField("SETDATE")
    private String setDate;
    @TableField("ACSPACE")
    private Integer acSpace;
    @TableField("CREATETIME")
    private Date createTime;
    @TableField("ACCTID")
    private String acctId;
    @TableField("STATUS")
    private Integer status;
    @TableField("ACWEEK")
    private String acWeek;
    @TableField("STAFFID")
    private Integer staffId;
    @Transient
    private String realName;
    @Transient
    private String orgName;


    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Integer getInfoId() {
        return infoId;
    }

    public void setInfoId(Integer infoId) {
        this.infoId = infoId;
    }

    public String getInfoName() {
        return infoName;
    }

    public void setInfoName(String infoName) {
        this.infoName = infoName;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getSetDate() {
        return setDate;
    }

    public void setSetDate(String setDate) {
        this.setDate = setDate;
    }

    public Integer getAcSpace() {
        return acSpace;
    }

    public void setAcSpace(Integer acSpace) {
        this.acSpace = acSpace;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAcWeek() {
        return acWeek;
    }

    public void setAcWeek(String acWeek) {
        this.acWeek = acWeek;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }
}
