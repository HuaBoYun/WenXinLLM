package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@TableName("TBL_EXTERNAL_EXPERT")
@Schema(name="TblExternalExpert对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblExternalExpert implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type= IdType.INPUT)
    private BigDecimal exterid;

    private String company;

    private String expertise;

    private String qualification;

    private BigDecimal staffid;

    public BigDecimal getExterid() {
        return exterid;
    }

    public void setExterid(BigDecimal exterid) {
        this.exterid = exterid;
    }
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }
    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
    public BigDecimal getStaffid() {
        return staffid;
    }

    public void setStaffid(BigDecimal staffid) {
        this.staffid = staffid;
    }

    @Override
    public String toString() {
        return "TblExternalExpert{" +
            "exterid=" + exterid +
            ", company=" + company +
            ", expertise=" + expertise +
            ", qualification=" + qualification +
            ", staffid=" + staffid +
        "}";
    }
}
