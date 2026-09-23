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
 * @since 2022-07-30
 */
@TableName("TBL_WORKSHEET_ATT")
public class WorksheetAtt implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal worksheetid;

    private BigDecimal attid;

    public BigDecimal getWorksheetid() {
        return worksheetid;
    }

    public void setWorksheetid(BigDecimal worksheetid) {
        this.worksheetid = worksheetid;
    }
    public BigDecimal getAttid() {
        return attid;
    }

    public void setAttid(BigDecimal attid) {
        this.attid = attid;
    }

    @Override
    public String toString() {
        return "WorksheetAtt{" +
            "worksheetid=" + worksheetid +
            ", attid=" + attid +
        "}";
    }
}
