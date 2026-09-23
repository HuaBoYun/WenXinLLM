package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("TBL_INDICATORSYSTEM")
public class Indicatorsystem implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal indcatid;

    private String indcatnumber;

    private String indcatname;

    private BigDecimal fatherindcatid;

    private String fullpath;

    private String memo;

    public BigDecimal getIndcatid() {
        return indcatid;
    }

    public void setIndcatid(BigDecimal indcatid) {
        this.indcatid = indcatid;
    }
    public String getIndcatnumber() {
        return indcatnumber;
    }

    public void setIndcatnumber(String indcatnumber) {
        this.indcatnumber = indcatnumber;
    }
    public String getIndcatname() {
        return indcatname;
    }

    public void setIndcatname(String indcatname) {
        this.indcatname = indcatname;
    }
    public BigDecimal getFatherindcatid() {
        return fatherindcatid;
    }

    public void setFatherindcatid(BigDecimal fatherindcatid) {
        this.fatherindcatid = fatherindcatid;
    }
    public String getFullpath() {
        return fullpath;
    }

    public void setFullpath(String fullpath) {
        this.fullpath = fullpath;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return "Indicatorsystem{" +
            "indcatid=" + indcatid +
            ", indcatnumber=" + indcatnumber +
            ", indcatname=" + indcatname +
            ", fatherindcatid=" + fatherindcatid +
            ", fullpath=" + fullpath +
            ", memo=" + memo +
        "}";
    }
}
