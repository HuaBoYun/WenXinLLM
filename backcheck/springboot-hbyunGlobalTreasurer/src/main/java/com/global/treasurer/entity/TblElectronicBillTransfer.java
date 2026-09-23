package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 电子票据流转记录实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_ELECTRONIC_BILL_TRANSFER")
public class TblElectronicBillTransfer implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "TRANSFER_ID", type = IdType.INPUT)
    private Long transferId;

    @TableField("BILL_ID")
    private Long billId;

    @TableField("BILL_NUMBER")
    private String billNumber;

    @TableField("FROM_PARTY")
    private String fromParty;

    @TableField("TO_PARTY")
    private String toParty;

    @TableField("TRANSFER_TYPE")
    private String transferType;

    @TableField("TRANSFER_TIME")
    private Date transferTime;

    @TableField("TRANSFER_AMOUNT")
    private BigDecimal transferAmount;

    @TableField("TRANSFER_STATUS")
    private String transferStatus;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("COMPANY_ID")
    private Long companyId;

    @TableField("DELETE_FLAG")
    private Integer deleteFlag;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getTransferId() { return transferId; }
    public void setTransferId(Long transferId) { this.transferId = transferId; }
    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public String getFromParty() { return fromParty; }
    public void setFromParty(String fromParty) { this.fromParty = fromParty; }
    public String getToParty() { return toParty; }
    public void setToParty(String toParty) { this.toParty = toParty; }
    public String getTransferType() { return transferType; }
    public void setTransferType(String transferType) { this.transferType = transferType; }
    public Date getTransferTime() { return transferTime; }
    public void setTransferTime(Date transferTime) { this.transferTime = transferTime; }
    public BigDecimal getTransferAmount() { return transferAmount; }
    public void setTransferAmount(BigDecimal transferAmount) { this.transferAmount = transferAmount; }
    public String getTransferStatus() { return transferStatus; }
    public void setTransferStatus(String transferStatus) { this.transferStatus = transferStatus; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
}
