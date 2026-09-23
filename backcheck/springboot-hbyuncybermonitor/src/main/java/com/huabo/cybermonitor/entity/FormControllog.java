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
@TableName("TBL_FORM_CONTROLLOG")
public class FormControllog implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal rulelogid;

    private LocalDateTime createdate;

    private String operation;

    private String returnresult;

    private String inparam;

    private String ruleno;

    private String executestaff;

    private LocalDateTime returndate;

    public BigDecimal getRulelogid() {
        return rulelogid;
    }

    public void setRulelogid(BigDecimal rulelogid) {
        this.rulelogid = rulelogid;
    }
    public LocalDateTime getCreatedate() {
        return createdate;
    }

    public void setCreatedate(LocalDateTime createdate) {
        this.createdate = createdate;
    }
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
    public String getReturnresult() {
        return returnresult;
    }

    public void setReturnresult(String returnresult) {
        this.returnresult = returnresult;
    }
    public String getInparam() {
        return inparam;
    }

    public void setInparam(String inparam) {
        this.inparam = inparam;
    }
    public String getRuleno() {
        return ruleno;
    }

    public void setRuleno(String ruleno) {
        this.ruleno = ruleno;
    }
    public String getExecutestaff() {
        return executestaff;
    }

    public void setExecutestaff(String executestaff) {
        this.executestaff = executestaff;
    }
    public LocalDateTime getReturndate() {
        return returndate;
    }

    public void setReturndate(LocalDateTime returndate) {
        this.returndate = returndate;
    }

    @Override
    public String toString() {
        return "FormControllog{" +
            "rulelogid=" + rulelogid +
            ", createdate=" + createdate +
            ", operation=" + operation +
            ", returnresult=" + returnresult +
            ", inparam=" + inparam +
            ", ruleno=" + ruleno +
            ", executestaff=" + executestaff +
            ", returndate=" + returndate +
        "}";
    }
}
