package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 
 * </p>
 *
 * @author kangjx
 * @since 2022-07-22
 */
@TableName("TBL_ACCBOOK")
public class Accbook implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private BigDecimal id;
    @TableField(exist = false)
    private String acctname;

    @TableId
    private String bookid;

    private String bookname;

    private BigDecimal orgid;

    private String orgname;

    private String acctid;

    private String bookdesc;

    private String bookyear;

    private String balancesheeturl;

    private String incomestatementsurl;

    private String cashflowstatementsurl;

    @TableField(exist = false)
    private Set<Accbook> children = new HashSet<Accbook>();

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getAcctname() {
        return acctname;
    }

    public void setAcctname(String acctname) {
        this.acctname = acctname;
    }

    public Set<Accbook> getChildren() {
        return children;
    }

    public void setChildren(Set<Accbook> children) {
        this.children = children;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }
    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }
    public BigDecimal getOrgid() {
        return orgid;
    }

    public void setOrgid(BigDecimal orgid) {
        this.orgid = orgid;
    }
    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }
    public String getAcctid() {
        return acctid;
    }

    public void setAcctid(String acctid) {
        this.acctid = acctid;
    }
    public String getBookdesc() {
        return bookdesc;
    }

    public void setBookdesc(String bookdesc) {
        this.bookdesc = bookdesc;
    }
    public String getBookyear() {
        return bookyear;
    }

    public void setBookyear(String bookyear) {
        this.bookyear = bookyear;
    }
    public String getBalancesheeturl() {
        return balancesheeturl;
    }

    public void setBalancesheeturl(String balancesheeturl) {
        this.balancesheeturl = balancesheeturl;
    }
    public String getIncomestatementsurl() {
        return incomestatementsurl;
    }

    public void setIncomestatementsurl(String incomestatementsurl) {
        this.incomestatementsurl = incomestatementsurl;
    }
    public String getCashflowstatementsurl() {
        return cashflowstatementsurl;
    }

    public void setCashflowstatementsurl(String cashflowstatementsurl) {
        this.cashflowstatementsurl = cashflowstatementsurl;
    }

    @Override
    public String toString() {
        return "Accbook{" +
            "bookid=" + bookid +
            ", bookname=" + bookname +
            ", orgid=" + orgid +
            ", orgname=" + orgname +
            ", acctid=" + acctid +
            ", bookdesc=" + bookdesc +
            ", bookyear=" + bookyear +
            ", balancesheeturl=" + balancesheeturl +
            ", incomestatementsurl=" + incomestatementsurl +
            ", cashflowstatementsurl=" + cashflowstatementsurl +
        "}";
    }
}
