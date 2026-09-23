package com.huabo.cybermonitor.entity;

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
 * @since 2022-07-13
 */
@TableName("TBL_INDICATORPREWARNING")
public class Indicatorprewarning implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal prewarningid;

    private LocalDateTime prewarningtime;

    private LocalDateTime datastarttime;

    private LocalDateTime dataendtime;

    private BigDecimal datavalue;

    private String dataunit;

    private String prewarningregion;

    private String prewarningstatus;

    private String analysisdes;

    private BigDecimal indicatorid;

    private String memo;

    public BigDecimal getPrewarningid() {
        return prewarningid;
    }

    public void setPrewarningid(BigDecimal prewarningid) {
        this.prewarningid = prewarningid;
    }
    public LocalDateTime getPrewarningtime() {
        return prewarningtime;
    }

    public void setPrewarningtime(LocalDateTime prewarningtime) {
        this.prewarningtime = prewarningtime;
    }
    public LocalDateTime getDatastarttime() {
        return datastarttime;
    }

    public void setDatastarttime(LocalDateTime datastarttime) {
        this.datastarttime = datastarttime;
    }
    public LocalDateTime getDataendtime() {
        return dataendtime;
    }

    public void setDataendtime(LocalDateTime dataendtime) {
        this.dataendtime = dataendtime;
    }
    public BigDecimal getDatavalue() {
        return datavalue;
    }

    public void setDatavalue(BigDecimal datavalue) {
        this.datavalue = datavalue;
    }
    public String getDataunit() {
        return dataunit;
    }

    public void setDataunit(String dataunit) {
        this.dataunit = dataunit;
    }
    public String getPrewarningregion() {
        return prewarningregion;
    }

    public void setPrewarningregion(String prewarningregion) {
        this.prewarningregion = prewarningregion;
    }
    public String getPrewarningstatus() {
        return prewarningstatus;
    }

    public void setPrewarningstatus(String prewarningstatus) {
        this.prewarningstatus = prewarningstatus;
    }
    public String getAnalysisdes() {
        return analysisdes;
    }

    public void setAnalysisdes(String analysisdes) {
        this.analysisdes = analysisdes;
    }
    public BigDecimal getIndicatorid() {
        return indicatorid;
    }

    public void setIndicatorid(BigDecimal indicatorid) {
        this.indicatorid = indicatorid;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return "Indicatorprewarning{" +
            "prewarningid=" + prewarningid +
            ", prewarningtime=" + prewarningtime +
            ", datastarttime=" + datastarttime +
            ", dataendtime=" + dataendtime +
            ", datavalue=" + datavalue +
            ", dataunit=" + dataunit +
            ", prewarningregion=" + prewarningregion +
            ", prewarningstatus=" + prewarningstatus +
            ", analysisdes=" + analysisdes +
            ", indicatorid=" + indicatorid +
            ", memo=" + memo +
        "}";
    }
}
