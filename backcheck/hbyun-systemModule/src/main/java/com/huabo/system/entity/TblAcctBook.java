package com.huabo.system.entity;

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

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_ACCBOOK")
@Schema(name="TblAcctBook")
public class TblAcctBook {

    @TableId(value="BOOKID",type = IdType.INPUT)
    @Schema(name= "主键")
    private String bookid;
    private TblOrganization tblOrganization;
    @TableField("BOOKNAME")
    @Schema(name= "账套名称")
    private String bookname;
    @TableField("ORGNAME")
    @Schema(name= "公司名称")
    private String orgname;
    @TableField("ACCTID")
    @Schema(name= "账套")
    private String acctid;
//    private TblAcctBook parent;
//    private Set<TblAcctBook> children = new HashSet();
    @TableField("BOOKYEAR")
    @Schema(name= "年份")
    private String bookyear;
    private BigDecimal staffId;
    @TableField("ORGID")
    @Schema(name= "隶属公司ID")
    private BigDecimal orgId;
    @TableField("BALANCESHEETURL")
    @Schema(name= "资产负责表地址")
    private String balanceSheetUrl;
    @Schema(name= "利润表地址")
    @TableField("INCOMESTATEMENTSURL")
    private String incomeStateMentsUrl;
    @Schema(name= "现金流量表地址")
    @TableField("CASHFLOWSTATEMENTSURL")
    private String cashLowsUrl;
    
    @Schema(name= "公司名称")
    @TableField(exist = false)
    private String companyName;
    
    
    private Set tblManageUserBooks = new HashSet(0);

    public TblAcctBook() {
    }

    public TblAcctBook(String bookid) {
        this.bookid = bookid;
    }

    public TblAcctBook(BigDecimal staffId, BigDecimal orgId) {
        this.staffId = staffId;
        this.orgId = orgId;
    }

    public void setOrgId(int intValue) {


    }
}
