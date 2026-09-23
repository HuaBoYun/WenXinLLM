package com.huabo.system.entity;
// default package

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * TblAccBook entity. @author MyEclipse Persistence Tools
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_ACCBOOK")
@Schema(name="TblAccBook")
public class TblAccBook implements Serializable {


    // Fields    
    @TableId(value="BOOKID",type = IdType.INPUT)
    @Schema(name="账套主键")
     private String bookid;
     private TblOrganization tblOrganization;
     @TableField("BOOKNAME")
     @Schema(name="账套名称")
     private String bookname;
     @TableField("ORGNAME")
     @Schema(name="公司名称")
     private String orgname;
     @TableField("ACCTID")
     @Schema(name="账套库名")
     private String acctid;
     private TblAccBook parent;
	private Set<TblAccBook> children = new HashSet<TblAccBook>();
    @TableField("BOOKYEAR")
    @Schema(name="年份")
    private String bookyear;
    @Schema(name="用户")
     private BigDecimal staffId;
    @Schema(name="公司")
     private BigDecimal orgId;
    @TableField("BALANCESHEETURL")
     private String balanceSheetUrl;
    @TableField("INCOMESTATEMENTSURL")
     private String incomeStateMentsUrl;
    @TableField("CASHFLOWSTATEMENTSURL")
     private String cashLowsUrl;
    
    @TableField(exist = false)
    private Integer startDate;
    
    @TableField(exist = false)
    private Integer endDate;
     
     private Set tblManageUserBooks = new HashSet(0);


    // Constructors

    /** default constructor */
    public TblAccBook() {
    }
    
    public TblAccBook(String bookid){
    	this.bookid = bookid;
    }
    public TblAccBook(BigDecimal staffId, BigDecimal orgId) {
    	this.staffId = staffId;
    	this.orgId = orgId;
    }
    
    // Property accessors

    
    public String getBookid() {
        return this.bookid;
    }
    
    public Set<TblAccBook> getChildren() {
		return children;
	}

	public void setChildren(Set<TblAccBook> children) {
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

    public TblOrganization getTblOrganization() {
        return this.tblOrganization;
    }
    
    public void setTblOrganization(TblOrganization tblOrganization) {
        this.tblOrganization = tblOrganization;
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
	public TblAccBook getParent() {
		return parent;
	}
	public void setParent(TblAccBook parent) {
		this.parent = parent;
	}

}