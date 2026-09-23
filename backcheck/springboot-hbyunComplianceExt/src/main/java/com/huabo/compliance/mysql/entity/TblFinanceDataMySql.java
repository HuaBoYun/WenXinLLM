package com.huabo.compliance.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FINANCEDATA")
@Schema(name="TblFinanceData对象")
public class TblFinanceDataMySql implements Serializable {
    private static final long serialVersionUID = 1509385990408224564L;

    @TableId("ORDERID")
    @Id
    private BigDecimal orderId;
    @TableField("OPERTYPE")
    private String operType;
    @TableField("SOURCEDBTYPE")
    private String sourceDbType;
    @TableField("SOURCECONN")
    private String sourceConn;
    @TableField("SOURCEUSERID")
    private String sourceUserId;
    @TableField("SOURCEPASSWORD")
    private String sourcePassWord;
    @TableField("DESTDBTYPE")
    private String destDbType;
    @TableField("DESTCONN")
    private String destConn;
    @TableField("DESTUSERID")
    private String destUserId;
    @TableField("DESTPASSWORD")
    private String destPassWord;
    @TableField("COMPANYID")
    private String companyId;
    @TableField("COMPANYNAME")
    private String companyName;
    @TableField("STATUS")
    private Integer status;
    @TableField("FID")
    private String fid;//版本
    @TableField("FVENDOR")
    private String fvendor;//版本名称
    @TableField("STARTDATE")
    private Integer startdate;//开始年份
    @TableField("ENDDATE")
    private Integer enddate;//结束年份
    @TableField("DESTCOMPANYID")
    private String destcompanyid;
    @TableField("DESTBOOKID")
    private String destbookid;


    public TblFinanceDataMySql() {
        super();
        // TODO Auto-generated constructor stub
    }

    public TblFinanceDataMySql(BigDecimal orderId, String operType, String sourceDbType, String sourceConn,
                               String sourceUserId, String sourcePassWord, String destDbType, String destConn, String destUserId,
                               String destPassWord, String companyId, String companyName, Integer status, String fid, String fvendor,
                               Integer startdate, Integer enddate, String destcompanyid, String destbookid) {
        super();
        this.orderId = orderId;
        this.operType = operType;
        this.sourceDbType = sourceDbType;
        this.sourceConn = sourceConn;
        this.sourceUserId = sourceUserId;
        this.sourcePassWord = sourcePassWord;
        this.destDbType = destDbType;
        this.destConn = destConn;
        this.destUserId = destUserId;
        this.destPassWord = destPassWord;
        this.companyId = companyId;
        this.companyName = companyName;
        this.status = status;
        this.fid = fid;
        this.fvendor = fvendor;
        this.startdate = startdate;
        this.enddate = enddate;
        this.destcompanyid = destcompanyid;
        this.destbookid = destbookid;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }


}
