package com.global.treasurer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 贷款合同DTO
 * 对应数据库表: TBL_LOAN_CONTRACT
 *
 * @author 华博云开发团队
 * @since 2026-02-09
 */
public class LoanContractDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 合同ID */
    private String contractId;
    /** 关联贷款ID */
    private String loanId;
    /** 合同编号 */
    private String contractNo;
    /** 合同名称 */
    private String contractName;
    /** 合同状态: DRAFT-草稿, ACTIVE-生效, EXPIRED-已到期, TERMINATED-已终止 */
    private String contractStatus;
    /** 合同完整性: COMPLETE-完整, INCOMPLETE-不完整 */
    private String contractCompleteness;
    /** 签署日期 */
    private String signingDate;
    /** 生效日期 */
    private String effectiveDate;
    /** 到期日期 */
    private String expiryDate;
    /** 合同金额 */
    private BigDecimal contractAmount;
    /** 合同文件路径 */
    private String contractFilePath;
    /** 备注 */
    private String remark;

    // 分页参数
    private Integer pageNum = 1;
    private Integer pageSize = 10;

    // Getters and Setters
    public String getContractId() { return contractId; }
    public void setContractId(String contractId) { this.contractId = contractId; }

    public String getLoanId() { return loanId; }
    public void setLoanId(String loanId) { this.loanId = loanId; }

    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }

    public String getContractName() { return contractName; }
    public void setContractName(String contractName) { this.contractName = contractName; }

    public String getContractStatus() { return contractStatus; }
    public void setContractStatus(String contractStatus) { this.contractStatus = contractStatus; }

    public String getContractCompleteness() { return contractCompleteness; }
    public void setContractCompleteness(String contractCompleteness) { this.contractCompleteness = contractCompleteness; }

    public String getSigningDate() { return signingDate; }
    public void setSigningDate(String signingDate) { this.signingDate = signingDate; }

    public String getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(String effectiveDate) { this.effectiveDate = effectiveDate; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    public BigDecimal getContractAmount() { return contractAmount; }
    public void setContractAmount(BigDecimal contractAmount) { this.contractAmount = contractAmount; }

    public String getContractFilePath() { return contractFilePath; }
    public void setContractFilePath(String contractFilePath) { this.contractFilePath = contractFilePath; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }

    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}

