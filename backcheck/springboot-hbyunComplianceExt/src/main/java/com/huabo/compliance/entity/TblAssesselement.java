package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@TableName("TBL_ASSESSELEMENT")
@Schema(name="TblAssesselement对象")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblAssesselement implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    //TBL_ASSESS_MARK 外键 一对多
    @TableId(type= IdType.INPUT)
    private BigDecimal asseleid;

    private String elementname;

    private BigDecimal standardscore;

    private String memo;

    private String businesstype;

    private String businessattribute;

    private String auditpoint;

    private double assessrules;

    private String status;

    private String elementnumber;//要素编号

    private String tblcomany;
	public String getElementnumber() {
		return elementnumber;
	}

	public void setElementnumber(String elementnumber) {
		this.elementnumber = elementnumber;
	}

    public BigDecimal getAsseleid() {
        return asseleid;
    }

    public void setAsseleid(BigDecimal asseleid) {
        this.asseleid = asseleid;
    }
    public String getElementname() {
        return elementname;
    }

    public void setElementname(String elementname) {
        this.elementname = elementname;
    }
    public BigDecimal getStandardscore() {
        return standardscore;
    }

    public void setStandardscore(BigDecimal standardscore) {
        this.standardscore = standardscore;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getBusinesstype() {
        return businesstype;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
    }
    public String getBusinessattribute() {
        return businessattribute;
    }

    public void setBusinessattribute(String businessattribute) {
        this.businessattribute = businessattribute;
    }
    public String getAuditpoint() {
        return auditpoint;
    }

    public void setAuditpoint(String auditpoint) {
        this.auditpoint = auditpoint;
    }
    public double getAssessrules() {
        return assessrules;
    }

    public void setAssessrules(double assessrules) {
        this.assessrules = assessrules;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTblcomany() {
        return tblcomany;
    }

    public void setTblcomany(String tblcomany) {
        this.tblcomany = tblcomany;
    }

    @Override
    public String toString() {
        return "TblAssesselement{" +
            "asseleid=" + asseleid +
            ", elementname=" + elementname +
            ", standardscore=" + standardscore +
            ", memo=" + memo +
            ", businesstype=" + businesstype +
            ", businessattribute=" + businessattribute +
            ", auditpoint=" + auditpoint +
            ", assessrules=" + assessrules +
            ", status=" + status +
            ", elementnumber=" + elementnumber +
            ", tblcomany=" + tblcomany +
        "}";
    }
}
