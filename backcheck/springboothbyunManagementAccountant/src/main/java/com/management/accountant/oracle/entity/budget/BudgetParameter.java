package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算参数实体
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("TBL_BUDGET_PARAMETER")
public class BudgetParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 参数ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String parameterId;

    /**
     * 参数编码
     */
    private String parameterCode;

    /**
     * 参数名称
     */
    private String parameterName;

    /**
     * 参数类型 (STRING/NUMBER/DATE/BOOLEAN/JSON)
     */
    private String parameterType;

    /**
     * 参数分类
     */
    private String parameterCategory;

    /**
     * 参数值
     */
    private String parameterValue;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 参数单位
     */
    private String parameterUnit;

    /**
     * 参数范围 (SYSTEM/BUSINESS/CUSTOM)
     */
    private String parameterScope;

    /**
     * 当前值
     */
    private String currentValue;

    /**
     * 验证规则（正则表达式）
     */
    private String validationRule;

    /**
     * 是否只读
     */
    private Boolean isReadonly;

    /**
     * 是否加密存储
     */
    private Boolean isEncrypted;

    /**
     * 生效范围（逗号分隔，如 GLOBAL,COMPANY）
     */
    private String effectiveScope;

    /**
     * 最后修改时间（冗余字段，同 updateTime）
     */
    private Date lastModifyTime;

    /**
     * 是否必填
     */
    private Boolean isRequired;

    /**
     * 是否启用
     */
    private Boolean isEnabled;

    /**
     * 排序序号
     */
    private Integer sortOrder;

    /**
     * 参数描述
     */
    private String parameterDescription;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除标志 (0-未删除 1-已删除)
     */
    @TableLogic
    private Integer delFlag;

    // ========== 显式 Getter/Setter，防止 Lombok 处理失败 ==========

    public String getParameterId() { return parameterId; }
    public void setParameterId(String parameterId) { this.parameterId = parameterId; }

    public String getParameterCode() { return parameterCode; }
    public void setParameterCode(String parameterCode) { this.parameterCode = parameterCode; }

    public String getParameterName() { return parameterName; }
    public void setParameterName(String parameterName) { this.parameterName = parameterName; }

    public String getParameterType() { return parameterType; }
    public void setParameterType(String parameterType) { this.parameterType = parameterType; }

    public String getParameterCategory() { return parameterCategory; }
    public void setParameterCategory(String parameterCategory) { this.parameterCategory = parameterCategory; }

    public String getParameterValue() { return parameterValue; }
    public void setParameterValue(String parameterValue) { this.parameterValue = parameterValue; }

    public String getDefaultValue() { return defaultValue; }
    public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }

    public String getParameterUnit() { return parameterUnit; }
    public void setParameterUnit(String parameterUnit) { this.parameterUnit = parameterUnit; }

    public String getParameterScope() { return parameterScope; }
    public void setParameterScope(String parameterScope) { this.parameterScope = parameterScope; }

    public String getCurrentValue() { return currentValue; }
    public void setCurrentValue(String currentValue) { this.currentValue = currentValue; }

    public String getValidationRule() { return validationRule; }
    public void setValidationRule(String validationRule) { this.validationRule = validationRule; }

    public Boolean getIsRequired() { return isRequired; }
    public void setIsRequired(Boolean isRequired) { this.isRequired = isRequired; }

    public Boolean getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }

    public Boolean getIsReadonly() { return isReadonly; }
    public void setIsReadonly(Boolean isReadonly) { this.isReadonly = isReadonly; }

    public Boolean getIsEncrypted() { return isEncrypted; }
    public void setIsEncrypted(Boolean isEncrypted) { this.isEncrypted = isEncrypted; }

    public String getEffectiveScope() { return effectiveScope; }
    public void setEffectiveScope(String effectiveScope) { this.effectiveScope = effectiveScope; }

    public Date getLastModifyTime() { return lastModifyTime; }
    public void setLastModifyTime(Date lastModifyTime) { this.lastModifyTime = lastModifyTime; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public String getParameterDescription() { return parameterDescription; }
    public void setParameterDescription(String parameterDescription) { this.parameterDescription = parameterDescription; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public Integer getDelFlag() { return delFlag; }
    public void setDelFlag(Integer delFlag) { this.delFlag = delFlag; }
}

