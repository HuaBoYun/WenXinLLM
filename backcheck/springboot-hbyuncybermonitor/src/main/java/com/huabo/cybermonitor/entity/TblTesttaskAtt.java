package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author yhr
 * @since 2022-09-09
 */
@TableName("TBL_TESTTASK_ATT")
@Schema(name="TblTesttaskAtt对象")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblTesttaskAtt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema
    private BigDecimal attid;

    @Schema
    private BigDecimal testtaskid;

    public BigDecimal getAttid() {
        return attid;
    }

    public void setAttid(BigDecimal attid) {
        this.attid = attid;
    }
    public BigDecimal getTesttaskid() {
        return testtaskid;
    }

    public void setTesttaskid(BigDecimal testtaskid) {
        this.testtaskid = testtaskid;
    }

    @Override
    public String toString() {
        return "TblTesttaskAtt{" +
            "attid=" + attid +
            ", testtaskid=" + testtaskid +
        "}";
    }
}
