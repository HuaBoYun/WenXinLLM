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
@TableName("TBL_FORM_INFO")
public class FormInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal formid;

    private String formno;

    private String formname;

    private BigDecimal formstatus;

    private LocalDateTime createtime;

    private BigDecimal creator;

    private BigDecimal orgid;

    private String formdesc;

    private BigDecimal formversion;

    private BigDecimal formgroup;

    private String formcode;

    private String formeditcode;

    private BigDecimal formfile;

    private BigDecimal flowid;

    private BigDecimal showrelationinfo;

    public BigDecimal getFormid() {
        return formid;
    }

    public void setFormid(BigDecimal formid) {
        this.formid = formid;
    }
    public String getFormno() {
        return formno;
    }

    public void setFormno(String formno) {
        this.formno = formno;
    }
    public String getFormname() {
        return formname;
    }

    public void setFormname(String formname) {
        this.formname = formname;
    }
    public BigDecimal getFormstatus() {
        return formstatus;
    }

    public void setFormstatus(BigDecimal formstatus) {
        this.formstatus = formstatus;
    }
    public LocalDateTime getCreatetime() {
        return createtime;
    }

    public void setCreatetime(LocalDateTime createtime) {
        this.createtime = createtime;
    }
    public BigDecimal getCreator() {
        return creator;
    }

    public void setCreator(BigDecimal creator) {
        this.creator = creator;
    }
    public BigDecimal getOrgid() {
        return orgid;
    }

    public void setOrgid(BigDecimal orgid) {
        this.orgid = orgid;
    }
    public String getFormdesc() {
        return formdesc;
    }

    public void setFormdesc(String formdesc) {
        this.formdesc = formdesc;
    }
    public BigDecimal getFormversion() {
        return formversion;
    }

    public void setFormversion(BigDecimal formversion) {
        this.formversion = formversion;
    }
    public BigDecimal getFormgroup() {
        return formgroup;
    }

    public void setFormgroup(BigDecimal formgroup) {
        this.formgroup = formgroup;
    }
    public String getFormcode() {
        return formcode;
    }

    public void setFormcode(String formcode) {
        this.formcode = formcode;
    }
    public String getFormeditcode() {
        return formeditcode;
    }

    public void setFormeditcode(String formeditcode) {
        this.formeditcode = formeditcode;
    }
    public BigDecimal getFormfile() {
        return formfile;
    }

    public void setFormfile(BigDecimal formfile) {
        this.formfile = formfile;
    }
    public BigDecimal getFlowid() {
        return flowid;
    }

    public void setFlowid(BigDecimal flowid) {
        this.flowid = flowid;
    }
    public BigDecimal getShowrelationinfo() {
        return showrelationinfo;
    }

    public void setShowrelationinfo(BigDecimal showrelationinfo) {
        this.showrelationinfo = showrelationinfo;
    }

    @Override
    public String toString() {
        return "FormInfo{" +
            "formid=" + formid +
            ", formno=" + formno +
            ", formname=" + formname +
            ", formstatus=" + formstatus +
            ", createtime=" + createtime +
            ", creator=" + creator +
            ", orgid=" + orgid +
            ", formdesc=" + formdesc +
            ", formversion=" + formversion +
            ", formgroup=" + formgroup +
            ", formcode=" + formcode +
            ", formeditcode=" + formeditcode +
            ", formfile=" + formfile +
            ", flowid=" + flowid +
            ", showrelationinfo=" + showrelationinfo +
        "}";
    }
}
