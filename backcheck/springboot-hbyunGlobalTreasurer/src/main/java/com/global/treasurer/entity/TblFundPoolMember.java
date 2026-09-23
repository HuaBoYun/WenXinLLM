package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金池成员实体类
 * @author Claude
 * @date 2026-01-20
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_FUND_POOL_MEMBER")
@ApiModel(value = "TblFundPoolMember", description = "资金池成员实体")
public class TblFundPoolMember implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "MEMBER_ID", type = IdType.ASSIGN_UUID)
    @ApiModelProperty("成员ID")
    private String memberId;

    @TableField("POOL_ID")
    @ApiModelProperty("资金池ID")
    private String poolId;

    @TableField("COMPANY_ID")
    @ApiModelProperty("公司ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    @ApiModelProperty("公司名称")
    private String companyName;

    @TableField("ACCOUNT_ID")
    @ApiModelProperty("账户ID")
    private String accountId;

    @TableField("ACCOUNT_NO")
    @ApiModelProperty("账号")
    private String accountNo;

    @TableField("ACCOUNT_NAME")
    @ApiModelProperty("账户名称")
    private String accountName;

    @TableField("BANK_NAME")
    @ApiModelProperty("开户银行")
    private String bankName;

    @TableField("MEMBER_TYPE")
    @ApiModelProperty("成员类型(HEADER-头寸户/MEMBER-成员户)")
    private String memberType;

    @TableField("CREDIT_LIMIT")
    @ApiModelProperty("授信额度")
    private BigDecimal creditLimit;

    @TableField("BALANCE")
    @ApiModelProperty("余额")
    private BigDecimal balance;

    @TableField("JOIN_DATE")
    @ApiModelProperty("加入日期")
    private Date joinDate;

    @TableField("STATUS")
    @ApiModelProperty("状态")
    private String status;

    @TableField("CREATE_BY")
    @ApiModelProperty("创建人")
    private String createBy;

    @TableField("CREATE_TIME")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @TableField("UPDATE_BY")
    @ApiModelProperty("更新人")
    private String updateBy;

    @TableField("UPDATE_TIME")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    @TableField("DEL_FLAG")
    @ApiModelProperty("删除标志")
    private String delFlag;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    public String getPoolId() { return poolId; }
    public void setPoolId(String poolId) { this.poolId = poolId; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public String getAccountNo() { return accountNo; }
    public void setAccountNo(String accountNo) { this.accountNo = accountNo; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getMemberType() { return memberType; }
    public void setMemberType(String memberType) { this.memberType = memberType; }
    public BigDecimal getCreditLimit() { return creditLimit; }
    public void setCreditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public Date getJoinDate() { return joinDate; }
    public void setJoinDate(Date joinDate) { this.joinDate = joinDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }
}
