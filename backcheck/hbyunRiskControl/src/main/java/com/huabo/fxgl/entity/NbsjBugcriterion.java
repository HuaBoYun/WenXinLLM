package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-10
 */
@TableName("TBL_NBSJ_BUGCRITERION")
public class NbsjBugcriterion implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal bugcriid;

    private String bugcrilevel;

    private String bugcridefine;

    private String bugcriration;

    private String bugcristability;

    private BigDecimal status;

    private BigDecimal version;

    private BigDecimal orgid;

    public BigDecimal getBugcriid() {
        return bugcriid;
    }

    public void setBugcriid(BigDecimal bugcriid) {
        this.bugcriid = bugcriid;
    }
    public String getBugcrilevel() {
        return bugcrilevel;
    }

    public void setBugcrilevel(String bugcrilevel) {
        this.bugcrilevel = bugcrilevel;
    }
    public String getBugcridefine() {
        return bugcridefine;
    }

    public void setBugcridefine(String bugcridefine) {
        this.bugcridefine = bugcridefine;
    }
    public String getBugcriration() {
        return bugcriration;
    }

    public void setBugcriration(String bugcriration) {
        this.bugcriration = bugcriration;
    }
    public String getBugcristability() {
        return bugcristability;
    }

    public void setBugcristability(String bugcristability) {
        this.bugcristability = bugcristability;
    }
    public BigDecimal getStatus() {
        return status;
    }

    public void setStatus(BigDecimal status) {
        this.status = status;
    }
    public BigDecimal getVersion() {
        return version;
    }

    public void setVersion(BigDecimal version) {
        this.version = version;
    }
    public BigDecimal getOrgid() {
        return orgid;
    }

    public void setOrgid(BigDecimal orgid) {
        this.orgid = orgid;
    }

    @Override
    public String toString() {
        return "NbsjBugcriterion{" +
            "bugcriid=" + bugcriid +
            ", bugcrilevel=" + bugcrilevel +
            ", bugcridefine=" + bugcridefine +
            ", bugcriration=" + bugcriration +
            ", bugcristability=" + bugcristability +
            ", status=" + status +
            ", version=" + version +
            ", orgid=" + orgid +
        "}";
    }
}
