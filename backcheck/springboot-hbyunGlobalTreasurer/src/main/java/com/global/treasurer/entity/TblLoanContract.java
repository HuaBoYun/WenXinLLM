package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 贷款合同实体类
 * 对应数据库表: TBL_LOAN_CONTRACT
 *
 * @author 华博云开发团队
 * @since 2026-02-09
 */
@TableName("TBL_LOAN_CONTRACT")
public class TblLoanContract implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 合同ID (主键)
     */
    @TableId(value = "CONTRACT_ID", type = IdType.ASSIGN_UUID)
    private String contractId;

    /**
     * 关联贷款ID
     */
    @TableField("LOAN_ID")
    private String loanId;

    /**
     * 合同编号
     */
    @TableField("CONTRACT_NO")
    private String contractNo;

    /**
     * 合同名称
     */
    @TableField("CONTRACT_NAME")
    private String contractName;

    /**
     * 合同状态: DRAFT-草稿, ACTIVE-生效, EXPIRED-已到期, TERMINATED-已终止
     */
    @TableField("CONTRACT_STATUS")
    private String contractStatus;

    /**
     * 合同完整性: COMPLETE-完整, INCOMPLETE-不完整
     */
    @TableField("CONTRACT_COMPLETENESS")
    private String contractCompleteness;

    /**
     * 签署日期
     */
    @TableField("SIGNING_DATE")
    private Date signingDate;

    /**
     * 生效日期
     */
    @TableField("EFFECTIVE_DATE")
    private Date effectiveDate;

    /**
     * 到期日期
     */
    @TableField("EXPIRY_DATE")
    private Date expiryDate;

    /**
     * 合同金额
     */
    @TableField("CONTRACT_AMOUNT")
    private BigDecimal contractAmount;

    /**
     * 合同文件路径
     */
    @TableField("CONTRACT_FILE_PATH")
    private String contractFilePath;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

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

    public Date getSigningDate() { return signingDate; }
    public void setSigningDate(Date signingDate) { this.signingDate = signingDate; }

    public Date getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }

    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }

    public BigDecimal getContractAmount() { return contractAmount; }
    public void setContractAmount(BigDecimal contractAmount) { this.contractAmount = contractAmount; }

    public String getContractFilePath() { return contractFilePath; }
    public void setContractFilePath(String contractFilePath) { this.contractFilePath = contractFilePath; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
