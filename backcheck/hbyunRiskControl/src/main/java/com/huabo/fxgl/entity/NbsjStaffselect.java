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
 * @since 2022-08-18
 */
@TableName("TBL_NBSJ_STAFFSELECT")
public class NbsjStaffselect implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal selectid;

    private BigDecimal projectid;

    private BigDecimal staffid;

    public BigDecimal getSelectid() {
        return selectid;
    }

    public void setSelectid(BigDecimal selectid) {
        this.selectid = selectid;
    }
    public BigDecimal getProjectid() {
        return projectid;
    }

    public void setProjectid(BigDecimal projectid) {
        this.projectid = projectid;
    }
    public BigDecimal getStaffid() {
        return staffid;
    }

    public void setStaffid(BigDecimal staffid) {
        this.staffid = staffid;
    }

    @Override
    public String toString() {
        return "NbsjStaffselect{" +
            "selectid=" + selectid +
            ", projectid=" + projectid +
            ", staffid=" + staffid +
        "}";
    }
}
