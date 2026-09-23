package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算模型实体类
 * 
 * @description 预算模型管理实体,支持多种预算模型类型
 * @author AI Assistant
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_MODEL")
@ApiModel(value = "BudgetModel对象", description = "预算模型")
public class BudgetModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "MODEL_ID", type = IdType.ASSIGN_UUID)
    private String modelId;

    @ApiModelProperty(value = "模型名称")
    @TableField("MODEL_NAME")
    private String modelName;

    @ApiModelProperty(value = "模型编码")
    @TableField("MODEL_CODE")
    private String modelCode;

    @ApiModelProperty(value = "模型类型:INCREMENTAL-增量,ZERO_BASED-零基,ROLLING-滚动,FLEXIBLE-弹性,ACTIVITY_BASED-作业,CAPITAL-资本,CASH_FLOW-现金流,CUSTOM-自定义")
    @TableField("MODEL_TYPE")
    private String modelType;

    @ApiModelProperty(value = "模型状态:DRAFT-草稿,ACTIVE-生效,INACTIVE-失效,ARCHIVED-归档")
    @TableField("MODEL_STATUS")
    private String modelStatus;

    @ApiModelProperty(value = "适用范围")
    @TableField("APPLICABLE_SCOPE")
    private String applicableScope;

    @ApiModelProperty(value = "是否启用")
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    @ApiModelProperty(value = "排序序号")
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    @ApiModelProperty(value = "复制来源")
    @TableField("COPIED_FROM")
    private String copiedFrom;

    @ApiModelProperty(value = "预算周期:MONTHLY-月度,QUARTERLY-季度,YEARLY-年度,CUSTOM-自定义")
    @TableField("BUDGET_CYCLE")
    private String budgetCycle;

    @ApiModelProperty(value = "描述")
    @TableField("DESCRIPTION")
    private String description;

    @ApiModelProperty(value = "模型描述")
    @TableField("MODEL_DESCRIPTION")
    private String modelDescription;

    @ApiModelProperty(value = "配置参数(JSON格式)")
    @TableField("CONFIG_PARAMETERS")
    private String configParameters;

    @ApiModelProperty(value = "计算规则")
    @TableField("CALCULATION_RULES")
    private String calculationRules;

    @ApiModelProperty(value = "版本号")
    @TableField("VERSION")
    private String version;

    @ApiModelProperty(value = "版本号")
    @TableField("VERSION_NUM")
    private String versionNum;

    @ApiModelProperty(value = "是否当前版本")
    @TableField("IS_CURRENT_VERSION")
    private Integer isCurrentVersion;

    @ApiModelProperty(value = "计算公式")
    @TableField("FORMULA")
    private String formula;

    @ApiModelProperty(value = "维度信息(JSON)")
    @TableField("DIMENSIONS")
    private String dimensions;

    @ApiModelProperty(value = "状态")
    @TableField("STATUS")
    private String status;

    @ApiModelProperty(value = "创建人ID")
    @TableField("CREATOR_ID")
    private String creatorId;

    @ApiModelProperty(value = "创建人姓名")
    @TableField("CREATOR_NAME")
    private String creatorName;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "更新人ID")
    @TableField("UPDATER_ID")
    private String updaterId;

    @ApiModelProperty(value = "更新人姓名")
    @TableField("UPDATER_NAME")
    private String updaterName;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @ApiModelProperty(value = "删除标记:0-未删除,1-已删除")
    @TableField("IS_DELETED")
    private Integer isDeleted;

    @ApiModelProperty(value = "公司ID")
    @TableField("COMPANY_ID")
    private String companyId;

    @ApiModelProperty(value = "公司名称")
    @TableField("COMPANY_NAME")
    private String companyName;

    // ========== 显式Getter和Setter方法，防止Lombok处理失败 ==========

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getModelStatus() {
        return modelStatus;
    }

    public void setModelStatus(String modelStatus) {
        this.modelStatus = modelStatus;
    }

    public String getApplicableScope() {
        return applicableScope;
    }

    public void setApplicableScope(String applicableScope) {
        this.applicableScope = applicableScope;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getCopiedFrom() {
        return copiedFrom;
    }

    public void setCopiedFrom(String copiedFrom) {
        this.copiedFrom = copiedFrom;
    }

    public String getBudgetCycle() {
        return budgetCycle;
    }

    public void setBudgetCycle(String budgetCycle) {
        this.budgetCycle = budgetCycle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModelDescription() {
        return modelDescription;
    }

    public void setModelDescription(String modelDescription) {
        this.modelDescription = modelDescription;
    }

    public String getConfigParameters() {
        return configParameters;
    }

    public void setConfigParameters(String configParameters) {
        this.configParameters = configParameters;
    }

    public String getCalculationRules() {
        return calculationRules;
    }

    public void setCalculationRules(String calculationRules) {
        this.calculationRules = calculationRules;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }

    public Integer getIsCurrentVersion() {
        return isCurrentVersion;
    }

    public void setIsCurrentVersion(Integer isCurrentVersion) {
        this.isCurrentVersion = isCurrentVersion;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(String updaterId) {
        this.updaterId = updaterId;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}

