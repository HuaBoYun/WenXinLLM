package com.global.treasurer.financialProductDefinition.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 现金流类型实体类
 * 对应数据库表：TBL_CASHFLOW_TYPE
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_CASHFLOW_TYPE")
public class TblCashflowType implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "CASHFLOW_TYPE_ID", type = IdType.INPUT)
    private Long cashflowTypeId;

    /**
     * 现金流类型编码
     */
    @TableField("CASHFLOW_TYPE_CODE")
    private String cashflowTypeCode;

    /**
     * 现金流类型名称
     */
    @TableField("CASHFLOW_TYPE_NAME")
    private String cashflowTypeName;

    /**
     * 现金流方向(INFLOW-流入,OUTFLOW-流出,BOTH-双向)
     */
    @TableField("CASHFLOW_DIRECTION")
    private String cashflowDirection;

    /**
     * 业务分类(OPERATING-经营,INVESTING-投资,FINANCING-筹资)
     */
    @TableField("BUSINESS_CATEGORY")
    private String businessCategory;

    /**
     * 影响类型(DIRECT-直接,INDIRECT-间接)
     */
    @TableField("IMPACT_TYPE")
    private String impactType;

    /**
     * 会计科目
     */
    @TableField("ACCOUNTING_SUBJECT")
    private String accountingSubject;

    /**
     * 预测权重(0-100)
     */
    @TableField("FORECAST_WEIGHT")
    private BigDecimal forecastWeight;

    /**
     * 是否启用(1-启用,0-禁用)
     */
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    /**
     * 父级ID
     */
    @TableField("PARENT_ID")
    private Long parentId;

    /**
     * 排序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 组织ID
     */
    @TableField("ORG_ID")
    private Long orgId;

    /**
     * 子节点列表（非数据库字段，用于树形结构）
     */
    @TableField(exist = false)
    private List<TblCashflowType> children;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getCashflowTypeId() { return cashflowTypeId; }
    public void setCashflowTypeId(Long cashflowTypeId) { this.cashflowTypeId = cashflowTypeId; }
    public String getCashflowTypeCode() { return cashflowTypeCode; }
    public void setCashflowTypeCode(String cashflowTypeCode) { this.cashflowTypeCode = cashflowTypeCode; }
    public String getCashflowTypeName() { return cashflowTypeName; }
    public void setCashflowTypeName(String cashflowTypeName) { this.cashflowTypeName = cashflowTypeName; }
    public String getCashflowDirection() { return cashflowDirection; }
    public void setCashflowDirection(String cashflowDirection) { this.cashflowDirection = cashflowDirection; }
    public String getBusinessCategory() { return businessCategory; }
    public void setBusinessCategory(String businessCategory) { this.businessCategory = businessCategory; }
    public String getImpactType() { return impactType; }
    public void setImpactType(String impactType) { this.impactType = impactType; }
    public String getAccountingSubject() { return accountingSubject; }
    public void setAccountingSubject(String accountingSubject) { this.accountingSubject = accountingSubject; }
    public BigDecimal getForecastWeight() { return forecastWeight; }
    public void setForecastWeight(BigDecimal forecastWeight) { this.forecastWeight = forecastWeight; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public List<TblCashflowType> getChildren() { return children; }
    public void setChildren(List<TblCashflowType> children) { this.children = children; }
}
