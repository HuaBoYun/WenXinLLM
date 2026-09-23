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
 * 票据实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_BILL_INSTRUMENT")
public class TblBillInstrument implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 票据ID */
    @TableId(value = "INSTRUMENT_ID", type = IdType.ASSIGN_ID)
    private Long instrumentId;

    /** 票据编号 */
    @TableField("INSTRUMENT_NO")
    private String instrumentNumber;

    /** 票据类型 */
    @TableField("BILL_TYPE")
    private String instrumentType;

    /** 票据金额 */
    @TableField("BILL_AMOUNT")
    private BigDecimal instrumentAmount;

    /** 币种 */
    @TableField("CURRENCY_CODE")
    private String currency;

    /** 出票日期 */
    @TableField("ISSUE_DATE")
    private Date issueDate;

    /** 到期日期 */
    @TableField("MATURITY_DATE")
    private Date maturityDate;

    /** 出票人 */
    @TableField("DRAWER")
    private String drawer;

    /** 收款人 */
    @TableField("PAYEE")
    private String payee;

    /** 承兑人 */
    @TableField("ACCEPTOR")
    private String acceptor;

    /** 票据状态 */
    @TableField("BILL_STATUS")
    private String instrumentStatus;

    /** 公司ID - 非数据库字段 */
    @TableField(exist = false)
    private String companyId;

    /** 公司名称 - 非数据库字段 */
    @TableField(exist = false)
    private String companyName;

    /** 备注 */
    @TableField("REMARK")
    private String remark;

    /** 删除标志 */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    /** 创建人 */
    @TableField("CREATE_USER")
    private String createBy;

    /** 创建时间 */
    @TableField("CREATE_TIME")
    private Date createTime;

    /** 更新人 */
    @TableField("UPDATE_USER")
    private String updateBy;

    /** 更新时间 */
    @TableField("UPDATE_TIME")
    private Date updateTime;


    // 以下方法由Lombok生成,手动添加以解决编译问题
    public Long getInstrumentId() { return instrumentId; }
    public void setInstrumentId(Long instrumentId) { this.instrumentId = instrumentId; }
    public String getInstrumentNumber() { return instrumentNumber; }
    public void setInstrumentNumber(String instrumentNumber) { this.instrumentNumber = instrumentNumber; }
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
    public String getInstrumentStatus() { return instrumentStatus; }
    public void setInstrumentStatus(String instrumentStatus) { this.instrumentStatus = instrumentStatus; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
