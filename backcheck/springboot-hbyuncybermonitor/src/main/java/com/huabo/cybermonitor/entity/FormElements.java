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
@TableName("TBL_FORM_ELEMENTS")
public class FormElements implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal eleid;

    private BigDecimal formid;

    private String elelabel;

    private String elecode;

    private BigDecimal isrequire;

    private BigDecimal eleorder;

    private String eletype;

    private BigDecimal showlist;

    private String elename;

    private BigDecimal showorder;

    private String defaultvalue;

    public BigDecimal getEleid() {
        return eleid;
    }

    public void setEleid(BigDecimal eleid) {
        this.eleid = eleid;
    }
    public BigDecimal getFormid() {
        return formid;
    }

    public void setFormid(BigDecimal formid) {
        this.formid = formid;
    }
    public String getElelabel() {
        return elelabel;
    }

    public void setElelabel(String elelabel) {
        this.elelabel = elelabel;
    }
    public String getElecode() {
        return elecode;
    }

    public void setElecode(String elecode) {
        this.elecode = elecode;
    }
    public BigDecimal getIsrequire() {
        return isrequire;
    }

    public void setIsrequire(BigDecimal isrequire) {
        this.isrequire = isrequire;
    }
    public BigDecimal getEleorder() {
        return eleorder;
    }

    public void setEleorder(BigDecimal eleorder) {
        this.eleorder = eleorder;
    }
    public String getEletype() {
        return eletype;
    }

    public void setEletype(String eletype) {
        this.eletype = eletype;
    }
    public BigDecimal getShowlist() {
        return showlist;
    }

    public void setShowlist(BigDecimal showlist) {
        this.showlist = showlist;
    }
    public String getElename() {
        return elename;
    }

    public void setElename(String elename) {
        this.elename = elename;
    }
    public BigDecimal getShoworder() {
        return showorder;
    }

    public void setShoworder(BigDecimal showorder) {
        this.showorder = showorder;
    }
    public String getDefaultvalue() {
        return defaultvalue;
    }

    public void setDefaultvalue(String defaultvalue) {
        this.defaultvalue = defaultvalue;
    }

    @Override
    public String toString() {
        return "FormElements{" +
            "eleid=" + eleid +
            ", formid=" + formid +
            ", elelabel=" + elelabel +
            ", elecode=" + elecode +
            ", isrequire=" + isrequire +
            ", eleorder=" + eleorder +
            ", eletype=" + eletype +
            ", showlist=" + showlist +
            ", elename=" + elename +
            ", showorder=" + showorder +
            ", defaultvalue=" + defaultvalue +
        "}";
    }
}
