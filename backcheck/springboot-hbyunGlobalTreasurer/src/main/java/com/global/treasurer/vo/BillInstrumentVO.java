package com.global.treasurer.vo;

// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据VO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
public class BillInstrumentVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 票据ID */
    private Long instrumentId;

    /** 票据编号 */
    private String instrumentNumber;

    /** 票据类型 */
    private String instrumentType;

    /** 票据类型名称 */
    private String instrumentTypeName;

    /** 票据金额 */
    private BigDecimal instrumentAmount;

    /** 币种 */
    private String currency;

    /** 出票日期 */
    private Date issueDate;

    /** 到期日期 */
    private Date maturityDate;

    /** 出票人 */
    private String drawer;

    /** 收款人 */
    private String payee;

    /** 承兑人 */
    private String acceptor;

    /** 票据状态 */
    private String instrumentStatus;

    /** 票据状态名称 */
    private String instrumentStatusName;

    /** 公司ID */
    private String companyId;

    /** 公司名称 */
    private String companyName;

    /** 备注 */
    private String remark;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getInstrumentId() { return instrumentId; }
    public void setInstrumentId(Long instrumentId) { this.instrumentId = instrumentId; }
    public String getInstrumentNumber() { return instrumentNumber; }
    public void setInstrumentNumber(String instrumentNumber) { this.instrumentNumber = instrumentNumber; }
    public String getInstrumentType() { return instrumentType; }
    public void setInstrumentType(String instrumentType) { this.instrumentType = instrumentType; }
    public String getInstrumentTypeName() { return instrumentTypeName; }
    public void setInstrumentTypeName(String instrumentTypeName) { this.instrumentTypeName = instrumentTypeName; }
    public BigDecimal getInstrumentAmount() { return instrumentAmount; }
    public void setInstrumentAmount(BigDecimal instrumentAmount) { this.instrumentAmount = instrumentAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }
    public Date getMaturityDate() { return maturityDate; }
    public void setMaturityDate(Date maturityDate) { this.maturityDate = maturityDate; }
    public String getDrawer() { return drawer; }
    public void setDrawer(String drawer) { this.drawer = drawer; }
    public String getPayee() { return payee; }
    public void setPayee(String payee) { this.payee = payee; }
    public String getAcceptor() { return acceptor; }
    public void setAcceptor(String acceptor) { this.acceptor = acceptor; }
    public String getInstrumentStatus() { return instrumentStatus; }
    public void setInstrumentStatus(String instrumentStatus) { this.instrumentStatus = instrumentStatus; }
    public String getInstrumentStatusName() { return instrumentStatusName; }
    public void setInstrumentStatusName(String instrumentStatusName) { this.instrumentStatusName = instrumentStatusName; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

}
