package com.huabo.monitor.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * TblYyQueryPriceId entity. @author MyEclipse Persistence Tools
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YY_QUERY_PRICE")
@Schema(name="TblYyQueryPriceMySql对象")
public class TblYyQueryPriceIdMySql implements java.io.Serializable {

    // Fields

    /**
     *
     */
    private static final long serialVersionUID = 4898647194982541226L;
    @TableField("PRICEID")
    private BigDecimal priceid;
    @TableField("RECORDID")
    private BigDecimal recordid;
    @TableField("PRICEMONEY")
    private Double priceMoney;

    // Constructors

    /**
     * default constructor
     */
    public TblYyQueryPriceIdMySql() {
    }

    /**
     * full constructor
     */
    public TblYyQueryPriceIdMySql(BigDecimal priceid, BigDecimal recordid, Double priceMoney) {
        this.priceid = priceid;
        this.recordid = recordid;
        this.priceMoney = priceMoney;
    }

    // Property accessors

    public BigDecimal getPriceid() {
        return this.priceid;
    }

    public void setPriceid(BigDecimal priceid) {
        this.priceid = priceid;
    }

    public BigDecimal getRecordid() {
        return this.recordid;
    }

    public void setRecordid(BigDecimal recordid) {
        this.recordid = recordid;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof TblYyQueryPriceIdMySql))
            return false;
        TblYyQueryPriceIdMySql castOther = (TblYyQueryPriceIdMySql) other;

        return ((this.getPriceid() == castOther.getPriceid()) || (this
                .getPriceid() != null && castOther.getPriceid() != null && this
                .getPriceid().equals(castOther.getPriceid())))
                && ((this.getRecordid() == castOther.getRecordid()) || (this
                .getRecordid() != null
                && castOther.getRecordid() != null && this
                .getRecordid().equals(castOther.getRecordid())));
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result
                + (getPriceid() == null ? 0 : this.getPriceid().hashCode());
        result = 37 * result
                + (getRecordid() == null ? 0 : this.getRecordid().hashCode());
        return result;
    }

    public Double getPriceMoney() {
        return priceMoney;
    }

    public void setPriceMoney(Double priceMoney) {
        this.priceMoney = priceMoney;
    }

}