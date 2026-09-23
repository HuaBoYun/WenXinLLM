package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除
// import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 账户冻结记录实体类
 * 
 * @author system
 * @since 2024-01-01
 */
// @Data // 已移除,使用手动编写的getter/setter
// @EqualsAndHashCode // 已移除
@TableName("t_am_account_freeze_record")
public class AmAccountFreezeRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 账户ID
     */
    @TableField("account_id")
    private Long accountId;

    /**
     * 冻结类型：JUDICIAL-司法冻结，INTERNAL-内部冻结，REGULATORY-监管冻结
     */
    @TableField("freeze_type")
    private String freezeType;

    /**
     * 冻结原因
     */
    @TableField("freeze_reason")
    private String freezeReason;

    /**
     * 冻结金额（部分冻结时使用）
     */
    @TableField("freeze_amount")
    private BigDecimal freezeAmount;

    /**
     * 冻结范围：FULL-全部冻结，PARTIAL-部分冻结
     */
    @TableField("freeze_scope")
    private String freezeScope;

    /**
     * 法院文号
     */
    @TableField("court_document_no")
    private String courtDocumentNo;

    /**
     * 冻结开始日期
     */
    @TableField("freeze_start_date")
    private LocalDate freezeStartDate;

    /**
     * 冻结结束日期
     */
    @TableField("freeze_end_date")
    private LocalDate freezeEndDate;

    /**
     * 冻结状态：ACTIVE-生效中，EXPIRED-已过期，UNFROZEN-已解冻
     */
    @TableField("freeze_status")
    private String freezeStatus;

    /**
     * 解冻日期
     */
    @TableField("unfreeze_date")
    private LocalDate unfreezeDate;

    /**
     * 解冻原因
     */
    @TableField("unfreeze_reason")
    private String unfreezeReason;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 状态：1-有效，0-无效
     */
    @TableField("status")
    private String status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField("create_user")
    private Long createUser;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @TableField("update_user")
    private Long updateUser;

    /**
     * 版本号
     */
    @TableField("version_no")
    private Long versionNo;

    /**
     * 客户端IP
     */
    @TableField("client_ip")
    private String clientIp;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public String getFreezeType() { return freezeType; }
    public void setFreezeType(String freezeType) { this.freezeType = freezeType; }
    public String getFreezeReason() { return freezeReason; }
    public void setFreezeReason(String freezeReason) { this.freezeReason = freezeReason; }
    public BigDecimal getFreezeAmount() { return freezeAmount; }
    public void setFreezeAmount(BigDecimal freezeAmount) { this.freezeAmount = freezeAmount; }
    public String getFreezeScope() { return freezeScope; }
    public void setFreezeScope(String freezeScope) { this.freezeScope = freezeScope; }
    public String getCourtDocumentNo() { return courtDocumentNo; }
    public void setCourtDocumentNo(String courtDocumentNo) { this.courtDocumentNo = courtDocumentNo; }
    public LocalDate getFreezeStartDate() { return freezeStartDate; }
    public void setFreezeStartDate(LocalDate freezeStartDate) { this.freezeStartDate = freezeStartDate; }
    public LocalDate getFreezeEndDate() { return freezeEndDate; }
    public void setFreezeEndDate(LocalDate freezeEndDate) { this.freezeEndDate = freezeEndDate; }
    public String getFreezeStatus() { return freezeStatus; }
    public void setFreezeStatus(String freezeStatus) { this.freezeStatus = freezeStatus; }
    public LocalDate getUnfreezeDate() { return unfreezeDate; }
    public void setUnfreezeDate(LocalDate unfreezeDate) { this.unfreezeDate = unfreezeDate; }
    public String getUnfreezeReason() { return unfreezeReason; }
    public void setUnfreezeReason(String unfreezeReason) { this.unfreezeReason = unfreezeReason; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public Long getCreateUser() { return createUser; }
    public void setCreateUser(Long createUser) { this.createUser = createUser; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Long getUpdateUser() { return updateUser; }
    public void setUpdateUser(Long updateUser) { this.updateUser = updateUser; }
    public Long getVersionNo() { return versionNo; }
    public void setVersionNo(Long versionNo) { this.versionNo = versionNo; }
    public String getClientIp() { return clientIp; }
    public void setClientIp(String clientIp) { this.clientIp = clientIp; }
}
