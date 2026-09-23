package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-02
 */
@TableName("TBL_YY_PRICE")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class YyPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private String priceid;

    private String interfacename;

    private String accountrule;

    private BigDecimal price;

    private String annualprice;

    private BigDecimal companyid;

    private BigDecimal hbprice;

    public String getPriceid() {
        return priceid;
    }

    public void setPriceid(String priceid) {
        this.priceid = priceid;
    }
    public String getInterfacename() {
        return interfacename;
    }

    public void setInterfacename(String interfacename) {
        this.interfacename = interfacename;
    }
    public String getAccountrule() {
        return accountrule;
    }

    public void setAccountrule(String accountrule) {
        this.accountrule = accountrule;
    }
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public String getAnnualprice() {
        return annualprice;
    }

    public void setAnnualprice(String annualprice) {
        this.annualprice = annualprice;
    }
    public BigDecimal getCompanyid() {
        return companyid;
    }

    public void setCompanyid(BigDecimal companyid) {
        this.companyid = companyid;
    }
    public BigDecimal getHbprice() {
        return hbprice;
    }

    public void setHbprice(BigDecimal hbprice) {
        this.hbprice = hbprice;
    }

    @Override
    public String toString() {
        return "YyPrice{" +
            "priceid=" + priceid +
            ", interfacename=" + interfacename +
            ", accountrule=" + accountrule +
            ", price=" + price +
            ", annualprice=" + annualprice +
            ", companyid=" + companyid +
            ", hbprice=" + hbprice +
        "}";
    }
}
