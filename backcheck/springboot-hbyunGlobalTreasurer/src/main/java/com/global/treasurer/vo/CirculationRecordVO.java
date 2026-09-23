package com.global.treasurer.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import java.math.BigDecimal;
import java.util.Date;

/**
 * 电子票据流转记录VO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@ApiModel("电子票据流转记录VO")
public class CirculationRecordVO {
    @ApiModelProperty("记录ID")
    private Long recordId;

    @ApiModelProperty("票据ID")
    private Long billId;

    @ApiModelProperty("记录类型")
    private String recordType;

    @ApiModelProperty("记录类型名称")
    private String recordTypeName;

    @ApiModelProperty("转出方")
    private String fromHolder;

    @ApiModelProperty("转入方")
    private String toHolder;

    @ApiModelProperty("交易时间")
    private Date transactionTime;

    @ApiModelProperty("操作人")
    private String operatorName;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("备注")
    private String remark;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }
    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }
    public String getRecordType() { return recordType; }
    public void setRecordType(String recordType) { this.recordType = recordType; }
    public String getRecordTypeName() { return recordTypeName; }
    public void setRecordTypeName(String recordTypeName) { this.recordTypeName = recordTypeName; }
    public String getFromHolder() { return fromHolder; }
    public void setFromHolder(String fromHolder) { this.fromHolder = fromHolder; }
    public String getToHolder() { return toHolder; }
    public void setToHolder(String toHolder) { this.toHolder = toHolder; }
    public Date getTransactionTime() { return transactionTime; }
    public void setTransactionTime(Date transactionTime) { this.transactionTime = transactionTime; }
    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
