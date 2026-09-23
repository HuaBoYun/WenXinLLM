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
@TableName("TBL_FORM_ATT")
public class FormAtt implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal valueid;

    private BigDecimal attid;

    public BigDecimal getValueid() {
        return valueid;
    }

    public void setValueid(BigDecimal valueid) {
        this.valueid = valueid;
    }
    public BigDecimal getAttid() {
        return attid;
    }

    public void setAttid(BigDecimal attid) {
        this.attid = attid;
    }

    @Override
    public String toString() {
        return "FormAtt{" +
            "valueid=" + valueid +
            ", attid=" + attid +
        "}";
    }
}
