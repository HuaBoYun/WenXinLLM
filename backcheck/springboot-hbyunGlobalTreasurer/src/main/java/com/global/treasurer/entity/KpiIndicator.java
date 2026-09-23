package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * KPI指标实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-12
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_KPI_INDICATOR")
public class KpiIndicator implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * KPI ID
     */
    @TableId(value = "KPI_ID", type = IdType.AUTO)
    private Long kpiId;

    /**
     * KPI编码
     */
    @TableField("KPI_CODE")
    private String kpiCode;

    /**
     * KPI名称
     */
    @TableField("KPI_NAME")
    private String kpiName;

    /**
     * KPI分类(FINANCIAL/OPERATIONAL/RISK/PERFORMANCE/CUSTOMER/PROCESS)
     */
    @TableField("KPI_CATEGORY")
    private String kpiCategory;

    /**
     * KPI类型(FINANCIAL/OPERATIONAL/RISK/PERFORMANCE)
     */
    @TableField("KPI_TYPE")
    private String kpiType;

    /**
     * 计算公式
     */
    @TableField("CALCULATION_FORMULA")
    private String calculationFormula;

    /**
     * 数据源
     */
    @TableField("DATA_SOURCE")
    private String dataSource;

    /**
     * 单位
     */
    @TableField("UNIT")
    private String unit;

    /**
     * 目标值
     */
    @TableField("TARGET_VALUE")
    private BigDecimal targetValue;

    /**
     * 预警阈值
     */
    @TableField("WARNING_THRESHOLD")
    private BigDecimal warningThreshold;

    /**
     * 临界阈值
     */
    @TableField("CRITICAL_THRESHOLD")
    private BigDecimal criticalThreshold;

    /**
     * 当前值
     */
    @TableField("CURRENT_VALUE")
    private BigDecimal currentValue;

    /**
     * 计算频率(REAL_TIME/HOURLY/DAILY/WEEKLY/MONTHLY)
     */
    @TableField("CALCULATION_FREQUENCY")
    private String calculationFrequency;

    /**
     * 计算周期(DAILY/WEEKLY/MONTHLY/QUARTERLY/YEARLY)
     * 注意：此字段不对应数据库列，仅用于业务逻辑
     */
    @TableField(exist = false)
    private String calculationCycle;

    /**
     * 趋势(UP/DOWN/STABLE)
     */
    @TableField("TREND")
    private String trend;

    /**
     * KPI状态(NORMAL/WARNING/CRITICAL/INACTIVE)
     */
    @TableField("KPI_STATUS")
    private String kpiStatus;

    /**
     * 计算日期
     */
    @TableField("CALCULATION_DATE")
    private LocalDate calculationDate;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 组织ID
     */
    @TableField("ORG_ID")
    private Long orgId;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private Long updateBy;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 删除标志(0正常1删除)
     */
    @TableField("DEL_FLAG")
    private String delFlag;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getKpiId() { return kpiId; }
    public void setKpiId(Long kpiId) { this.kpiId = kpiId; }
    public String getKpiCode() { return kpiCode; }
    public void setKpiCode(String kpiCode) { this.kpiCode = kpiCode; }
    public String getKpiName() { return kpiName; }
    public void setKpiName(String kpiName) { this.kpiName = kpiName; }
    public String getKpiCategory() { return kpiCategory; }
    public void setKpiCategory(String kpiCategory) { this.kpiCategory = kpiCategory; }
    public String getKpiType() { return kpiType; }
    public void setKpiType(String kpiType) { this.kpiType = kpiType; }
    public String getCalculationFormula() { return calculationFormula; }
    public void setCalculationFormula(String calculationFormula) { this.calculationFormula = calculationFormula; }
    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public BigDecimal getTargetValue() { return targetValue; }
    public void setTargetValue(BigDecimal targetValue) { this.targetValue = targetValue; }
    public BigDecimal getWarningThreshold() { return warningThreshold; }
    public void setWarningThreshold(BigDecimal warningThreshold) { this.warningThreshold = warningThreshold; }
    public BigDecimal getCriticalThreshold() { return criticalThreshold; }
    public void setCriticalThreshold(BigDecimal criticalThreshold) { this.criticalThreshold = criticalThreshold; }
    public BigDecimal getCurrentValue() { return currentValue; }
    public void setCurrentValue(BigDecimal currentValue) { this.currentValue = currentValue; }
    public String getCalculationFrequency() { return calculationFrequency; }
    public void setCalculationFrequency(String calculationFrequency) { this.calculationFrequency = calculationFrequency; }
    public String getCalculationCycle() { return calculationCycle; }
    public void setCalculationCycle(String calculationCycle) { this.calculationCycle = calculationCycle; }
    public String getTrend() { return trend; }
    public void setTrend(String trend) { this.trend = trend; }
    public String getKpiStatus() { return kpiStatus; }
    public void setKpiStatus(String kpiStatus) { this.kpiStatus = kpiStatus; }
    public LocalDate getCalculationDate() { return calculationDate; }
    public void setCalculationDate(LocalDate calculationDate) { this.calculationDate = calculationDate; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public Long getCreateBy() { return createBy; }
    public void setCreateBy(Long createBy) { this.createBy = createBy; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public Long getUpdateBy() { return updateBy; }
    public void setUpdateBy(Long updateBy) { this.updateBy = updateBy; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }

    @Override
    public String toString() {
        return "KpiIndicator{" +
                "kpiId=" + kpiId +
                ", kpiCode='" + kpiCode + '\'' +
                ", kpiName='" + kpiName + '\'' +
                ", kpiCategory='" + kpiCategory + '\'' +
                ", kpiType='" + kpiType + '\'' +
                ", currentValue=" + currentValue +
                ", targetValue=" + targetValue +
                ", kpiStatus='" + kpiStatus + '\'' +
                ", orgId=" + orgId +
                '}';
    }

}
