package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@TableName("TBL_AUDITFINDING")
@Schema(name="TblAuditfinding对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblAuditfinding implements Serializable {

    private static final long serialVersionUID = 1L;

    //TBL_WORKSHEET 外键
    @TableId(type= IdType.INPUT)
    private BigDecimal audfinid;

    private String probname;

    private String probdescribe;

    private String bussinessbelongsto;

    private String riskbelongsto;

    private String discoveryperson;

    private String proborgs;

    private String memo;

    private String explainitem;

    private String personincharge;

    private String probnumber;

    public BigDecimal getAudfinid() {
        return audfinid;
    }

    public void setAudfinid(BigDecimal audfinid) {
        this.audfinid = audfinid;
    }
    public String getProbname() {
        return probname;
    }

    public void setProbname(String probname) {
        this.probname = probname;
    }
    public String getProbdescribe() {
        return probdescribe;
    }

    public void setProbdescribe(String probdescribe) {
        this.probdescribe = probdescribe;
    }
    public String getBussinessbelongsto() {
        return bussinessbelongsto;
    }

    public void setBussinessbelongsto(String bussinessbelongsto) {
        this.bussinessbelongsto = bussinessbelongsto;
    }
    public String getRiskbelongsto() {
        return riskbelongsto;
    }

    public void setRiskbelongsto(String riskbelongsto) {
        this.riskbelongsto = riskbelongsto;
    }
    public String getDiscoveryperson() {
        return discoveryperson;
    }

    public void setDiscoveryperson(String discoveryperson) {
        this.discoveryperson = discoveryperson;
    }
    public String getProborgs() {
        return proborgs;
    }

    public void setProborgs(String proborgs) {
        this.proborgs = proborgs;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getExplainitem() {
        return explainitem;
    }

    public void setExplainitem(String explainitem) {
        this.explainitem = explainitem;
    }
    public String getPersonincharge() {
        return personincharge;
    }

    public void setPersonincharge(String personincharge) {
        this.personincharge = personincharge;
    }
    public String getProbnumber() {
        return probnumber;
    }

    public void setProbnumber(String probnumber) {
        this.probnumber = probnumber;
    }

    @Override
    public String toString() {
        return "TblAuditfinding{" +
            "audfinid=" + audfinid +
            ", probname=" + probname +
            ", probdescribe=" + probdescribe +
            ", bussinessbelongsto=" + bussinessbelongsto +
            ", riskbelongsto=" + riskbelongsto +
            ", discoveryperson=" + discoveryperson +
            ", proborgs=" + proborgs +
            ", memo=" + memo +
            ", explainitem=" + explainitem +
            ", personincharge=" + personincharge +
            ", probnumber=" + probnumber +
        "}";
    }
}
