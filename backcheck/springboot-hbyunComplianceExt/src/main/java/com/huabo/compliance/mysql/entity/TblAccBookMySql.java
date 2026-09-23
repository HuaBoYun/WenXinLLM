package com.huabo.compliance.mysql.entity;
// default package

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


/**
 * TblAccBook entity. @author MyEclipse Persistence Tools
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_ACCBOOK")
@Schema(name="TblAccBook")
public class TblAccBookMySql implements Serializable {


    // Fields    
    @TableId("BOOKID")
    private String bookid;
    private TblOrganizationMySql tblOrganizationMySql;
    @TableField("BOOKNAME")
    private String bookname;
    @TableField("ORGNAME")
    private String orgname;
    @TableField("ACCTID")
    private String acctid;
    private TblAccBookMySql parent;
    private Set<TblAccBookMySql> children = new HashSet<TblAccBookMySql>();
    @TableField("BOOKYEAR")
    private String bookyear;
    private BigDecimal staffId;
    private BigDecimal orgId;
    @TableField("BALANCESHEETURL")
    private String balanceSheetUrl;
    @TableField("INCOMESTATEMENTSURL")
    private String incomeStateMentsUrl;
    @TableField("CASHFLOWSTATEMENTSURL")
    private String cashLowsUrl;

    private Set tblManageUserBooks = new HashSet(0);


    // Constructors

    /**
     * default constructor
     */
    public TblAccBookMySql() {
    }

    public TblAccBookMySql(String bookid) {
        this.bookid = bookid;
    }

    public TblAccBookMySql(BigDecimal staffId, BigDecimal orgId) {
        this.staffId = staffId;
        this.orgId = orgId;
    }

    // Property accessors


    public String getBookid() {
        return this.bookid;
    }

    public Set<TblAccBookMySql> getChildren() {
        return children;
    }

    public void setChildren(Set<TblAccBookMySql> children) {
        this.children = children;
    }

    public String getBalanceSheetUrl() {
        return balanceSheetUrl;
    }

    public void setBalanceSheetUrl(String balanceSheetUrl) {
        this.balanceSheetUrl = balanceSheetUrl;
    }

    public String getIncomeStateMentsUrl() {
        return incomeStateMentsUrl;
    }

    public void setIncomeStateMentsUrl(String incomeStateMentsUrl) {
        this.incomeStateMentsUrl = incomeStateMentsUrl;
    }

    public String getCashLowsUrl() {
        return cashLowsUrl;
    }

    public void setCashLowsUrl(String cashLowsUrl) {
        this.cashLowsUrl = cashLowsUrl;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public TblOrganizationMySql getTblOrganization() {
        return this.tblOrganizationMySql;
    }

    public void setTblOrganization(TblOrganizationMySql tblOrganization) {
        this.tblOrganizationMySql = tblOrganization;
    }

    public String getBookname() {
        return this.bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getOrgname() {
        return this.orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getAcctid() {
        return this.acctid;
    }

    public void setAcctid(String acctid) {
        this.acctid = acctid;
    }

    public String getBookyear() {
        return this.bookyear;
    }

    public void setBookyear(String bookyear) {
        this.bookyear = bookyear;
    }

    public Set getTblManageUserBooks() {
        return this.tblManageUserBooks;
    }

    public void setTblManageUserBooks(Set tblManageUserBooks) {
        this.tblManageUserBooks = tblManageUserBooks;
    }

    public BigDecimal getStaffId() {
        return staffId;
    }

    public void setStaffId(BigDecimal staffId) {
        this.staffId = staffId;
    }

    public BigDecimal getOrgId() {
        return orgId;
    }

    public void setOrgId(BigDecimal orgId) {
        this.orgId = orgId;
    }

    public TblAccBookMySql getParent() {
        return parent;
    }

    public void setParent(TblAccBookMySql parent) {
        this.parent = parent;
    }

}