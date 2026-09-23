package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("TBL_BUDGET_DATA_INTEGRATION")
public class BudgetDataIntegrationConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "INTEGRATION_ID", type = IdType.ASSIGN_UUID)
    private String integrationId;
    @TableField("INTEGRATION_CODE")
    private String integrationCode;
    @TableField("INTEGRATION_NAME")
    private String integrationName;
    @TableField("INTEGRATION_TYPE")
    private String integrationType;
    @TableField("SOURCE_SYSTEM")
    private String sourceSystem;
    @TableField("TARGET_SYSTEM")
    private String targetSystem;
    @TableField("SYNC_FREQUENCY")
    private String syncFrequency;
    @TableField("SYNC_STATUS")
    private String syncStatus;
    @TableField("LAST_SYNC_TIME")
    private Date lastSyncTime;
    @TableField("TOTAL_RECORDS")
    private Integer totalRecords;
    @TableField("SUCCESS_RECORDS")
    private Integer successRecords;
    @TableField("FAILURE_RECORDS")
    private Integer failureRecords;
    @TableField("DATA_MAPPING")
    private String dataMapping;
    @TableField("IS_ENABLED")
    private Integer isEnabled;
    @TableField("REMARK")
    private String remark;
    @TableField("CREATOR_ID")
    private String creatorId;
    @TableField("CREATOR_NAME")
    private String creatorName;
    @TableField("CREATE_TIME")
    private Date createTime;
    @TableField("UPDATER_ID")
    private String updaterId;
    @TableField("UPDATER_NAME")
    private String updaterName;
    @TableField("UPDATE_TIME")
    private Date updateTime;
    @TableField("IS_DELETED")
    private Integer isDeleted;
    @TableField("COMPANY_ID")
    private String companyId;

    // ========== 显式Getter和Setter方法，防止Lombok处理失败 ==========

    public String getIntegrationId() { return integrationId; }
    public void setIntegrationId(String integrationId) { this.integrationId = integrationId; }

    public String getIntegrationCode() { return integrationCode; }
    public void setIntegrationCode(String integrationCode) { this.integrationCode = integrationCode; }

    public String getIntegrationName() { return integrationName; }
    public void setIntegrationName(String integrationName) { this.integrationName = integrationName; }

    public String getIntegrationType() { return integrationType; }
    public void setIntegrationType(String integrationType) { this.integrationType = integrationType; }

    public String getSourceSystem() { return sourceSystem; }
    public void setSourceSystem(String sourceSystem) { this.sourceSystem = sourceSystem; }

    public String getTargetSystem() { return targetSystem; }
    public void setTargetSystem(String targetSystem) { this.targetSystem = targetSystem; }

    public String getSyncFrequency() { return syncFrequency; }
    public void setSyncFrequency(String syncFrequency) { this.syncFrequency = syncFrequency; }

    public String getSyncStatus() { return syncStatus; }
    public void setSyncStatus(String syncStatus) { this.syncStatus = syncStatus; }

    public Date getLastSyncTime() { return lastSyncTime; }
    public void setLastSyncTime(Date lastSyncTime) { this.lastSyncTime = lastSyncTime; }

    public Integer getTotalRecords() { return totalRecords; }
    public void setTotalRecords(Integer totalRecords) { this.totalRecords = totalRecords; }

    public Integer getSuccessRecords() { return successRecords; }
    public void setSuccessRecords(Integer successRecords) { this.successRecords = successRecords; }

    public Integer getFailureRecords() { return failureRecords; }
    public void setFailureRecords(Integer failureRecords) { this.failureRecords = failureRecords; }

    public String getDataMapping() { return dataMapping; }
    public void setDataMapping(String dataMapping) { this.dataMapping = dataMapping; }

    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public String getCreatorId() { return creatorId; }
    public void setCreatorId(String creatorId) { this.creatorId = creatorId; }

    public String getCreatorName() { return creatorName; }
    public void setCreatorName(String creatorName) { this.creatorName = creatorName; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getUpdaterId() { return updaterId; }
    public void setUpdaterId(String updaterId) { this.updaterId = updaterId; }

    public String getUpdaterName() { return updaterName; }
    public void setUpdaterName(String updaterName) { this.updaterName = updaterName; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
}

