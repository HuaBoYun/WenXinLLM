package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author ccc
 * @since 2022-07-15
 */
@TableName("TBL_OUTFORM_FIELD")
public class OutformField implements Serializable {

    private static final long serialVersionUID = 1L;

    private Set<OutformField> tblOutformFields = new HashSet<OutformField>(0);
    private Set<FormControlrule> tblFormControlrules = new HashSet<FormControlrule>(0);
    private OutformField tblOutformField;
    @TableId
    private BigDecimal fieldid;

    private String fieldname;

    private BigDecimal parentsid;

    private String fieldtype;

    public BigDecimal getFieldid() {
        return fieldid;
    }

    public void setFieldid(BigDecimal fieldid) {
        this.fieldid = fieldid;
    }
    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }
    public BigDecimal getParentsid() {
        return parentsid;
    }

    public void setParentsid(BigDecimal parentsid) {
        this.parentsid = parentsid;
    }
    public String getFieldtype() {
        return fieldtype;
    }

    public void setFieldtype(String fieldtype) {
        this.fieldtype = fieldtype;
    }

    public Set<OutformField> getTblOutformFields() {
        return tblOutformFields;
    }

    public void setTblOutformFields(Set<OutformField> tblOutformFields) {
        this.tblOutformFields = tblOutformFields;
    }

    public OutformField getTblOutformField() {
        return tblOutformField;
    }

    public void setTblOutformField(OutformField tblOutformField) {
        this.tblOutformField = tblOutformField;
    }

    public Set<FormControlrule> getTblFormControlrules() {
        return tblFormControlrules;
    }

    public void setTblFormControlrules(Set<FormControlrule> tblFormControlrules) {
        this.tblFormControlrules = tblFormControlrules;
    }

    @Override
    public String toString() {
        return "OutformField{" +
                "fieldid=" + fieldid +
                ", fieldname=" + fieldname +
                ", parentsid=" + parentsid +
                ", fieldtype=" + fieldtype +
                "}";
    }
}
