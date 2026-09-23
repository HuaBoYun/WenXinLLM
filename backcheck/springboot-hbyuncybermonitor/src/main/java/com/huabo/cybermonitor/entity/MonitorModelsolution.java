package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author kangjx
 * @since 2022-07-22
 */
@TableName("TBL_MONITOR_MODELSOLUTION")
public class MonitorModelsolution implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private BigDecimal solutionid;

    private String solutioncode;

    private String solutionname;

    private String solutionstatus;

    private String creater;

    private LocalDateTime createdate;

    private String memo;

    public BigDecimal getSolutionid() {
        return solutionid;
    }

    public void setSolutionid(BigDecimal solutionid) {
        this.solutionid = solutionid;
    }
    public String getSolutioncode() {
        return solutioncode;
    }

    public void setSolutioncode(String solutioncode) {
        this.solutioncode = solutioncode;
    }
    public String getSolutionname() {
        return solutionname;
    }

    public void setSolutionname(String solutionname) {
        this.solutionname = solutionname;
    }
    public String getSolutionstatus() {
        return solutionstatus;
    }

    public void setSolutionstatus(String solutionstatus) {
        this.solutionstatus = solutionstatus;
    }
    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }
    public LocalDateTime getCreatedate() {
        return createdate;
    }

    public void setCreatedate(LocalDateTime createdate) {
        this.createdate = createdate;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return "MonitorModelsolution{" +
            "solutionid=" + solutionid +
            ", solutioncode=" + solutioncode +
            ", solutionname=" + solutionname +
            ", solutionstatus=" + solutionstatus +
            ", creater=" + creater +
            ", createdate=" + createdate +
            ", memo=" + memo +
        "}";
    }
}
