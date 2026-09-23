package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-09-08
 */
@TableName("TBL_COM_EXT_TESTTEMPLE")
@Schema(name="TblTesttemple对象")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblTesttemple implements Serializable {



    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.INPUT)
    private BigDecimal testtemid;

    private String templename;//模板名称

    private String templenumber;//模板编号

    private String memo;

    private LocalDateTime createdatetime;//创建时间

    private BigDecimal templestatus;

    private String templedesc;//模板说明

    private String tblcomany;//公司名称

    private BigDecimal staffid;//创建者id

    private String source;//来源：自建、上级公司名称

    public BigDecimal getTesttemid() {
        return testtemid;
    }

    public void setTesttemid(BigDecimal testtemid) {
        this.testtemid = testtemid;
    }
    public String getTemplename() {
        return templename;
    }

    public void setTemplename(String templename) {
        this.templename = templename;
    }
    public String getTemplenumber() {
        return templenumber;
    }

    public void setTemplenumber(String templenumber) {
        this.templenumber = templenumber;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public LocalDateTime getCreatedatetime() {
        return createdatetime;
    }

    public void setCreatedatetime(LocalDateTime createdatetime) {
        this.createdatetime = createdatetime;
    }
    public BigDecimal getTemplestatus() {
        return templestatus;
    }

    public void setTemplestatus(BigDecimal templestatus) {
        this.templestatus = templestatus;
    }
    public String getTempledesc() {
        return templedesc;
    }

    public void setTempledesc(String templedesc) {
        this.templedesc = templedesc;
    }
    public String getTblcomany() {
        return tblcomany;
    }

    public void setTblcomany(String tblcomany) {
        this.tblcomany = tblcomany;
    }
    public BigDecimal getStaffid() {
        return staffid;
    }

    public void setStaffid(BigDecimal staffid) {
        this.staffid = staffid;
    }
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "TblTesttemple{" +
            "testtemid=" + testtemid +
            ", templename=" + templename +
            ", templenumber=" + templenumber +
            ", memo=" + memo +
            ", createdatetime=" + createdatetime +
            ", templestatus=" + templestatus +
            ", templedesc=" + templedesc +
            ", tblcomany=" + tblcomany +
            ", staffid=" + staffid +
            ", source=" + source +
        "}";
    }
}
