package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
public class BillInstrumentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 票据ID */
    private Long instrumentId;

    /** 票据类型 */
    @NotBlank(message = "票据类型不能为空")
    private String instrumentType;

    /** 票据金额 */
    @NotNull(message = "票据金额不能为空")
    private BigDecimal instrumentAmount;

    /** 币种 */
    private String currency;

    /** 出票日期 */
    private Date issueDate;

    /** 到期日期 */
    @NotNull(message = "到期日期不能为空")
    private Date maturityDate;

    /** 出票人 */
    @NotBlank(message = "出票人不能为空")
    private String drawer;

    /** 收款人 */
    @NotBlank(message = "收款人不能为空")
    private String payee;

    /** 承兑人 */
    private String acceptor;

    /** 公司ID */
    private String companyId;

    /** 公司名称 */
    private String companyName;

    /** 备注 */
    private String remark;


    // 以下方法由Lombok生成,手动添加以解决编译问题
    public Long getInstrumentId() { return instrumentId; }
    public void setInstrumentId(Long instrumentId) { this.instrumentId = instrumentId; }
    public String getInstrumentType() { return instrumentType; }
    public void setInstrumentType(String instrumentType) { this.instrumentType = instrumentType; }
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
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
