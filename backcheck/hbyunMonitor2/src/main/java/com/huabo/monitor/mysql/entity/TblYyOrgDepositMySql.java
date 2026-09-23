package com.huabo.monitor.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * TblYyOrgDeposit entity. @author MyEclipse Persistence Tools
 * 用户购买望远镜存款表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YY_ORG_DEPOSIT")
@Schema(name="TblYyOrgDepositMySql对象")
public class TblYyOrgDepositMySql implements java.io.Serializable {

    private static final long serialVersionUID = -7428773810836250078L;

    @TableId("DEPOSITID")
    private BigDecimal depositid; //存款信息主键

    private TblOrganizationMySql tblOrganization; //隶属组织
    @TableField("TOTALMONEY")
    private Double totalmoney; //总金额
    @TableField("TOTALPAYMONEY")
    private Double totalpaymoney; // 总花费金额
    @TableField("LASTDATE")
    private Date lastdate; //最后一次充值到账时间


    public TblYyOrgDepositMySql() {
    }

    public TblYyOrgDepositMySql(TblOrganizationMySql tblOrganization, Double totalmoney,
                                Double totalpaymoney, Date lastdate) {
        this.tblOrganization = tblOrganization;
        this.totalmoney = totalmoney;
        this.totalpaymoney = totalpaymoney;
        this.lastdate = lastdate;
    }

    public BigDecimal getDepositid() {
        return this.depositid;
    }

    public void setDepositid(BigDecimal depositid) {
        this.depositid = depositid;
    }

    public TblOrganizationMySql getTblOrganization() {
        return this.tblOrganization;
    }

    public void setTblOrganization(TblOrganizationMySql tblOrganization) {
        this.tblOrganization = tblOrganization;
    }

    public Double getTotalmoney() {
        if (this.totalmoney == null) {
            return 0.0;
        }
        return this.totalmoney;
    }

    public void setTotalmoney(Double totalmoney) {
        this.totalmoney = totalmoney;
    }

    public Double getTotalpaymoney() {
        if (this.totalpaymoney == null) {
            return 0.0;
        }
        return this.totalpaymoney;
    }

    public void setTotalpaymoney(Double totalpaymoney) {
        this.totalpaymoney = totalpaymoney;
    }

    public Date getLastdate() {
        return lastdate;
    }

    public void setLastdate(Date lastdate) {
        this.lastdate = lastdate;
    }
}