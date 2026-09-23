package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-15
 */
@Data
@TableName("TBL_ORG_NO")
public class OrgNo implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal orgid;

    private BigDecimal noid;

    private String nocode;

    private String nosepartor;

    private BigDecimal nonumber;

    private BigDecimal isusedefault;

    private BigDecimal nosuffix;

    public BigDecimal getOrgid() {
        return orgid;
    }

    public void setOrgid(BigDecimal orgid) {
        this.orgid = orgid;
    }
    public BigDecimal getNoid() {
        return noid;
    }

    public void setNoid(BigDecimal noid) {
        this.noid = noid;
    }
    public String getNocode() {
        return nocode;
    }

    public void setNocode(String nocode) {
        this.nocode = nocode;
    }
    public String getNosepartor() {
        return nosepartor;
    }

    public void setNosepartor(String nosepartor) {
        this.nosepartor = nosepartor;
    }
    public BigDecimal getNonumber() {
        return nonumber;
    }

    public void setNonumber(BigDecimal nonumber) {
        this.nonumber = nonumber;
    }
    public BigDecimal getIsusedefault() {
        return isusedefault;
    }

    public void setIsusedefault(BigDecimal isusedefault) {
        this.isusedefault = isusedefault;
    }
    public BigDecimal getNosuffix() {
        return nosuffix;
    }

    public void setNosuffix(BigDecimal nosuffix) {
        this.nosuffix = nosuffix;
    }

    @Override
    public String toString() {
        return "OrgNo{" +
            "orgid=" + orgid +
            ", noid=" + noid +
            ", nocode=" + nocode +
            ", nosepartor=" + nosepartor +
            ", nonumber=" + nonumber +
            ", isusedefault=" + isusedefault +
            ", nosuffix=" + nosuffix +
        "}";
    }
}
