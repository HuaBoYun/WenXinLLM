package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("TBL_BUDGET_SYSTEM_CONFIG")
public class BudgetSystemConfigEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "CONFIG_ID", type = IdType.ASSIGN_UUID)
    private String configId;
    @TableField("CONFIG_CODE")
    private String configCode;
    @TableField("CONFIG_NAME")
    private String configName;
    @TableField("CONFIG_TYPE")
    private String configType;
    @TableField("CONFIG_KEY")
    private String configKey;
    @TableField("CONFIG_VALUE")
    private String configValue;
    @TableField("VALUE_TYPE")
    private String valueType;
    @TableField("DEFAULT_VALUE")
    private String defaultValue;
    @TableField("CONFIG_DESC")
    private String configDesc;
    @TableField("IS_ENABLED")
    private Integer isEnabled;
    @TableField("SORT_ORDER")
    private Integer sortOrder;
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
    @TableField("COMPANY_NAME")
    private String companyName;

    // ========== 显式Getter和Setter方法，防止Lombok处理失败 ==========

    public String getConfigId() { return configId; }
    public void setConfigId(String configId) { this.configId = configId; }

    public String getConfigCode() { return configCode; }
    public void setConfigCode(String configCode) { this.configCode = configCode; }

    public String getConfigName() { return configName; }
    public void setConfigName(String configName) { this.configName = configName; }

    public String getConfigType() { return configType; }
    public void setConfigType(String configType) { this.configType = configType; }

    public String getConfigKey() { return configKey; }
    public void setConfigKey(String configKey) { this.configKey = configKey; }

    public String getConfigValue() { return configValue; }
    public void setConfigValue(String configValue) { this.configValue = configValue; }

    public String getValueType() { return valueType; }
    public void setValueType(String valueType) { this.valueType = valueType; }

    public String getDefaultValue() { return defaultValue; }
    public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }

    public String getConfigDesc() { return configDesc; }
    public void setConfigDesc(String configDesc) { this.configDesc = configDesc; }

    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

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

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}

