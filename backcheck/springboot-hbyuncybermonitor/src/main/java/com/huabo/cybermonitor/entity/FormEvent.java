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
@TableName("TBL_FORM_EVENT")
public class FormEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal eventid;

    private BigDecimal formid;

    private BigDecimal eventtype;

    private String eventcode;

    public BigDecimal getEventid() {
        return eventid;
    }

    public void setEventid(BigDecimal eventid) {
        this.eventid = eventid;
    }
    public BigDecimal getFormid() {
        return formid;
    }

    public void setFormid(BigDecimal formid) {
        this.formid = formid;
    }
    public BigDecimal getEventtype() {
        return eventtype;
    }

    public void setEventtype(BigDecimal eventtype) {
        this.eventtype = eventtype;
    }
    public String getEventcode() {
        return eventcode;
    }

    public void setEventcode(String eventcode) {
        this.eventcode = eventcode;
    }

    @Override
    public String toString() {
        return "FormEvent{" +
            "eventid=" + eventid +
            ", formid=" + formid +
            ", eventtype=" + eventtype +
            ", eventcode=" + eventcode +
        "}";
    }
}
