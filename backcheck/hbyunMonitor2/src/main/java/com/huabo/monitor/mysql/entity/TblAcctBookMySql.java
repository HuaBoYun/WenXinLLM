package com.huabo.monitor.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_ACCTBOOK")
@Schema(name="TblAcctBook")
public class TblAcctBookMySql {

    @TableId("BOOKID")
    private String bookid;
    private TblOrganizationMySql tblOrganizationMySql;
    @TableField("BOOKNAME")
    private String bookname;
    @TableField("ORGNAME")
    private String orgname;
    @TableField("ACCTID")
    private String acctid;
    //    private TblAcctBook parent;
//    private Set<TblAcctBook> children = new HashSet();
    @TableField("BOOKYEAR")
    private String bookyear;
    private BigDecimal staffId;
    @TableField("ORGID")
    private BigDecimal orgId;
    @TableField("BALANCESHEETURL")
    private String balanceSheetUrl;
    @TableField("INCOMESTATEMENTSURL")
    private String incomeStateMentsUrl;
    @TableField("CASHFLOWSTATEMENTSURL")
    private String cashLowsUrl;
    private Set tblManageUserBooks = new HashSet(0);

    public TblAcctBookMySql() {
    }

    public TblAcctBookMySql(String bookid) {
        this.bookid = bookid;
    }

    public TblAcctBookMySql(BigDecimal staffId, BigDecimal orgId) {
        this.staffId = staffId;
        this.orgId = orgId;
    }

    public void setOrgId(int intValue) {


    }
}
