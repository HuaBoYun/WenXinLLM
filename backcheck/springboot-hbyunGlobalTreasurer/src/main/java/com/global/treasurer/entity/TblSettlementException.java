package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("TBL_SETTLEMENT_EXCEPTION")
public class TblSettlementException implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @TableField("EXCEPTION_ID")
    private Long exceptionId;

    @TableField("EXCEPTION_NO")
    private String exceptionNo;

    @TableField("EXCEPTION_LEVEL")
    private String exceptionLevel;

    @TableField("EXCEPTION_TYPE")
    private String exceptionType;

    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    @TableField("EXCEPTION_STATUS")
    private String exceptionStatus;

    @TableField("ASSIGNEE")
    private String assignee;

    @TableField("ASSIGNEE_NAME")
    private String assigneeName;

    @TableField("RESOLVE_NOTES")
    private String resolveNotes;

    @TableField("RESOLVE_TIME")
    private Date resolveTime;

    @TableField("ESCALATION_LEVEL")
    private Integer escalationLevel;

    @TableField("RELATED_BUSINESS_NO")
    private String relatedBusinessNo;

    @TableField("RELATED_BATCH_ID")
    private Long relatedBatchId;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("CREATE_USER")
    private String createUser;

    @TableField("UPDATE_USER")
    private String updateUser;

    @TableField("ORG_ID")
    private Long orgId;

    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    public Long getExceptionId() { return exceptionId; }
    public void setExceptionId(Long exceptionId) { this.exceptionId = exceptionId; }
    public String getExceptionNo() { return exceptionNo; }
    public void setExceptionNo(String exceptionNo) { this.exceptionNo = exceptionNo; }
    public String getExceptionLevel() { return exceptionLevel; }
    public void setExceptionLevel(String exceptionLevel) { this.exceptionLevel = exceptionLevel; }
    public String getExceptionType() { return exceptionType; }
    public void setExceptionType(String exceptionType) { this.exceptionType = exceptionType; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public String getExceptionStatus() { return exceptionStatus; }
    public void setExceptionStatus(String exceptionStatus) { this.exceptionStatus = exceptionStatus; }
    public String getAssignee() { return assignee; }
    public void setAssignee(String assignee) { this.assignee = assignee; }
    public String getAssigneeName() { return assigneeName; }
    public void setAssigneeName(String assigneeName) { this.assigneeName = assigneeName; }
    public String getResolveNotes() { return resolveNotes; }
    public void setResolveNotes(String resolveNotes) { this.resolveNotes = resolveNotes; }
    public Date getResolveTime() { return resolveTime; }
    public void setResolveTime(Date resolveTime) { this.resolveTime = resolveTime; }
    public Integer getEscalationLevel() { return escalationLevel; }
    public void setEscalationLevel(Integer escalationLevel) { this.escalationLevel = escalationLevel; }
    public String getRelatedBusinessNo() { return relatedBusinessNo; }
    public void setRelatedBusinessNo(String relatedBusinessNo) { this.relatedBusinessNo = relatedBusinessNo; }
    public Long getRelatedBatchId() { return relatedBatchId; }
    public void setRelatedBatchId(Long relatedBatchId) { this.relatedBatchId = relatedBatchId; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
}
