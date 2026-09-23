package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2022-09-09
 */
@TableName("TBL_TESTPLAN_MATRIX")
@Schema(name="TblTestplanMatrix对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblTestplanMatrix implements Serializable {

    private static final long serialVersionUID = 1L;
    // 联合主键
    @TableId(type= IdType.INPUT)
    private BigDecimal conmatid;
    // 联合主键
    private BigDecimal testplanid;



    private BigDecimal staffid;

    private String memo;

    private BigDecimal testtaskid;

    public BigDecimal getConmatid() {
        return conmatid;
    }

    public void setConmatid(BigDecimal conmatid) {
        this.conmatid = conmatid;
    }
    public BigDecimal getTestplanid() {
        return testplanid;
    }

    public void setTestplanid(BigDecimal testplanid) {
        this.testplanid = testplanid;
    }
    public BigDecimal getStaffid() {
        return staffid;
    }

    public void setStaffid(BigDecimal staffid) {
        this.staffid = staffid;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public BigDecimal getTesttaskid() {
        return testtaskid;
    }

    public void setTesttaskid(BigDecimal testtaskid) {
        this.testtaskid = testtaskid;
    }

    @Override
    public String toString() {
        return "TblTestplanMatrix{" +
            "conmatid=" + conmatid +
            ", testplanid=" + testplanid +
            ", staffid=" + staffid +
            ", memo=" + memo +
            ", testtaskid=" + testtaskid +
        "}";
    }
}
